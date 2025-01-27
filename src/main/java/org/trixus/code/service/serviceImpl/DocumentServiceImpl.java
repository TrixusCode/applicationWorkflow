package org.trixus.code.service.serviceImpl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.trixus.code.model.Application;
import org.trixus.code.model.Document;
import org.trixus.code.repository.DocumentRepo;
import org.trixus.code.service.ApplicationService;
import org.trixus.code.service.DocumentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final String UPLOAD_DIR = "uploads/documents/";
    private final ApplicationService applicationService;
    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);


    private final DocumentRepo documentRepo;
    private ModelMapper modelMapper;

    public DocumentServiceImpl(ApplicationService applicationService, DocumentRepo documentRepo) {
        this.applicationService = applicationService;
        this.documentRepo = documentRepo;
    }
    @Override
    public void uploadDocument(Long applicationId, MultipartFile file, byte[] content, String documentType) {
        validateInput(applicationId, file, content, documentType);
        String uniqueFileName = generateUniqueFileName(documentType);
        try {
            Application application = getApplicationById(applicationId);

            Path directoryPath = prepareDirectory(application.getApplicationNumber());
            Path filePath = directoryPath.resolve(uniqueFileName);

            Files.write(filePath, content);

            Document document = createDocumentMetadata(applicationId, documentType, filePath.toString(), uniqueFileName);
            saveDocumentsMetaData(document);
        } catch (IOException e) {
            logger.error("File upload failed for applicationId: {}, documentType: {}", applicationId, documentType, e);
            throw new RuntimeException("Failed to upload file: " + documentType, e);
        }
    }

    private void validateInput(Long applicationId, MultipartFile file, byte[] content, String documentType) {
        if (applicationId == null || file == null || content == null || documentType == null || documentType.isBlank()) {
            throw new IllegalArgumentException("Invalid input provided for document upload.");
        }
    }

    private Application getApplicationById(Long applicationId) {
        return applicationService.getApplicationById(applicationId)
                .map(applicationDto -> modelMapper.map(applicationDto, Application.class))
                .orElseThrow(() -> new RuntimeException("No application found for ID: " + applicationId));
    }

    private Path prepareDirectory(String applicationNumber) throws IOException {
        Path directoryPath = Paths.get(UPLOAD_DIR, applicationNumber);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        return directoryPath;
    }

    private String generateUniqueFileName(String documentType) {
        return UUID.randomUUID() + "_" + StringUtils.cleanPath(documentType);
    }

    private Document createDocumentMetadata( Long applicationId, String documentType, String filePath, String uniqueFileName) {
        Document document = new Document();
        document.setDocumentType(documentType);
        document.setCreatedDate(LocalDateTime.now());
        document.setApplicationId(applicationId);
        document.setFileName(uniqueFileName);
        document.setFilePath(filePath);
        return document;
    }

    private void saveDocumentsMetaData(Document document) {
        documentRepo.save(document);
    }


    @Override
    public byte[] downloadDocument(Long applicationId, String fileName) {
        try {

            Optional<Document> documentOpt = documentRepo.findByApplicationIdAndFileName(applicationId, fileName);

            if (documentOpt.isEmpty()) {
                throw new RuntimeException("Document not found for applicationId: " + applicationId + " and fileName: " + fileName);
            }

            Document document = documentOpt.get();


            Path filePath = Paths.get(document.getFilePath());


            if (Files.exists(filePath)) {
                return Files.readAllBytes(filePath);
            } else {
                throw new RuntimeException("File not found on disk for document: " + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to download document: " + fileName, e);
        }
    }

}
