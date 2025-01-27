package org.trixus.code.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.trixus.code.dto.DocumentDto;
import org.trixus.code.service.DocumentService;

import java.io.IOException;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    @PostMapping("/upload/{applicationId}")
    public ResponseEntity<String> uploadDocument (@PathVariable("applicationId") Long applicationId, @RequestParam("file") MultipartFile file) throws IOException {
      documentService.uploadDocument(applicationId, file, file.getBytes(),file.getContentType());
      return ResponseEntity.status(HttpStatus.CREATED).body("Document Uploaded Successfully");
    }

    @GetMapping("/download/{applicationId}/{filename}")
    public ResponseEntity<byte[]> downloadDocument (@PathVariable("applicationId") Long applicationId, @PathVariable("filename") String fileName) {
        try {
            byte[] documentContent = documentService.downloadDocument(applicationId, fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            return new ResponseEntity<>(documentContent, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Error: " + e.getMessage()).getBytes());
        }
    }


}
