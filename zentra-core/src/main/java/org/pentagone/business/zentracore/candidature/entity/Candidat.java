package org.pentagone.business.zentracore.candidature.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidat")
    private Long idCandidat;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "adresse", columnDefinition = "TEXT")
    private String adresse;

    @Column(name = "ville", length = 100)
    private String ville;

    @Column(name = "code_postal", length = 20)
    private String codePostal;

    @Column(name = "pays", length = 100)
    private String pays;

    @Column(name = "niveau_etude", length = 150)
    private String niveauEtude;

    @Column(name = "diplome_obtenu", length = 200)
    private String diplomeObtenu;

    @Column(name = "annee_diplome")
    private Integer anneeDiplome;

    @Column(name = "annees_experience", nullable = false)
    private Integer anneesExperience;

    @Column(name = "domaine_experience", columnDefinition = "TEXT")
    private String domaineExperience;

    @Column(name = "cv_fichier", nullable = false, length = 255)
    private String cvFichier;

    @Column(name = "lettre_motivation_fichier", length = 255)
    private String lettreMotivatonFichier;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    @Column(name = "portfolio_url", length = 255)
    private String portfolioUrl;

    @Column(name = "statut", nullable = false, length = 50)
    private String statut = "Actif";

    @Column(name = "date_inscription", nullable = false, updatable = false)
    private LocalDateTime dateInscription = LocalDateTime.now();
}
