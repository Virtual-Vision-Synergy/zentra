# 🚀 Démarrage Rapide - Zentra

## ✅ État Actuel
- ✅ Backend démarré sur http://localhost:8080
- ✅ Frontend démarré sur http://localhost:5173
- ⚠️ **Base de données vide** - Il faut charger les données de test

---

## 📊 Charger les données de test

### Méthode 1 : Avec psql (Recommandé)

```powershell
# Ouvrir un nouveau terminal PowerShell
cd "d:\S5\Mr tovo\zentra\zentra-core"

# Se connecter à PostgreSQL et exécuter le script
psql -U postgres -d zentra -f "src\main\resources\sql\test_data_staffing_needs.sql"
```

### Méthode 2 : Avec pgAdmin ou DBeaver

1. Ouvrir pgAdmin ou DBeaver
2. Se connecter à la base `zentra`
3. Ouvrir le fichier : `zentra-core\src\main\resources\sql\test_data_staffing_needs.sql`
4. Exécuter le script SQL

---

## 🔍 Vérifier que tout fonctionne

### 1. Vérifier l'API

```powershell
# Dans PowerShell
curl http://localhost:8080/api/staffing-needs
```

**Résultat attendu** : Vous devriez voir 6 besoins en JSON

### 2. Rafraîchir le Frontend

1. Aller sur http://localhost:5173
2. Appuyer sur **F5** pour rafraîchir
3. Vous devriez voir :
   - 📋 **Titre** : "Gestion des Besoins en Personnel"
   - ➕ **Bouton** : "Nouveau Besoin"
   - 📦 **6 cartes** avec les besoins en personnel

---

## 🎯 Si la page est toujours vide

### Vérifier la console du navigateur (F12)

1. Ouvrir http://localhost:5173
2. Appuyer sur **F12**
3. Aller dans l'onglet **Console**
4. Chercher des erreurs en rouge

### Erreurs possibles et solutions

| Erreur | Solution |
|--------|----------|
| `CORS error` | Redémarrer le backend |
| `Failed to fetch` | Vérifier que le backend tourne sur port 8080 |
| `404 Not Found` | Vérifier l'URL de l'API dans `staffingNeedService.ts` |
| Page blanche | Vérifier la console pour erreurs JavaScript |

---

## 🔄 Redémarrer les serveurs

### Redémarrer le Backend

```powershell
# Terminal backend - Arrêter avec Ctrl+C puis :
cd "d:\S5\Mr tovo\zentra\zentra-core"
.\mvnw.cmd spring-boot:run
```

### Redémarrer le Frontend

```powershell
# Terminal frontend - Arrêter avec Ctrl+C puis :
cd "d:\S5\Mr tovo\zentra\zentra-ui"
npm run dev
```

---

## 📝 Créer un besoin manuellement (si les données de test ne fonctionnent pas)

Si vous ne pouvez pas exécuter le script SQL, créez les données manuellement :

### 1. Créer un département

```sql
INSERT INTO department (id, name, description, annual_budget, created_at, updated_at)
VALUES (1, 'IT Department', 'Technologies de l''information', 500000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

### 2. Créer un poste

```sql
INSERT INTO job (id, title, description, required_degree, required_skills, department_id, created_date, updated_at)
VALUES (1, 'Développeur Backend', 'Développeur Spring Boot', 'Master', 'Java, Spring', 1, CURRENT_DATE, CURRENT_DATE);
```

### 3. Créer un besoin

```sql
INSERT INTO staffing_need (title, description, number_of_positions, priority, status, 
                           required_start_date, budget_allocated, justification,
                           department_id, job_id, created_at, updated_at)
VALUES ('Développeur Full Stack', 
        'Besoin urgent',
        2, 'High', 'Open',
        '2026-01-15', 120000.00,
        'Expansion équipe',
        1, 1,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

---

## ✅ Une fois les données chargées

1. **Rafraîchir le navigateur** (F5)
2. Vous devriez voir la **liste des besoins**
3. Testez les fonctionnalités :
   - ➕ Créer un nouveau besoin
   - ✏️ Modifier un besoin
   - 👁️ Voir les détails
   - 🗑️ Supprimer un besoin
   - 🔍 Filtrer par statut/priorité

---

## 🎉 C'est tout !

Si tout fonctionne, vous devriez avoir :
- Une liste de besoins affichée
- Des filtres fonctionnels
- Un formulaire de création
- Une vue détaillée

**Besoin d'aide ?** Vérifiez les logs :
- **Backend** : Terminal où tourne `mvnw.cmd spring-boot:run`
- **Frontend** : Console navigateur (F12)
