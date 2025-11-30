package org.pentagone.business.zentracore.hr.ai.document.repository;

import org.pentagone.business.zentracore.hr.ai.document.entity.GeneratedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneratedDocumentRepository extends JpaRepository<GeneratedDocument, Long> {
    List<GeneratedDocument> findByEmployeeId(Integer employeeId);
    List<GeneratedDocument> findByDocumentType(String documentType);
}

