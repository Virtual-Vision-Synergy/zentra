package org.pentagone.business.zentracore.module.entretien.repository;

import org.pentagone.business.zentracore.module.entretien.entity.Entretien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntretienRepository extends JpaRepository<Entretien, Integer> {
    
    List<Entretien> findByIdCandidature(Integer idCandidature);
    
    List<Entretien> findByStatut(String statut);
    
    List<Entretien> findByDateEntretien(LocalDate dateEntretien);
    
    List<Entretien> findByDateEntretienBetween(LocalDate startDate, LocalDate endDate);
    
    List<Entretien> findByTypeEntretien(String typeEntretien);
}
