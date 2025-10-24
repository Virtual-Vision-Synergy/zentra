-- Table: department
-- Français: Département. Contient les départements de l'entreprise.
CREATE TABLE department
(
    department_id SERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL UNIQUE,
    description   TEXT,
    annual_budget DECIMAL(15, 2) CHECK (annual_budget >= 0),
    created_at    DATE         NOT NULL DEFAULT CURRENT_DATE,
    updated_at    DATE         NOT NULL DEFAULT CURRENT_DATE
);

-- Table: job
-- Français: Poste. Définit les postes et leurs contraintes salariales.
CREATE TABLE job
(
    job_id          SERIAL PRIMARY KEY,
    title           VARCHAR(100) NOT NULL,
    description     TEXT,
    required_degree TEXT, -- String -> List<String>
    required_skills TEXT, -- String -> List<String>
    department_id   INTEGER      NOT NULL REFERENCES department (department_id),
    created_date    DATE         NOT NULL DEFAULT CURRENT_DATE,
    updated_at      DATE         NOT NULL DEFAULT CURRENT_DATE
);

-- Table: employee
-- Français: Employé. Informations personnelles et professionnelles de l'employé.
CREATE TABLE employee
(
    employee_id       SERIAL PRIMARY KEY,
    employee_number   VARCHAR(50)    NOT NULL UNIQUE,            -- E: existence
    last_name         VARCHAR(100)   NOT NULL,
    first_name        VARCHAR(100)   NOT NULL,
    work_email        VARCHAR(150)   NOT NULL UNIQUE,
    work_phone        VARCHAR(20),
    birth_date        DATE           NOT NULL,
    gender            CHAR(1) CHECK (gender IN ('M', 'F', 'O')), -- O = Other (Autre)
    address           TEXT,
    city              VARCHAR(100),
    country           VARCHAR(100)   NOT NULL DEFAULT 'France',

    -- Professional information
    hire_date         DATE           NOT NULL,                   -- M: mesure
    base_salary       DECIMAL(10, 2) NOT NULL,
    contract_end_date DATE,                                      -- For fixed-term contracts

    candidate_id      INTEGER REFERENCES candidate (candidate_id),
    job_id            INTEGER        NOT NULL REFERENCES job (job_id),

    -- Metadata
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: contract
-- Français: Contrat de travail. Détails des contrats, périodes d'essai et conditions.
CREATE TABLE employment_contract
(
    contract_id       SERIAL PRIMARY KEY,
    employee_id       INTEGER       NOT NULL REFERENCES employee (employee_id),
    contract_number   VARCHAR(100)  NOT NULL UNIQUE, -- E: existence
    start_date        DATE          NOT NULL,
    end_date          DATE,

    -- Contract terms
    gross_salary      DECIMAL(10, 2),
    annual_bonus      DECIMAL(10, 2),
    benefits          TEXT,
    weekly_hours      DECIMAL(5, 2) NOT NULL,
    annual_leave_days INTEGER       NOT NULL DEFAULT 25,

    -- Status
    signature_date    DATE,
    contract_file     VARCHAR(255),
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: publication
-- Français: Annonce d'emploi. Publie les offres liées à un poste.
CREATE TABLE publication
(
    publication_id      SERIAL PRIMARY KEY,
    title               VARCHAR(200) NOT NULL,
    description         TEXT         NOT NULL,
    published_date      DATE         NOT NULL DEFAULT CURRENT_DATE,
    closing_date        DATE,
    number_of_positions INTEGER      NOT NULL DEFAULT 1,
    status              VARCHAR(50)  NOT NULL DEFAULT 'Open' CHECK (status IN ('Open', 'Closed', 'Suspended', 'Filled')),
    job_id              INTEGER      NOT NULL REFERENCES job (job_id),
    created_date        DATE         NOT NULL DEFAULT CURRENT_DATE,
    updated_at          DATE         NOT NULL DEFAULT CURRENT_DATE
);

-- Table: candidate
-- Français: Candidat. Stocke les candidats et leurs métadonnées.
CREATE TABLE candidate
(
    candidate_id             SERIAL PRIMARY KEY,
    last_name                VARCHAR(100) NOT NULL,
    first_name               VARCHAR(100) NOT NULL,
    email                    VARCHAR(150) NOT NULL UNIQUE,
    phone                    VARCHAR(20),
    birth_date               DATE         NOT NULL,
    address                  TEXT,
    city                     VARCHAR(100),
    country                  VARCHAR(100) NOT NULL DEFAULT 'France',
    education_level          VARCHAR(100),
    last_degree              VARCHAR(150),
    years_experience         INTEGER      NOT NULL DEFAULT 0,
    skills                   TEXT, -- String -> List<String>
    cv_file                  VARCHAR(255),
    motivational_letter_file VARCHAR(255),
    created_date             DATE         NOT NULL DEFAULT CURRENT_DATE,
    updated_at               DATE         NOT NULL DEFAULT CURRENT_DATE
);

-- Table: application
-- Français: Candidature. Lie un candidat à une annonce.
CREATE TABLE application
(
    application_id SERIAL PRIMARY KEY,
    applied_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status         VARCHAR(50) NOT NULL DEFAULT 'Received' CHECK (status IN
                                                                  ('Received', 'Under Review', 'Shortlisted',
                                                                   'Accepted', 'Rejected', 'Pending')),
    document_score DECIMAL(5, 2),
    score          DECIMAL(5, 2),
    comment        TEXT,
    candidate_id   INTEGER     NOT NULL REFERENCES candidate (candidate_id),
    publication_id INTEGER     NOT NULL REFERENCES publication (publication_id),
    created_date   DATE        NOT NULL DEFAULT CURRENT_DATE,
    updated_at     DATE        NOT NULL DEFAULT CURRENT_DATE
);

-- Table: qcm
-- Français: Test et évaluation. Tests administrés aux candidats.
CREATE TABLE qcm
(
    qcm_id           SERIAL PRIMARY KEY,
    title            VARCHAR(150) NOT NULL,
    description      TEXT,
    date             DATE         NOT NULL,
    duration_minutes INTEGER,
    total_score      DECIMAL(5, 2), -- V: valeurs
    required_score   DECIMAL(5, 2), -- V: valeurs
    created_date     DATE         NOT NULL DEFAULT CURRENT_DATE,
    updated_at       DATE         NOT NULL DEFAULT CURRENT_DATE
);

-- Table: question
-- Français: Question d un qcm
CREATE TABLE question
(
    question_id  SERIAL PRIMARY KEY,
    libelle      VARCHAR(150) NOT NULL,
    required     BOOLEAN               DEFAULT TRUE,
    score        DECIMAL(5, 2),
    qcm_id       INTEGER      NOT NULL REFERENCES qcm (qcm_id),
    created_date DATE         NOT NULL DEFAULT CURRENT_DATE,
    updated_at   DATE         NOT NULL DEFAULT CURRENT_DATE
);

-- Table: choice
-- Francais: Choix a une question
CREATE TABLE choice
(
    choice_id    SERIAL PRIMARY KEY,
    libelle      VARCHAR(150) NOT NULL,
    score        DECIMAL(5, 2),
    correct      BOOLEAN               DEFAULT TRUE,
    question_id  INTEGER      NOT NULL REFERENCES question (question_id),
    created_date DATE         NOT NULL DEFAULT CURRENT_DATE,
    updated_at   DATE         NOT NULL DEFAULT CURRENT_DATE
);

-- Table: attempt
-- Francais: Tentative a un qcm par un candidat
CREATE TABLE attempt
(
    attempt_id     SERIAL PRIMARY KEY,
    qcm_id         INTEGER NOT NULL REFERENCES qcm (qcm_id),
    application_id INTEGER NOT NULL REFERENCES application (application_id),
    obtained_score DECIMAL(5, 2),
    created_date   DATE    NOT NULL DEFAULT CURRENT_DATE,
    updated_at     DATE    NOT NULL DEFAULT CURRENT_DATE
);

-- Table: answer
-- Francais: Reponses a une tentative
CREATE TABLE response
(
    response_id  SERIAL PRIMARY KEY,
    attempt_id   INTEGER NOT NULL REFERENCES attempt (attempt_id),
    choice_id    INTEGER NOT NULL REFERENCES choice (choice_id),
    created_date DATE    NOT NULL DEFAULT CURRENT_DATE,
    updated_at   DATE    NOT NULL DEFAULT CURRENT_DATE
);

-- Table: interview
-- Français: Entretien. Programmation et compte-rendu des entretiens.
CREATE TABLE interview
(
    interview_id     SERIAL PRIMARY KEY,
    interview_type   VARCHAR(100) NOT NULL CHECK (interview_type IN
                                                  ('Phone', 'Video', 'In-Person', 'Panel', 'Technical', 'HR')),
    interview_date   DATE         NOT NULL,
    start_time       TIME         NOT NULL,
    end_time         TIME,
    duration_minutes INTEGER, -- R: réalisme - max 4h
    interviewer_id   INTEGER      NOT NULL REFERENCES employee (employee_id),
    report           TEXT,
    score            DECIMAL(5, 2),
    application_id   INTEGER      NOT NULL REFERENCES application (application_id),
    created_date     DATE         NOT NULL DEFAULT CURRENT_DATE,
    updated_at       DATE         NOT NULL DEFAULT CURRENT_DATE
);

