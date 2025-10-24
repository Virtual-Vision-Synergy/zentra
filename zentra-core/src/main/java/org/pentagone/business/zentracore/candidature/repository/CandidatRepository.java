package org.pentagone.business.zentracore.candidature.repository;

import org.pentagone.business.zentracore.candidature.entity.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    
    Optional<Candidat> findByEmail(String email);
    
    List<Candidat> findByStatut(String statut);
}
