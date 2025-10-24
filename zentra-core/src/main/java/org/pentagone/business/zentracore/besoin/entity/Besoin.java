package org.pentagone.business.zentracore.besoin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "besoin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Besoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_besoin")
    private Long idBesoin;

    @Column(name = "titre", nullable = false, length = 200)
    private String titre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "departement", length = 100)
    private String departement;

    @Column(name = "nombre_postes", nullable = false)
    private Integer nombrePostes;

    @Column(name = "type_contrat", length = 50)
    private String typeContrat;

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @Column(name = "date_limite")
    private LocalDate dateLimite;

    @Column(name = "statut", nullable = false, length = 50)
    private String statut;

    @Column(name = "priorite", length = 20)
    private String priorite;

    @Column(name = "competences_requises", columnDefinition = "TEXT")
    private String competencesRequises;

    @Column(name = "budget_alloue")
    private Double budgetAlloue;

    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = LocalDate.now();
        }
        if (statut == null) {
            statut = "Nouveau";
        }
        if (nombrePostes == null) {
            nombrePostes = 1;
        }
    }
}
