-- Script d'insertion des connaissances de base pour le Chatbot RH
-- À exécuter : psql -U postgres -d zentra -f insert_chatbot_knowledge.sql

-- Créer la table si elle n'existe pas
CREATE TABLE IF NOT EXISTS ai_chatbot_knowledge (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    keywords TEXT,
    active BOOLEAN DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_knowledge_category ON ai_chatbot_knowledge(category);
CREATE INDEX IF NOT EXISTS idx_knowledge_active ON ai_chatbot_knowledge(active);

-- Nettoyer les anciennes données (optionnel)
TRUNCATE TABLE ai_chatbot_knowledge;

-- ========================================
-- CONNAISSANCES SUR LES CONGÉS
-- ========================================

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Congés',
 'Comment demander un congé ?',
 'Pour demander un congé, suivez ces étapes :
1. Connectez-vous à votre espace employé sur Zentra
2. Accédez au menu "Mes Congés" ou "Demandes de congés"
3. Remplissez le formulaire en indiquant :
   - Le type de congé (congé payé, maladie, etc.)
   - Les dates de début et de fin
   - Le motif si nécessaire
4. Soumettez votre demande
5. Votre supérieur hiérarchique recevra une notification
6. Vous serez notifié de la validation ou du refus

Vous pouvez également envoyer un email à rh@zentra.mg avec les mêmes informations.',
 'congé,conger,demande,vacances,absence,permission',
 true);

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Congés',
 'Combien de jours de congés ai-je droit ?',
 'En Madagascar, selon le Code du Travail :
- Congés payés : 2,5 jours ouvrables par mois de travail effectif
- Soit 30 jours ouvrables par an (environ 5 semaines)

Types de congés supplémentaires :
- Congé de maternité : 14 semaines
- Congé de paternité : 10 jours
- Congés exceptionnels : mariage (3 jours), décès (3-5 jours)

Pour connaître votre solde exact, consultez votre espace employé ou contactez le service RH.',
 'jours,congé,droit,solde,combien,nombre',
 true);

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Congés',
 'Quel est le délai pour demander un congé ?',
 'Les délais recommandés pour demander un congé :
- Congés payés : minimum 2 semaines à l''avance
- Congés exceptionnels : dès que possible
- Congé maladie : sous 48h avec justificatif médical

Pour les périodes de forte activité, il est conseillé de faire votre demande au moins 1 mois à l''avance.

En cas d''urgence (maladie, événement familial), prévenez immédiatement votre responsable.',
 'délai,avance,préavis,quand,demander',
 true);

-- ========================================
-- CONNAISSANCES SUR LA PAIE
-- ========================================

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Paie',
 'Quand est-ce que je reçois mon salaire ?',
 'Le versement des salaires chez Zentra s''effectue :
- Date de paiement : Le 30 de chaque mois
- Mode de paiement : Virement bancaire automatique
- En cas de week-end ou jour férié : Le dernier jour ouvrable précédent

Votre bulletin de paie est disponible :
- Dans votre espace employé (section "Mes bulletins de paie")
- Par email le jour du versement
- Sur demande auprès du service RH

Si votre salaire n''est pas versé à la date prévue, contactez immédiatement le service paie : paie@zentra.mg',
 'salaire,paie,paye,versement,quand,date,recevoir',
 true);

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Paie',
 'Comment obtenir un bulletin de paie ?',
 'Pour obtenir votre bulletin de paie :

En ligne :
1. Connectez-vous à votre espace employé
2. Menu "Documents" > "Bulletins de paie"
3. Sélectionnez le mois souhaité
4. Téléchargez le PDF

Par email :
- Les bulletins sont envoyés automatiquement chaque mois
- Vérifiez votre dossier spam/courrier indésirable

Copie papier :
- Demande au service RH : rh@zentra.mg
- Disponible sous 48h

Historique disponible : Tous vos bulletins depuis votre date d''embauche.',
 'bulletin,paie,fiche,salaire,obtenir,télécharger,document',
 true);

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Paie',
 'Quelles sont les cotisations sur mon salaire ?',
 'Les cotisations sociales à Madagascar :

CNaPS (Retraite) :
- Part employé : 1% du salaire brut
- Part employeur : 13% du salaire brut

OSTIE (Santé) :
- Part employé : 1% du salaire brut
- Part employeur : 5% du salaire brut

IRSA (Impôts) :
- Barème progressif selon le salaire
- 0% jusqu''à 350 000 Ar
- 5% de 350 001 à 400 000 Ar
- 10% de 400 001 à 500 000 Ar
- 15% de 500 001 à 600 000 Ar
- 20% au-delà de 600 000 Ar

Le détail de vos cotisations apparaît sur votre bulletin de paie.',
 'cotisations,retenues,cnaps,ostie,irsa,impot,charges',
 true);

-- ========================================
-- CONNAISSANCES SUR LES HORAIRES
-- ========================================

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Horaires',
 'Comment pointer mes heures ?',
 'Pour pointer vos heures chez Zentra :

Méthode en ligne (recommandée) :
1. Connectez-vous à votre espace employé
2. Menu "Pointage" ou "Mes heures"
3. Cliquez sur "Pointer l''arrivée" le matin
4. Cliquez sur "Pointer le départ" le soir

Méthode physique :
- Badge ou empreinte digitale au terminal de pointage
- Situé à l''entrée principale

À faire :
- ✅ Pointer chaque jour (arrivée et départ)
- ✅ Pointer aux pauses si requis
- ✅ Signaler les oublis à votre responsable

En cas d''oubli :
- Informez votre manager dans les 24h
- Régularisation possible via une demande RH',
 'pointer,heures,pointage,badger,présence,arrivée,départ',
 true);

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Horaires',
 'Quels sont les horaires de travail ?',
 'Les horaires de travail standard chez Zentra :

Horaires normaux :
- Matin : 08h00 - 12h00
- Après-midi : 13h00 - 17h00
- Total : 40 heures par semaine

Pause déjeuner :
- Durée : 1 heure (12h00 - 13h00)
- Non rémunérée

Jours travaillés :
- Du lundi au vendredi
- Week-end : repos

Horaires flexibles :
- Possibles selon les postes
- À valider avec votre responsable

Heures supplémentaires :
- Sur demande du responsable
- Majorées selon la loi (+30% en semaine, +50% week-end)',
 'horaires,heures,travail,début,fin,8h,17h,emploi du temps',
 true);

-- ========================================
-- CONNAISSANCES SUR LES DOCUMENTS RH
-- ========================================

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Documents',
 'Comment obtenir une attestation de travail ?',
 'Pour obtenir une attestation de travail :

En ligne :
1. Connectez-vous à votre espace employé
2. Menu "Documents" > "Demander un document"
3. Sélectionnez "Attestation de travail"
4. Téléchargement immédiat du PDF

Par email :
- Envoyez une demande à : rh@zentra.mg
- Objet : "Demande d''attestation de travail"
- Indiquez : Nom, Prénom, Matricule
- Délai : 48 heures ouvrées

L''attestation contient :
- Vos informations personnelles
- Votre poste actuel
- Votre date d''embauche
- Signature et cachet de l''entreprise

Gratuit et illimité.',
 'attestation,travail,document,obtenir,demander,certificat',
 true);

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Documents',
 'Quels documents puis-je obtenir ?',
 'Documents RH disponibles sur Zentra :

Documents automatiques :
✅ Bulletins de paie (tous les mois)
✅ Contrat de travail (à l''embauche)
✅ Attestation de travail (sur demande)
✅ Certificat de travail (fin de contrat)

Documents sur demande :
✅ Attestation de salaire
✅ Relevé de carrière
✅ Attestation pour prêt bancaire
✅ Copie de contrat

Délais :
- Documents automatiques : Immédiat
- Documents sur demande : 2-5 jours ouvrés

Demande :
- En ligne via votre espace employé
- Par email : rh@zentra.mg
- Au bureau RH (sur rendez-vous)',
 'documents,attestation,certificat,contrat,obtenir,demander',
 true);

-- ========================================
-- CONNAISSANCES SUR LE CONTACT RH
-- ========================================

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Contact',
 'Comment contacter le service RH ?',
 'Contacts du service Ressources Humaines Zentra :

📧 Email principal : rh@zentra.mg
📧 Service paie : paie@zentra.mg
📧 Recrutement : recrutement@zentra.mg

📞 Téléphone : +261 20 22 XXX XX
📱 WhatsApp : +261 34 XX XXX XX

🏢 Bureau RH :
- Adresse : Lot II M 34 Ampefiloha, Antananarivo
- Horaires : Lundi - Vendredi, 09h00 - 16h00
- Sur rendez-vous de préférence

💬 Chatbot (moi !) :
- Disponible 24/7
- Réponses instantanées aux questions courantes

🖥️ Espace employé :
- Accès à vos documents
- Suivi de vos demandes
- Messagerie interne avec la RH',
 'contact,contacter,téléphone,email,joindre,appeler,rh',
 true);

-- ========================================
-- CONNAISSANCES GÉNÉRALES
-- ========================================

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Général',
 'Que peut faire ce chatbot ?',
 'Bonjour ! Je suis l''Assistant RH de Zentra 🤖

Je peux vous aider avec :

📅 Congés et absences
- Comment demander un congé
- Vérifier votre solde de congés
- Délais et procédures

💰 Paie et salaire
- Dates de versement
- Obtenir vos bulletins de paie
- Comprendre vos cotisations

⏰ Horaires et pointage
- Horaires de travail
- Pointer vos heures
- Heures supplémentaires

📄 Documents RH
- Attestations
- Certificats
- Copies de documents

❓ Questions fréquentes
- Informations générales
- Procédures RH
- Contacts

Posez-moi vos questions, je suis là pour vous aider !',
 'aide,bonjour,hello,salut,assistant,chatbot,que peux-tu faire',
 true);

INSERT INTO ai_chatbot_knowledge (category, question, answer, keywords, active) VALUES
('Général',
 'Qui est Zentra ?',
 'Zentra - Solutions RH & Gestion d''Entreprise

🏢 Présentation :
Zentra est une plateforme moderne de gestion des ressources humaines et de gestion d''entreprise basée à Madagascar.

🎯 Nos services :
- Gestion des employés et contrats
- Paie et cotisations sociales
- Recrutement et candidatures
- Pointage et présences
- Évaluations de performance
- Intelligence artificielle RH
- Chatbot RH (c''est moi !)

📍 Localisation :
Lot II M 34 Ampefiloha
Antananarivo 101, Madagascar

📞 Contact :
Email : contact@zentra.mg
Tél : +261 20 22 XXX XX

Notre mission : Simplifier la gestion RH pour les entreprises malgaches.',
 'zentra,entreprise,qui,société,présentation,about',
 true);

-- ========================================
-- VÉRIFICATION
-- ========================================

-- Compter les connaissances insérées
SELECT category as "Catégorie", COUNT(*) as "Nombre de connaissances"
FROM ai_chatbot_knowledge
WHERE active = true
GROUP BY category
ORDER BY category;

-- Total
SELECT COUNT(*) as "Total de connaissances actives"
FROM ai_chatbot_knowledge
WHERE active = true;

-- Afficher toutes les questions
SELECT
    category as "Catégorie",
    question as "Question",
    LEFT(answer, 50) || '...' as "Début de réponse"
FROM ai_chatbot_knowledge
WHERE active = true
ORDER BY category, question;

SELECT '✅ Base de connaissances du chatbot chargée avec succès !' as "Status";
