package org.pentagone.business.zentracore.candidature.repository;

import org.pentagone.business.zentracore.candidature.entity.AnnonceEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceEmploiRepository extends JpaRepository<AnnonceEmploi, Long> {
    
    List<AnnonceEmploi> findByStatut(String statut);
    
    List<AnnonceEmploi> findByIdPoste(Long idPoste);
}
