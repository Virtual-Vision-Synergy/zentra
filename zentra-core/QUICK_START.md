# 🚀 Guide de Démarrage Rapide - Module Besoins en Personnel

## Prérequis

- ✅ Java 17+
- ✅ Maven 3.6+
- ✅ PostgreSQL 13+
- ✅ IDE (IntelliJ IDEA / Eclipse / VS Code)

---

## 📋 Étapes d'installation

### 1. Configuration de la base de données

```sql
-- Créer la base de données
CREATE DATABASE zentra;

-- Se connecter à la base
\c zentra

-- Exécuter le script SQL
\i src/main/resources/sql/table_rh.sql
```

### 2. Configuration de l'application

Vérifier le fichier `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/zentra
spring.datasource.username=postgres
spring.datasource.password=votre_mot_de_passe
server.port=8080
server.servlet.context-path=/api
```

### 3. Compilation du projet

```bash
cd zentra-core
./mvnw clean install
```

### 4. Démarrage de l'application

```bash
./mvnw spring-boot:run
```

L'application démarre sur : `http://localhost:8080/api`

---

## 🧪 Tests rapides

### Test 1 : Vérifier que l'API est en ligne

```bash
curl http://localhost:8080/api/staffing-needs
```

**Résultat attendu** : `[]` (liste vide) ou liste de besoins

### Test 2 : Créer des données de test

#### a. Créer un département (si nécessaire)
```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Département IT",
    "description": "Technologies de l'information",
    "annualBudget": 500000.00
  }'
```

#### b. Créer un poste (si nécessaire)
```bash
curl -X POST http://localhost:8080/api/jobs \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Développeur Backend",
    "description": "Développeur Spring Boot",
    "requiredDegree": "Bac+5",
    "requiredSkills": "Java, Spring Boot, PostgreSQL",
    "departmentId": 1
  }'
```

#### c. Créer un besoin en personnel
```bash
curl -X POST http://localhost:8080/api/staffing-needs \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Développeur Backend Senior",
    "description": "Besoin urgent",
    "numberOfPositions": 2,
    "priority": "High",
    "status": "Open",
    "departmentId": 1,
    "jobId": 1
  }'
```

### Test 3 : Lire les besoins

```bash
# Tous les besoins
curl http://localhost:8080/api/staffing-needs

# Un besoin spécifique
curl http://localhost:8080/api/staffing-needs/1

# Par statut
curl http://localhost:8080/api/staffing-needs/status/Open

# Par priorité
curl http://localhost:8080/api/staffing-needs/priority/High
```

### Test 4 : Mettre à jour un besoin

```bash
curl -X PUT http://localhost:8080/api/staffing-needs/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Développeur Backend Senior",
    "description": "Besoin urgent - Mis à jour",
    "numberOfPositions": 3,
    "priority": "High",
    "status": "In Progress",
    "departmentId": 1,
    "jobId": 1
  }'
```

### Test 5 : Supprimer un besoin

```bash
curl -X DELETE http://localhost:8080/api/staffing-needs/1
```

---

## 📦 Import Postman

1. Ouvrir Postman
2. Cliquer sur "Import"
3. Sélectionner le fichier `Zentra_Staffing_Needs_API.postman_collection.json`
4. La collection contient 14 requêtes prêtes à l'emploi

---

## 🔍 Vérification des logs

Les logs affichent :
- Les requêtes SQL générées
- Les erreurs de validation
- Les exceptions

```bash
# Suivre les logs en temps réel
tail -f logs/zentra-core.log
```

---

## 🐛 Résolution de problèmes

### Problème : La base de données ne se connecte pas

**Solution** :
```bash
# Vérifier que PostgreSQL est démarré
sudo systemctl status postgresql

# Vérifier les credentials dans application.properties
```

### Problème : Port 8080 déjà utilisé

**Solution** :
Modifier dans `application.properties` :
```properties
server.port=8081
```

### Problème : Erreur de compilation MapStruct

**Solution** :
```bash
./mvnw clean install -U
```

### Problème : Foreign key constraint error

**Solution** :
Vérifier que les IDs de département et poste existent dans la base

```sql
SELECT * FROM department;
SELECT * FROM job;
```

---

## 📊 Données de test

### Script SQL pour insérer des données de test

```sql
-- Insérer un département
INSERT INTO department (name, description, annual_budget, created_at, updated_at)
VALUES ('IT Department', 'Technologies de l''information', 500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insérer un poste
INSERT INTO job (title, description, required_degree, required_skills, department_id, created_date, updated_at)
VALUES ('Développeur Backend', 'Développeur Spring Boot Senior', 'Master', 'Java, Spring, PostgreSQL', 1, CURRENT_DATE, CURRENT_DATE);

-- Insérer un besoin en personnel
INSERT INTO staffing_need (title, description, number_of_positions, priority, status, 
                           required_start_date, budget_allocated, justification,
                           department_id, job_id, created_at, updated_at)
VALUES ('Développeur Backend Senior', 
        'Besoin urgent de développeur avec expertise Spring Boot',
        2, 'High', 'Open',
        '2025-11-15', 120000.00,
        'Extension équipe technique',
        1, 1,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

---

## 📚 Ressources

| Ressource | Emplacement |
|-----------|-------------|
| Documentation API | `STAFFING_NEED_API.md` |
| Documentation Module | `STAFFING_NEED_MODULE.md` |
| Résumé complet | `STAFFING_NEED_SUMMARY.md` |
| Collection Postman | `Zentra_Staffing_Needs_API.postman_collection.json` |
| Script SQL | `src/main/resources/sql/table_rh.sql` |

---

## ✅ Checklist de vérification

- [ ] PostgreSQL installé et démarré
- [ ] Base de données `zentra` créée
- [ ] Tables créées (script SQL exécuté)
- [ ] Données de test insérées
- [ ] application.properties configuré
- [ ] Projet compilé sans erreur
- [ ] Application démarrée
- [ ] Test GET /staffing-needs réussi
- [ ] Test POST création réussi
- [ ] Collection Postman importée

---

## 🎯 Prochaines étapes

Une fois l'API fonctionnelle :

1. **Frontend** : Créer les écrans dans zentra-ui
2. **Sécurité** : Ajouter Spring Security
3. **Tests** : Lancer les tests unitaires
4. **Documentation** : Ajouter Swagger
5. **CI/CD** : Configurer le pipeline

---

## 💡 Conseils

- Utiliser Postman pour tester rapidement
- Consulter les logs pour débugger
- Vérifier les contraintes FK avant insertion
- Sauvegarder régulièrement la base de données

---

## 📞 Support

En cas de problème :
1. Vérifier les logs de l'application
2. Consulter la documentation
3. Vérifier la configuration de la base de données
4. Tester avec curl ou Postman

---

**🎉 Bon développement avec Zentra !**
