-- AI Module Tables
-- Tables pour les modules d'automatisation et d'intelligence artificielle

-- Table pour les messages du chatbot
CREATE TABLE IF NOT EXISTS ai_chatbot_messages (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    response TEXT NOT NULL,
    category VARCHAR(50),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id INTEGER,
    CONSTRAINT fk_chatbot_user FOREIGN KEY (user_id) REFERENCES employee(id) ON DELETE SET NULL
);

CREATE INDEX idx_chatbot_session ON ai_chatbot_messages(session_id);
CREATE INDEX idx_chatbot_user ON ai_chatbot_messages(user_id);
CREATE INDEX idx_chatbot_timestamp ON ai_chatbot_messages(timestamp DESC);

-- Table pour la base de connaissances du chatbot
CREATE TABLE IF NOT EXISTS ai_chatbot_knowledge (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    keywords TEXT,
    active BOOLEAN DEFAULT TRUE
);

CREATE INDEX idx_knowledge_category ON ai_chatbot_knowledge(category);
CREATE INDEX idx_knowledge_active ON ai_chatbot_knowledge(active);

-- Table pour les documents générés
CREATE TABLE IF NOT EXISTS ai_generated_documents (
    id BIGSERIAL PRIMARY KEY,
    document_type VARCHAR(50) NOT NULL,
    employee_id INTEGER NOT NULL,
    employee_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    generated_by INTEGER,
    CONSTRAINT fk_generated_doc_employee FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    CONSTRAINT fk_generated_doc_by FOREIGN KEY (generated_by) REFERENCES employee(id) ON DELETE SET NULL
);

CREATE INDEX idx_generated_doc_employee ON ai_generated_documents(employee_id);
CREATE INDEX idx_generated_doc_type ON ai_generated_documents(document_type);
CREATE INDEX idx_generated_doc_date ON ai_generated_documents(generated_at DESC);

-- Table pour les prédictions de turnover
CREATE TABLE IF NOT EXISTS ai_turnover_predictions (
    id BIGSERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL,
    employee_name VARCHAR(255) NOT NULL,
    risk_score DOUBLE PRECISION NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    reasons TEXT,
    predicted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_turnover_employee FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    CONSTRAINT chk_risk_score CHECK (risk_score >= 0 AND risk_score <= 1),
    CONSTRAINT chk_risk_level CHECK (risk_level IN ('LOW', 'MEDIUM', 'HIGH'))
);

CREATE INDEX idx_turnover_employee ON ai_turnover_predictions(employee_id);
CREATE INDEX idx_turnover_risk_level ON ai_turnover_predictions(risk_level);
CREATE INDEX idx_turnover_risk_score ON ai_turnover_predictions(risk_score DESC);
CREATE INDEX idx_turnover_date ON ai_turnover_predictions(predicted_at DESC);

-- Table pour la détection d'anomalies
CREATE TABLE IF NOT EXISTS ai_anomaly_detections (
    id BIGSERIAL PRIMARY KEY,
    anomaly_type VARCHAR(50) NOT NULL,
    employee_id INTEGER NOT NULL,
    employee_name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    severity VARCHAR(20) NOT NULL,
    detected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved BOOLEAN DEFAULT FALSE,
    data_reference VARCHAR(255),
    CONSTRAINT fk_anomaly_employee FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    CONSTRAINT chk_anomaly_type CHECK (anomaly_type IN ('ATTENDANCE', 'PAYROLL', 'HOURS', 'OTHER')),
    CONSTRAINT chk_severity CHECK (severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'))
);

CREATE INDEX idx_anomaly_employee ON ai_anomaly_detections(employee_id);
CREATE INDEX idx_anomaly_type ON ai_anomaly_detections(anomaly_type);
CREATE INDEX idx_anomaly_severity ON ai_anomaly_detections(severity);
CREATE INDEX idx_anomaly_resolved ON ai_anomaly_detections(resolved);
CREATE INDEX idx_anomaly_date ON ai_anomaly_detections(detected_at DESC);

-- Table pour les recommandations de candidats
CREATE TABLE IF NOT EXISTS ai_candidate_recommendations (
    id BIGSERIAL PRIMARY KEY,
    candidate_id INTEGER NOT NULL,
    candidate_name VARCHAR(255) NOT NULL,
    job_id INTEGER NOT NULL,
    job_title VARCHAR(255) NOT NULL,
    match_score DOUBLE PRECISION NOT NULL,
    match_details TEXT,
    calculated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recommendation_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id) ON DELETE CASCADE,
    CONSTRAINT fk_recommendation_job FOREIGN KEY (job_id) REFERENCES publication(id) ON DELETE CASCADE,
    CONSTRAINT chk_match_score CHECK (match_score >= 0 AND match_score <= 1)
);

CREATE INDEX idx_recommendation_candidate ON ai_candidate_recommendations(candidate_id);
CREATE INDEX idx_recommendation_job ON ai_candidate_recommendations(job_id);
CREATE INDEX idx_recommendation_score ON ai_candidate_recommendations(match_score DESC);
CREATE INDEX idx_recommendation_date ON ai_candidate_recommendations(calculated_at DESC);

-- Insertion de données initiales pour la base de connaissances du chatbot
INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords) VALUES
('LEAVE', 'Comment demander un congé ?',
 'Pour demander un congé, vous devez : 1) Vous connecter à votre espace personnel, 2) Aller dans la section "Congés", 3) Remplir le formulaire de demande en spécifiant les dates et le type de congé, 4) Soumettre la demande. Votre manager recevra une notification et pourra approuver ou rejeter la demande.',
 'congé, vacances, demande, comment, formulaire'),

('LEAVE', 'Combien de jours de congé ai-je ?',
 'Le nombre de jours de congé dépend de votre contrat. En général, vous avez droit à 2.5 jours ouvrables par mois travaillé, soit 30 jours par an. Vous pouvez consulter votre solde de congés dans votre espace personnel.',
 'jours, congé, solde, nombre, combien'),

('PAYROLL', 'Quand est-ce que je reçois mon salaire ?',
 'Les salaires sont versés le dernier jour ouvrable de chaque mois. Votre bulletin de paie est disponible dans votre espace personnel quelques jours avant le versement.',
 'salaire, paie, quand, versement, date'),

('PAYROLL', 'Comment obtenir mon bulletin de paie ?',
 'Vos bulletins de paie sont disponibles dans votre espace personnel, section "Documents RH". Vous pouvez les télécharger et les imprimer à tout moment.',
 'bulletin, paie, fiche, télécharger, obtenir'),

('ATTENDANCE', 'Comment pointer mes heures ?',
 'Vous devez pointer votre arrivée et votre départ chaque jour via le système de pointage. Vous pouvez le faire via l''application mobile ou le terminal dans les locaux.',
 'pointage, heures, pointer, présence, arrivée, départ'),

('ATTENDANCE', 'Que faire si j''oublie de pointer ?',
 'Si vous oubliez de pointer, contactez votre manager ou le service RH dans les 24 heures pour régulariser votre pointage.',
 'oubli, pointer, manque, rectifier'),

('CONTRACT', 'Comment obtenir une attestation de travail ?',
 'Vous pouvez demander une attestation de travail via votre espace personnel ou en contactant le service RH. Le document sera généré et disponible sous 48 heures.',
 'attestation, travail, certificat, document'),

('GENERAL', 'Comment contacter le service RH ?',
 'Vous pouvez contacter le service RH par email à rh@zentra.com ou par téléphone au 01 XX XX XX XX. Les horaires d''ouverture sont du lundi au vendredi de 9h à 17h.',
 'contact, RH, email, téléphone, joindre');

-- Commentaires sur les tables
COMMENT ON TABLE ai_chatbot_messages IS 'Historique des conversations avec le chatbot RH';
COMMENT ON TABLE ai_chatbot_knowledge IS 'Base de connaissances pour les réponses automatiques du chatbot';
COMMENT ON TABLE ai_generated_documents IS 'Documents RH générés automatiquement (contrats, attestations, etc.)';
COMMENT ON TABLE ai_turnover_predictions IS 'Prédictions de risque de départ des employés';
COMMENT ON TABLE ai_anomaly_detections IS 'Détection d''anomalies dans les données RH (heures, paie, présence)';
COMMENT ON TABLE ai_candidate_recommendations IS 'Scores de correspondance entre candidats et postes';

