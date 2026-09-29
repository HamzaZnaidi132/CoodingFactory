# CodingFactory — Modules Chatbot & PFE

Projet professionnel pour **CodingFactory** : assistant consulting (chatbot) et espace **PFE** (sujets, projets réalisés, candidatures).

## Stack

| Couche | Technologie |
|--------|-------------|
| **Frontend** | Angular 19 (UI, routing, formulaires) |
| **Backend** | Spring Boot 3.2 en architecture microservices |
| **Base** | H2 (dev) — remplaçable par PostgreSQL en prod |
| **DevOps** | Docker, Docker Compose, GitHub Actions (CI/CD) |
| **Tests** | JUnit/MockMvc (backend), Jasmine/Karma (frontend), JaCoCo |

> **Note :** En architecture classique, **Angular = frontend** et **Spring Boot = backend** (c’est l’organisation retenue ici).
> Le dossier [backend](backend) contient l'ancien monolithe; pour l'architecture microservices, utilisez [eureka-server](eureka-server), [api-gateway](api-gateway), [chatbot-service](chatbot-service) et [pfe-service](pfe-service).

## Démarrage local

### Microservices

```bash
mvn -q -DskipTests package
docker compose up --build
```

Utilisez soit Docker Compose, soit `mvn spring-boot:run` pour démarrer les
microservices. Les deux modes utilisent les mêmes ports (`8090` à `8092` et
`8761`) et ne doivent pas être lancés en même temps.

Points d'entrée :
- Gateway: `http://localhost:8090`
- Eureka Server: `http://localhost:8761`
- Chatbot: `http://localhost:8091`
- PFE: `http://localhost:8092`

Endpoints principaux via le gateway :

- `POST /api/v1/chatbot/consulting/message`
- `GET /api/v1/chatbot/consulting/services`
- `GET /api/v1/pfe/topics`
- `POST /api/v1/pfe/applications`

### Frontend

```bash
cd frontend
npm install
npm start
```

UI : `http://localhost:4200` (proxy API vers le gateway `:8090`)

## Tests

```bash
cd . && mvn test
cd frontend && npm run test:ci
```

## Docker

```bash
docker compose up --build
```

Pour éviter un conflit de port sur la machine hôte, les ports publiés peuvent être
redéfinis séparément sans modifier les ports utilisés entre conteneurs :

```powershell
$env:GATEWAY_HOST_PORT=18090
$env:CHATBOT_HOST_PORT=18091
$env:PFE_HOST_PORT=18092
$env:EUREKA_HOST_PORT=18761
$env:FRONTEND_HOST_PORT=8088
docker compose up --build
```

- Frontend : http://localhost:8088
- Gateway : http://localhost:8090
- Eureka Server : http://localhost:8761

## CI/CD

Le workflow `.github/workflows/ci-cd.yml` exécute :

1. **CI** : tests + build backend et frontend sur `main` / `develop` et PR
2. **CD** : build des images Docker sur push `main` (étape deploy à brancher sur votre infra)

### Jenkins

Le fichier [Jenkinsfile](Jenkinsfile) exécute la pipeline backend Jenkins
declarative. Configurez un agent avec Java 17, Maven et Docker, puis créez un
job Pipeline depuis le dépôt SCM.

La pipeline couvre `api-gateway`, `chatbot-service`, `eureka-server`,
`pfe-service` et le projet parent `codingfactory-parent`. Le frontend Angular
est volontairement exclu de cette pipeline.

La CI s'exécute sur toutes les branches. La construction des images Docker
s'exécute uniquement sur `main`. Pour publier les images, créez dans Jenkins
un credential username/password nommé `docker-registry-credentials`, puis
activez le paramètre `PUSH_IMAGES` au lancement de la pipeline.

## Structure

```
codingfactory/
├── api-gateway/
├── chatbot-service/
├── eureka-server/
├── pfe-service/
├── frontend/         # Angular — pages Chatbot & PFE
├── docker-compose.yml
└── pom.xml
```
