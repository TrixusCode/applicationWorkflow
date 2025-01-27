package org.trixus.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trixus.code.model.Document;

import java.util.Optional;

public interface DocumentRepo extends JpaRepository<Document, Long> {
    Optional<Document> findByApplicationIdAndFileName(Long applicationId, String fileName);
}
