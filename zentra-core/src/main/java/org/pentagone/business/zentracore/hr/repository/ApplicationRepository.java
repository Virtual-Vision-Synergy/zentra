package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateId(Long candidateId);
    List<Application> findByPublicationId(Long publicationId);
    List<Application> findByStatus(String status);
}
