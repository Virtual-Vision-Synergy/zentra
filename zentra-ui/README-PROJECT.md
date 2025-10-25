# 🎓 Zentra UI - Plateforme de Gestion QCM

Application web moderne et complète pour la gestion de questionnaires à choix multiples (QCM) avec deux espaces distincts : candidat et administration.

![React](https://img.shields.io/badge/React-19.1-blue)
![TypeScript](https://img.shields.io/badge/TypeScript-5.9-blue)
![Vite](https://img.shields.io/badge/Vite-7.1-purple)
![React Router](https://img.shields.io/badge/React_Router-7.9-red)

---

## ✨ Fonctionnalités Principales

### 👤 Espace Candidat
- ✅ Authentification sécurisée par token
- ⏱️ Timer en temps réel avec alertes visuelles
- 📝 Interface intuitive pour répondre aux questions
- 📊 Barre de progression
- 🔔 Validation des questions obligatoires
- ⚡ Auto-soumission à la fin du temps
- 📱 Design 100% responsive

### 👨‍💼 Espace Administration
- 🏠 Dashboard avec cartes de navigation
- 📋 Gestion complète des QCM (CRUD)
- ➕ Création de QCM avec questions/choix dynamiques
- 📄 Vue détaillée de tous les QCMs
- ✏️ Édition avec formulaire intelligent
- 🗑️ Suppression avec confirmation
- 📌 Sidebar avec navigation hiérarchique
- 🎨 Interface professionnelle et moderne

---

## 🚀 Démarrage Rapide

### Prérequis
```bash
Node.js >= 18.0.0
npm >= 9.0.0
```

### Installation
```bash
# Installer les dépendances
npm install

# Créer le fichier de configuration
cp .env.example .env
# Éditer .env avec votre URL d'API
```

### Configuration
Créer un fichier `.env` à la racine :
```env
VITE_API_URL=http://localhost:8080/api
```

### Lancement
```bash
# Mode développement
npm run dev

# Build production
npm run build

# Preview du build
npm run preview
```

### Accès
- **Candidat**: http://localhost:5173/
- **Admin**: http://localhost:5173/admin

---

## 📁 Structure du Projet

```
src/
├── pages/              # Pages de l'application (7)
│   ├── UserLogin       # Connexion candidat
│   ├── QcmAttempt      # Passage du test
│   ├── Success         # Confirmation
│   ├── Dashboard       # Admin accueil
│   ├── QcmList         # Liste QCM
│   ├── QcmDetails      # Détails QCM
│   └── QcmForm         # Formulaire QCM
├── components/         # Composants réutilisables (5)
│   ├── Timer           # Timer circulaire
│   ├── QuestionCard    # Carte question
│   ├── ProgressBar     # Barre progression
│   ├── Sidebar         # Menu admin
│   └── AdminLayout     # Layout admin
├── styles/             # Fichiers CSS (13)
├── services/           # Services API
├── types/              # Types TypeScript
└── App.tsx             # Configuration routes
```

---

## 🎨 Technologies Utilisées

- **React 19** - Framework UI moderne
- **TypeScript** - Typage statique
- **React Router DOM 7** - Routing
- **Axios** - Client HTTP
- **Vite** - Build tool ultra-rapide
- **CSS3** - Styles et animations

---

## 🔌 API Endpoints

### Candidat
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/users/auth` | Authentification |
| GET | `/users/qcm` | Récupérer le QCM |
| POST | `/users/attempt` | Soumettre les réponses |

### Admin
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/qcms` | Liste des QCM |
| GET | `/qcms/:id` | Détails d'un QCM |
| POST | `/qcms` | Créer un QCM |
| PUT | `/qcms/:id` | Modifier un QCM |
| DELETE | `/qcms/:id` | Supprimer un QCM |

---

## 📚 Documentation Complète

| Fichier | Description |
|---------|-------------|
| [START.md](./START.md) | 🚀 Guide de démarrage rapide |
| [GUIDE.md](./GUIDE.md) | 📖 Guide complet espace candidat |
| [ADMIN.md](./ADMIN.md) | 📖 Guide complet espace admin |
| [TYPES.md](./TYPES.md) | 📝 Documentation des types |
| [DESIGN.md](./DESIGN.md) | 🎨 Aperçus visuels |
| [CHECKLIST.md](./CHECKLIST.md) | ✅ Checklist de vérification |
| [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) | 🔧 Résolution de problèmes |
| [PROJECT-COMPLETE.md](./PROJECT-COMPLETE.md) | 🎊 Vue d'ensemble complète |

---

## 🎨 Design

### Palette de Couleurs
- **Violet**: #667eea → #764ba2 (Principal)
- **Vert**: #48bb78 → #38a169 (Succès)
- **Rouge**: #ef4444 → #dc2626 (Danger)
- **Dark**: #1e293b → #0f172a (Sidebar)

### Animations
- Transitions fluides (0.3s ease)
- Hover effects élégants
- Loading spinners
- Fade in/out

---

## 📱 Responsive Design

- ✅ Mobile (< 768px)
- ✅ Tablette (768-1024px)
- ✅ Desktop (> 1024px)

Design optimisé pour tous les écrans avec layout adaptatif.

---

## 🔐 Sécurité

- Authentification JWT
- Token stocké dans localStorage
- Intercepteur Axios pour l'autorisation
- Validation côté client
- Protection des routes

---

## 🛠️ Scripts Disponibles

```bash
npm run dev          # Lancer le serveur de développement
npm run build        # Build pour production
npm run preview      # Preview du build
npm run lint         # Linter le code
```

---

## 🎯 Flows Utilisateur

### Candidat
1. Connexion avec token → Validation
2. Chargement du QCM → Affichage
3. Réponse aux questions → Navigation
4. Soumission → Confirmation

### Admin
1. Accès au dashboard → Navigation
2. Liste des QCM → Vue d'ensemble
3. Création/Édition → Formulaire
4. Validation → Sauvegarde

---

## 📞 Support

Pour toute question ou problème :
1. Consulter la documentation dans les fichiers .md
2. Vérifier [TROUBLESHOOTING.md](./TROUBLESHOOTING.md)

---

## 🎉 Statut du Projet

✅ **Production Ready**

- Interface candidat complète
- Interface admin professionnelle
- Design très élégant
- Code propre et documenté
- Responsive complet

---

**Développé avec ❤️ pour Zentra - Prêt à l'emploi ! 🚀**

