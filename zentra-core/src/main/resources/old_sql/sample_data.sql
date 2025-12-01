-- Script d'insertion de données d'exemple pour Candidate, Application et Token

-- Insertion d'un candidat
INSERT INTO candidate (
    created_at,
    updated_at,
    last_name,
    first_name,
    email,
    phone,
    birth_date,
    address,
    city,
    country,
    education_level,
    last_degree,
    years_experience,
    skills,
    cv_file,
    motivational_letter_file
) VALUES (
    '2024-01-15 10:30:00',
    '2024-01-15 10:30:00',
    'Dupont',
    'Jean',
    'jean.dupont@email.com',
    '+33 6 12 34 56 78',
    '1990-05-15',
    '123 Rue de la Paix',
    'Paris',
    'France',
    'Master',
    'Master en Informatique',
    5,
    'Java, Spring Boot, Angular, PostgreSQL, Docker',
    'cv_jean_dupont.pdf',
    'lettre_motivation_jean_dupont.pdf'
);

-- Insertion d'une application
INSERT INTO application (
    created_at,
    updated_at,
    applied_at,
    status,
    document_score,
    score,
    comment,
    candidate_id,
    qcm_id,
    publication_id
) VALUES (
    '2024-01-15 11:00:00',
    '2024-01-15 11:00:00',
    '2024-01-15 11:00:00',
    'Received',
    85.5,
    null,
    'Profil intéressant avec une bonne expérience en développement Java',
    1,
    null,
    null
);

-- Insertion d'un token
INSERT INTO token (
    created_at,
    updated_at,
    value,
    active,
    expiration_date,
    application_id
) VALUES (
    '2024-01-15 11:15:00',
    '2024-01-15 11:15:00',
    '123456789abcdef',
    true,
    '2026-01-22 11:15:00',
    1
);
