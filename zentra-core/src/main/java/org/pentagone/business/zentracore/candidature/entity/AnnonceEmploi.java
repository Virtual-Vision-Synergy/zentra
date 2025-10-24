package org.pentagone.business.zentracore.candidature.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "annonce_emploi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnonceEmploi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_annonce")
    private Long idAnnonce;

    @Column(name = "id_poste", nullable = false)
    private Long idPoste;

    @Column(name = "titre_annonce", nullable = false, length = 255)
    private String titreAnnonce;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "type_contrat", nullable = false, length = 50)
    private String typeContrat;

    @Column(name = "lieu_travail", nullable = false, length = 255)
    private String lieuTravail;

    @Column(name = "salaire_min", precision = 10, scale = 2)
    private BigDecimal salaireMin;

    @Column(name = "salaire_max", precision = 10, scale = 2)
    private BigDecimal salaireMax;

    @Column(name = "devise", length = 10)
    private String devise = "EUR";

    @Column(name = "nombre_postes", nullable = false)
    private Integer nombrePostes = 1;

    @Column(name = "date_publication", nullable = false)
    private LocalDate datePublication = LocalDate.now();

    @Column(name = "date_cloture")
    private LocalDate dateCloture;

    @Column(name = "statut", nullable = false, length = 50)
    private String statut = "Ouverte";

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
}
