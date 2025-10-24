package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByJobId(Long jobId);
    List<Publication> findByStatus(String status);
    List<Publication> findByTitleContainingIgnoreCase(String title);
}
