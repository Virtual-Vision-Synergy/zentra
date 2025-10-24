package org.pentagone.business.zentracore.module.entretien.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "entretien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entretien {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entretien")
    private Integer idEntretien;
    
    @Column(name = "id_candidature", nullable = false)
    private Integer idCandidature;
    
    @Column(name = "type_entretien", nullable = false, length = 100)
    private String typeEntretien;
    
    @Column(name = "date_entretien", nullable = false)
    private LocalDate dateEntretien;
    
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;
    
    @Column(name = "heure_fin")
    private LocalTime heureFin;
    
    @Column(name = "duree_minutes")
    private Integer dureeMinutes;
    
    @Column(name = "lieu", length = 200)
    private String lieu;
    
    @Column(name = "intervieweurs", nullable = false, columnDefinition = "TEXT")
    private String intervieweurs;
    
    @Column(name = "statut", nullable = false, length = 50)
    private String statut = "Planifié";
    
    @Column(name = "compte_rendu", columnDefinition = "TEXT")
    private String compteRendu;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
}
