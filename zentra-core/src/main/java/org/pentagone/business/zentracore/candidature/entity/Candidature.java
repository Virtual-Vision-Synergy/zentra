package org.pentagone.business.zentracore.candidature.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidature", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_candidat", "id_annonce"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidature")
    private Long idCandidature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_candidat", nullable = false)
    private Candidat candidat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_annonce", nullable = false)
    private AnnonceEmploi annonceEmploi;

    @Column(name = "date_candidature", nullable = false, updatable = false)
    private LocalDateTime dateCandidature = LocalDateTime.now();

    @Column(name = "statut", nullable = false, length = 50)
    private String statut = "Reçue";

    @Column(name = "score_initial", precision = 5, scale = 2)
    private BigDecimal scoreInitial;

    @Column(name = "commentaire_initial", columnDefinition = "TEXT")
    private String commentaireInitial;

    @Column(name = "date_derniere_modification", nullable = false)
    private LocalDateTime dateDerniereModification = LocalDateTime.now();
}
