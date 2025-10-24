package org.pentagone.business.zentracore.candidature.repository;

import org.pentagone.business.zentracore.candidature.entity.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    
    List<Candidature> findByStatut(String statut);
    
    List<Candidature> findByCandidatIdCandidat(Long idCandidat);
    
    List<Candidature> findByAnnonceEmploiIdAnnonce(Long idAnnonce);
    
    Optional<Candidature> findByCandidatIdCandidatAndAnnonceEmploiIdAnnonce(Long idCandidat, Long idAnnonce);
}
