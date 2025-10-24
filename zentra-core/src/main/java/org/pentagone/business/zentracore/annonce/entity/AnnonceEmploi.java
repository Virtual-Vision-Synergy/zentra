package org.pentagone.business.zentracore.annonce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "annonce_emploi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnonceEmploi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_annonce")
    private Integer idAnnonce;

    @Column(name = "id_poste", nullable = false)
    private Integer idPoste;

    @Column(name = "titre_annonce", nullable = false, length = 200)
    private String titreAnnonce;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "type_contrat", length = 50)
    private String typeContrat;

    @Column(name = "lieu_travail", length = 100)
    private String lieuTravail;

    @Column(name = "date_publication", nullable = false)
    private LocalDate datePublication;

    @Column(name = "date_cloture")
    private LocalDate dateCloture;

    @Column(name = "nombre_postes", nullable = false)
    private Integer nombrePostes = 1;

    @Column(name = "statut", nullable = false, length = 50)
    private String statut = "Ouverte";

    @Column(name = "canaux_publication", columnDefinition = "TEXT")
    private String canauxPublication;

    @PrePersist
    protected void onCreate() {
        if (datePublication == null) {
            datePublication = LocalDate.now();
        }
        if (statut == null) {
            statut = "Ouverte";
        }
        if (nombrePostes == null) {
            nombrePostes = 1;
        }
    }
}
