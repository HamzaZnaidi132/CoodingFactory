# CodingFactory — Modules Chatbot & PFE

Projet professionnel pour **CodingFactory** : assistant consulting (chatbot) et espace **PFE** (sujets, projets réalisés, candidatures).

## Stack

| Couche | Technologie |
|--------|-------------|
| **Frontend** | Angular 19 (UI, routing, formulaires) |
| **Backend** | Spring Boot 3.2 (API REST, JPA, validation) |
| **Base** | H2 (dev) — remplaçable par PostgreSQL en prod |
| **DevOps** | Docker, Docker Compose, GitHub Actions (CI/CD) |
| **Tests** | JUnit/MockMvc (backend), Jasmine/Karma (frontend), JaCoCo |

> **Note :** En architecture classique, **Angular = frontend** et **Spring Boot = backend** (c’est l’organisation retenue ici).

## Démarrage local

### Backend

```bash
cd backend
mvn spring-boot:run
```

Sous Windows, si le port est occupé (double lancement) :

```powershell
netstat -ano | findstr :8090
taskkill /PID <numéro_PID> /F
```

Ou utilisez le script : `.\run-dev.ps1` (propose d’arrêter l’ancienne instance Java).

API : `http://localhost:8090` (évite le conflit avec Oracle sur 8080)  
Swagger non inclus — endpoints sous `/api/v1/...`

### Frontend

```bash
cd frontend
npm install
npm start
```

UI : `http://localhost:4200` (proxy API vers `:8090`)

## Endpoints principaux

### Chatbot consulting

- `POST /api/v1/chatbot/consulting/message`
- `GET /api/v1/chatbot/consulting/services`
- `GET /api/v1/chatbot/consulting/services/{code}`

### PFE (Espace Étudiant / Candidat)

- `GET /api/v1/pfe/topics?openOnly=true|false` : Liste des sujets proposés
- `GET /api/v1/pfe/topics/{id}` : Détail d'un sujet PFE
- `GET /api/v1/pfe/projects` : Projets PFE réalisés et retours d'expérience
- `POST /api/v1/pfe/applications` : Dépôt d'une candidature
- `POST /api/v1/pfe/recommendations` : Recommandations intelligentes de sujets adaptées aux compétences et centres d'intérêt

### PFE (Espace Administration)

- `POST /api/v1/admin/pfe/topics` : Création d'un nouveau sujet
- `PUT /api/v1/admin/pfe/topics/{id}` : Modification d'un sujet
- `DELETE /api/v1/admin/pfe/topics/{id}` : Suppression d'un sujet
- `POST /api/v1/admin/pfe/projects` : Ajout d'un projet réalisé
- `PUT /api/v1/admin/pfe/projects/{id}` : Modification d'un projet réalisé
- `DELETE /api/v1/admin/pfe/projects/{id}` : Suppression d'un projet réalisé
- `GET /api/v1/admin/pfe/applications` : Consultation de toutes les candidatures avec filtres
- `PUT /api/v1/admin/pfe/applications/{id}/accept` : Acceptation d'une candidature
- `PUT /api/v1/admin/pfe/applications/{id}/reject` : Refus d'une candidature

## Tests

```bash
cd backend && mvn test
cd frontend && npm run test:ci
```

## Docker

```bash
docker compose up --build
```

- Frontend : http://localhost  
- Backend : http://localhost:8090  

## CI/CD

Le workflow `.github/workflows/ci-cd.yml` exécute :

1. **CI** : tests + build backend et frontend sur `main` / `develop` et PR
2. **CD** : build des images Docker sur push `main` (étape deploy à brancher sur votre infra)

## Structure

```
codingfactory/
├── backend/          # Spring Boot — chatbot + PFE
├── frontend/         # Angular — pages Chatbot & PFE
├── docker-compose.yml
└── .github/workflows/ci-cd.yml
```
