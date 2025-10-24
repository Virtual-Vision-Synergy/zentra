package org.pentagone.business.zentracore.annonce.repository;

import org.pentagone.business.zentracore.annonce.entity.AnnonceEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceEmploiRepository extends JpaRepository<AnnonceEmploi, Integer> {
    List<AnnonceEmploi> findByStatut(String statut);
    List<AnnonceEmploi> findByIdPoste(Integer idPoste);
}
