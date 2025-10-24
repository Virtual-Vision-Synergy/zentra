package org.pentagone.business.zentracore.qcm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "test_evaluation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_test")
    private Long idTest;

    @Column(name = "id_candidature", nullable = false)
    private Long idCandidature;

    @Column(name = "type_test", nullable = false, length = 100)
    private String typeTest;

    @Column(name = "nom_test", nullable = false, length = 150)
    private String nomTest;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_test", nullable = false)
    private LocalDate dateTest;

    @Column(name = "heure_debut")
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
    private LocalTime heureFin;

    @Column(name = "duree_minutes")
    private Integer dureeMinutes;

    @Column(name = "score_obtenu", precision = 5, scale = 2)
    private BigDecimal scoreObtenu;

    @Column(name = "score_requis", precision = 5, scale = 2)
    private BigDecimal scoreRequis;

    @Column(name = "reussi")
    private Boolean reussi;

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "evaluateur", length = 150)
    private String evaluateur;
}
