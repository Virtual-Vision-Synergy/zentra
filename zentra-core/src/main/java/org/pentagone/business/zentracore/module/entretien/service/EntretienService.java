package org.pentagone.business.zentracore.module.entretien.service;

import lombok.RequiredArgsConstructor;
import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.pentagone.business.zentracore.module.entretien.entity.Entretien;
import org.pentagone.business.zentracore.module.entretien.repository.EntretienRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EntretienService {
    
    private final EntretienRepository entretienRepository;
    
    public List<Entretien> getAllEntretiens() {
        return entretienRepository.findAll();
    }
    
    public Entretien getEntretienById(Integer id) {
        return entretienRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entretien not found with id: " + id));
    }
    
    public List<Entretien> getEntretiensByCandidature(Integer idCandidature) {
        return entretienRepository.findByIdCandidature(idCandidature);
    }
    
    public List<Entretien> getEntretiensByStatut(String statut) {
        return entretienRepository.findByStatut(statut);
    }
    
    public List<Entretien> getEntretiensByDate(LocalDate date) {
        return entretienRepository.findByDateEntretien(date);
    }
    
    public List<Entretien> getEntretiensByDateRange(LocalDate startDate, LocalDate endDate) {
        return entretienRepository.findByDateEntretienBetween(startDate, endDate);
    }
    
    public List<Entretien> getEntretiensByType(String typeEntretien) {
        return entretienRepository.findByTypeEntretien(typeEntretien);
    }
    
    public Entretien createEntretien(Entretien entretien) {
        validateEntretien(entretien);
        entretien.setDateCreation(LocalDateTime.now());
        return entretienRepository.save(entretien);
    }
    
    public Entretien updateEntretien(Integer id, Entretien entretien) {
        Entretien existingEntretien = getEntretienById(id);
        validateEntretien(entretien);
        
        existingEntretien.setIdCandidature(entretien.getIdCandidature());
        existingEntretien.setTypeEntretien(entretien.getTypeEntretien());
        existingEntretien.setDateEntretien(entretien.getDateEntretien());
        existingEntretien.setHeureDebut(entretien.getHeureDebut());
        existingEntretien.setHeureFin(entretien.getHeureFin());
        existingEntretien.setDureeMinutes(entretien.getDureeMinutes());
        existingEntretien.setLieu(entretien.getLieu());
        existingEntretien.setIntervieweurs(entretien.getIntervieweurs());
        existingEntretien.setStatut(entretien.getStatut());
        existingEntretien.setCompteRendu(entretien.getCompteRendu());
        
        return entretienRepository.save(existingEntretien);
    }
    
    public void deleteEntretien(Integer id) {
        Entretien entretien = getEntretienById(id);
        entretienRepository.delete(entretien);
    }
    
    private void validateEntretien(Entretien entretien) {
        if (entretien.getIdCandidature() == null) {
            throw new IllegalArgumentException("ID candidature is required");
        }
        if (entretien.getTypeEntretien() == null || entretien.getTypeEntretien().trim().isEmpty()) {
            throw new IllegalArgumentException("Type entretien is required");
        }
        if (entretien.getDateEntretien() == null) {
            throw new IllegalArgumentException("Date entretien is required");
        }
        if (entretien.getHeureDebut() == null) {
            throw new IllegalArgumentException("Heure début is required");
        }
        if (entretien.getIntervieweurs() == null || entretien.getIntervieweurs().trim().isEmpty()) {
            throw new IllegalArgumentException("Intervieweurs are required");
        }
        if (entretien.getHeureFin() != null && entretien.getHeureFin().isBefore(entretien.getHeureDebut())) {
            throw new IllegalArgumentException("Heure fin must be after heure début");
        }
        if (entretien.getDureeMinutes() != null && (entretien.getDureeMinutes() <= 0 || entretien.getDureeMinutes() > 240)) {
            throw new IllegalArgumentException("Durée minutes must be between 1 and 240");
        }
    }
}
