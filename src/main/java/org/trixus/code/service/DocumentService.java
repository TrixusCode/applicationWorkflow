package org.trixus.code.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    void uploadDocument (Long applicationId, MultipartFile file, byte[] content, String documentType);

    byte[] downloadDocument(Long applicationId, String fileName);

}
