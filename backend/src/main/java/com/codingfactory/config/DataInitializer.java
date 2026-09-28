package com.codingfactory.config;

import com.codingfactory.chatbot.domain.ConsultingService;
import com.codingfactory.chatbot.repository.ConsultingServiceRepository;
import com.codingfactory.pfe.domain.PfeApplication;
import com.codingfactory.pfe.domain.PfeCompletedProject;
import com.codingfactory.pfe.domain.PfeTopic;
import com.codingfactory.pfe.domain.enums.PfeApplicationStatus;
import com.codingfactory.pfe.domain.enums.PfeTopicStatus;
import com.codingfactory.pfe.repository.PfeApplicationRepository;
import com.codingfactory.pfe.repository.PfeCompletedProjectRepository;
import com.codingfactory.pfe.repository.PfeTopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            ConsultingServiceRepository consultingServiceRepository,
            PfeTopicRepository pfeTopicRepository,
            PfeCompletedProjectRepository completedProjectRepository,
            PfeApplicationRepository applicationRepository
    ) {
        return args -> {
            if (consultingServiceRepository.count() == 0) {
                consultingServiceRepository.saveAll(java.util.List.of(
                        ConsultingService.builder()
                                .code("cloud-devops")
                                .title("Cloud & DevOps")
                                .description("Audit cloud, migration, CI/CD, conteneurisation et observabilité.")
                                .contactEmail("cloud@codingfactory.tn")
                                .keywords("cloud,devops,aws,azure,kubernetes,docker,ci/cd,infra")
                                .build(),
                        ConsultingService.builder()
                                .code("data-ai")
                                .title("Data & Intelligence artificielle")
                                .description("Architecture data, pipelines, ML ops et cas d'usage IA métier.")
                                .contactEmail("data@codingfactory.tn")
                                .keywords("data,ia,ai,machine learning,analytics,big data,python")
                                .build(),
                        ConsultingService.builder()
                                .code("cybersecurity")
                                .title("Cybersécurité")
                                .description("Audit sécurité, durcissement, conformité et sensibilisation.")
                                .contactEmail("security@codingfactory.tn")
                                .keywords("sécurité,cyber,audit,iso,rssi,conformité")
                                .build(),
                        ConsultingService.builder()
                                .code("digital-transformation")
                                .title("Transformation digitale")
                                .description("Accompagnement stratégique, modernisation SI et conduite du changement.")
                                .contactEmail("consulting@codingfactory.tn")
                                .keywords("transformation,digital,modernisation,si,stratégie")
                                .build(),
                        ConsultingService.builder()
                                .code("custom-training")
                                .title("Formation sur mesure entreprise")
                                .description("Parcours adaptés aux équipes : développement, agile, cloud, data.")
                                .contactEmail("formation@codingfactory.tn")
                                .keywords("formation,training,entreprise,équipe,upskilling")
                                .build()
                ));
            }

            if (pfeTopicRepository.count() == 0) {
                PfeTopic topic1 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Plateforme e-learning adaptative CodingFactory")
                        .description("Conception d'une plateforme d'apprentissage modulaire intégrant des parcours personnalisés, des quiz interactifs, la validation des acquis par projet et la délivrance automatisée de certificats vérifiables.")
                        .domain("Développement web & EdTech")
                        .technologies("Spring Boot, Angular, PostgreSQL, Docker")
                        .supervisorName("Dr. Amira Ben Salah")
                        .status(PfeTopicStatus.OPEN)
                        .maxCandidates(3)
                        .build());

                PfeTopic topic2 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Assistant conversationnel IA & RAG pour consulting")
                        .description("Développement d'un chatbot intelligent exploitant les LLMs en local et une base de connaissances vectorielle (RAG) pour qualifier les besoins clients et les orienter vers les offres de conseil en transformation digitale.")
                        .domain("Intelligence Artificielle & NLP")
                        .technologies("Spring Boot, Python, Ollama, LangChain, pgvector")
                        .supervisorName("Ing. Karim Trabelsi")
                        .status(PfeTopicStatus.OPEN)
                        .maxCandidates(2)
                        .build());

                PfeTopic topic3 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Observabilité Cloud-Native et GitOps multi-environnements")
                        .description("Mise en place d'un pipeline GitOps automatisé avec déploiement continu multi-clusters, corrélation des métriques, traces distribuées et alerting intelligent pour microservices à haute disponibilité.")
                        .domain("DevOps & Cloud")
                        .technologies("Kubernetes, ArgoCD, Prometheus, Grafana, OpenTelemetry")
                        .supervisorName("Ing. Youssef Mabrouk")
                        .status(PfeTopicStatus.ASSIGNED)
                        .maxCandidates(1)
                        .build());

                PfeTopic topic4 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Application mobile de télésuivi médical et IoT")
                        .description("Solution mobile connectée permettant la collecte en temps réel des constantes vitales via capteurs IoT, avec détection d'anomalies, tableau de bord pour praticiens et notifications d'urgence.")
                        .domain("Mobile & IoT")
                        .technologies("React Native, Node.js, MongoDB, MQTT, WebSockets")
                        .supervisorName("Dr. Fatma Gharbi")
                        .status(PfeTopicStatus.OPEN)
                        .maxCandidates(2)
                        .build());

                PfeTopic topic5 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Détection de fraude transactionnelle par Machine Learning")
                        .description("Conception et déploiement d'un pipeline de scoring en continu des transactions financières, capable d'identifier les comportements suspects avec une latence inférieure à 50ms et une explicabilité SHAP.")
                        .domain("Data Science & ML")
                        .technologies("Python, Scikit-learn, Apache Kafka, Spark Streaming")
                        .supervisorName("Dr. Mohamed Bouazizi")
                        .status(PfeTopicStatus.OPEN)
                        .maxCandidates(2)
                        .build());

                PfeTopic topic6 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Plateforme DevSecOps & Automatisation de la conformité CI/CD")
                        .description("Intégration de tests de sécurité automatisés (SAST, DAST, SCA) dans les pipelines d'intégration continue avec gestion centralisée des secrets et tableau de bord de conformité ISO 27001.")
                        .domain("Cybersécurité & DevSecOps")
                        .technologies("OWASP ZAP, SonarQube, Trivy, HashiCorp Vault, GitLab CI")
                        .supervisorName("Ing. Walid Cherif")
                        .status(PfeTopicStatus.OPEN)
                        .maxCandidates(2)
                        .build());

                PfeTopic topic7 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Moteur de recommandation e-commerce par IA et streaming")
                        .description("Conception d'un système de recommandation prédisant les intentions d'achat des utilisateurs en temps réel à l'aide de modèles de graphes et de streaming d'événements à haute cadence.")
                        .domain("Big Data & IA")
                        .technologies("Neo4j, Apache Flink, Redis, Python, FastAPI")
                        .supervisorName("Dr. Sonia Mejri")
                        .status(PfeTopicStatus.OPEN)
                        .maxCandidates(2)
                        .build());

                PfeTopic topic8 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Certification académique et badges vérifiables sur Blockchain")
                        .description("Système décentralisé permettant l'émission inviolable et l'authentification instantanée des attestations de formation délivrées par CodingFactory à travers des Smart Contracts.")
                        .domain("Web3 & Blockchain")
                        .technologies("Solidity, Ethereum, Hardhat, Next.js, Web3.js")
                        .supervisorName("Dr. Taher Mansour")
                        .status(PfeTopicStatus.OPEN)
                        .maxCandidates(2)
                        .build());

                PfeTopic topic9 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Automatisation des tests E2E et observabilité QA pilotée par IA")
                        .description("Création d'un framework moderne d'assurance qualité avec génération automatisée de scénarios de tests E2E à partir de spécifications utilisateur et analyse intelligente des régressions.")
                        .domain("Ingénierie Logicielle & QA")
                        .technologies("Playwright, TypeScript, Allure, Docker, LLM")
                        .supervisorName("Ing. Leila Ben Salem")
                        .status(PfeTopicStatus.OPEN)
                        .maxCandidates(1)
                        .build());

                PfeTopic topic10 = pfeTopicRepository.save(PfeTopic.builder()
                        .title("Contrôle qualité industriel par vision par ordinateur & Edge AI")
                        .description("Système embarqué d'inspection visuelle automatique sur chaîne de montage, détectant les défauts de fabrication en temps réel avec inférence optimisée sur processeur Edge.")
                        .domain("Vision par ordinateur & Edge AI")
                        .technologies("YOLOv8, PyTorch, OpenCV, Jetson Nano, Docker")
                        .supervisorName("Dr. Sami Khelil")
                        .status(PfeTopicStatus.ASSIGNED)
                        .maxCandidates(2)
                        .build());

                // Seed demo applications
                if (applicationRepository.count() == 0) {
                    applicationRepository.saveAll(java.util.List.of(
                            PfeApplication.builder()
                                    .topic(topic1)
                                    .fullName("Amine Khelifi")
                                    .email("amine.khelifi@esprit.tn")
                                    .school("ESPRIT")
                                    .level("Cycle ingénieur")
                                    .motivation("Passionné par le développement web full-stack, j'ai une expérience solide avec Spring Boot et Angular à travers mes projets académiques. Je souhaite approfondir mes compétences en conception de plateformes modulaires et travailler sur un projet à impact éducatif.")
                                    .portfolioUrl("https://github.com/amine-khelifi")
                                    .status(PfeApplicationStatus.RECEIVED)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 3))
                                    .build(),
                            PfeApplication.builder()
                                    .topic(topic1)
                                    .fullName("Rania Mansouri")
                                    .email("rania.mansouri@tek-up.tn")
                                    .school("TEK-UP University")
                                    .level("Master")
                                    .motivation("Mon mémoire de master porte sur les plateformes adaptatives d'apprentissage. Ce sujet s'aligne parfaitement avec mes recherches. J'ai réalisé un prototype avec React et Node.js que je souhaite enrichir avec les technologies proposées.")
                                    .status(PfeApplicationStatus.RECEIVED)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 2))
                                    .build(),
                            PfeApplication.builder()
                                    .topic(topic2)
                                    .fullName("Yassine Ben Ali")
                                    .email("yassine.benali@insat.tn")
                                    .school("INSAT")
                                    .level("Cycle ingénieur")
                                    .motivation("J'ai développé un chatbot de support client avec Rasa et Python lors de mon stage. Je maîtrise les APIs LLM (OpenAI, Ollama) et souhaite mettre ces compétences au service d'un projet consulting innovant.")
                                    .portfolioUrl("https://linkedin.com/in/yassine-benali")
                                    .status(PfeApplicationStatus.ACCEPTED)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 5))
                                    .build(),
                            PfeApplication.builder()
                                    .topic(topic2)
                                    .fullName("Nour El Houda Triki")
                                    .email("nour.triki@enis.tn")
                                    .school("ENIS")
                                    .level("Licence")
                                    .motivation("Bien que je sois en licence, j'ai une passion pour l'IA et j'ai suivi des cours en ligne sur le NLP. Je cherche un stage de fin d'études pour mettre en pratique ces connaissances dans un contexte professionnel.")
                                    .status(PfeApplicationStatus.REJECTED)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 7))
                                    .build(),
                            PfeApplication.builder()
                                    .topic(topic3)
                                    .fullName("Farouk Trabelsi")
                                    .email("farouk.trabelsi@enit.utm.tn")
                                    .school("ENIT")
                                    .level("Cycle ingénieur")
                                    .motivation("Spécialisé en réseaux et systèmes distribués, j'ai administré des clusters Kubernetes et automatisé des déploiements avec GitLab CI. Ce sujet Cloud-Native représente l'aboutissement idéal pour mon projet de fin d'études.")
                                    .portfolioUrl("https://github.com/farouk-trabelsi")
                                    .status(PfeApplicationStatus.ACCEPTED)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 4))
                                    .build(),
                            PfeApplication.builder()
                                    .topic(topic5)
                                    .fullName("Salma Bouzid")
                                    .email("salma.bouzid@ensi-uma.tn")
                                    .school("ENSI")
                                    .level("Cycle ingénieur")
                                    .motivation("Très investie dans le machine learning et les architectures de données distribuées (Kafka, Spark). J'ai déjà mené un projet académique sur la classification de séries temporelles financières.")
                                    .portfolioUrl("https://github.com/salma-bouzid")
                                    .status(PfeApplicationStatus.UNDER_REVIEW)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 1))
                                    .build(),
                            PfeApplication.builder()
                                    .topic(topic6)
                                    .fullName("Khalil Ayadi")
                                    .email("khalil.ayadi@esprit.tn")
                                    .school("ESPRIT")
                                    .level("Cycle ingénieur")
                                    .motivation("Passionné par la cybersécurité offensive et le DevSecOps. J'ai obtenu la certification CEH et mis en place des pipelines SonarQube et Trivy sur mes projets universitaires.")
                                    .portfolioUrl("https://github.com/khalil-sec")
                                    .status(PfeApplicationStatus.RECEIVED)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 2))
                                    .build(),
                            PfeApplication.builder()
                                    .topic(topic7)
                                    .fullName("Ines Gharbi")
                                    .email("ines.gharbi@insat.u-carthage.tn")
                                    .school("INSAT")
                                    .level("Master Data Science")
                                    .motivation("Mon projet de recherche actuel porte sur les systèmes de recommandation par graphes (GNN). Je souhaite appliquer ces travaux sur des flux d'événements e-commerce à fort trafic.")
                                    .status(PfeApplicationStatus.UNDER_REVIEW)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 3))
                                    .build(),
                            PfeApplication.builder()
                                    .topic(topic8)
                                    .fullName("Hamza Ben Romdhane")
                                    .email("hamza.romdhane@fst.utm.tn")
                                    .school("FST")
                                    .level("Master")
                                    .motivation("Développeur Web3 et Solidity depuis 2 ans, j'ai déployé plusieurs contrats sur testnets Polygon et Ethereum. Je souhaite professionnaliser la certification de badges académiques.")
                                    .portfolioUrl("https://github.com/hamza-web3")
                                    .status(PfeApplicationStatus.RECEIVED)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 4))
                                    .build(),
                            PfeApplication.builder()
                                    .topic(topic10)
                                    .fullName("Meriem Louati")
                                    .email("meriem.louati@ensi-uma.tn")
                                    .school("ENSI")
                                    .level("Cycle ingénieur")
                                    .motivation("Passionnée par la vision par ordinateur, j'ai entraîné des modèles YOLOv8 et optimisé l'inférence via TensorRT. Ce sujet de contrôle qualité en milieu industriel répond exactement à mes ambitions.")
                                    .portfolioUrl("https://github.com/meriem-louati")
                                    .status(PfeApplicationStatus.ACCEPTED)
                                    .submittedAt(Instant.now().minusSeconds(86400 * 6))
                                    .build()
                    ));
                }
            }

            if (completedProjectRepository.count() == 0) {
                completedProjectRepository.saveAll(java.util.List.of(
                        PfeCompletedProject.builder()
                                .title("Marketplace B2B pour artisans")
                                .studentName("Sarra Haddad")
                                .academicYear("2024-2025")
                                .summary("Place de marché connectant artisans et fournisseurs avec gestion des commandes.")
                                .methodology("Scrum, DDD, API REST, tests automatisés.")
                                .results("MVP déployé, +35% de leads qualifiés pour le client pilote.")
                                .completionDate(LocalDate.of(2025, 6, 15))
                                .build(),
                        PfeCompletedProject.builder()
                                .title("Système de recommandation formations")
                                .studentName("Mehdi Jlassi")
                                .academicYear("2023-2024")
                                .summary("Moteur de recommandation basé sur profils et historique de navigation.")
                                .methodology("CRISP-DM, A/B testing, pipeline batch + API temps réel.")
                                .results("Précision@5 améliorée de 22%, intégration au site CodingFactory.")
                                .completionDate(LocalDate.of(2024, 7, 2))
                                .build(),
                        PfeCompletedProject.builder()
                                .title("Plateforme d'audit de sécurité des infrastructures Cloud")
                                .studentName("Rim Jaziri")
                                .academicYear("2024-2025")
                                .summary("Automatisation de l'audit de posture de sécurité (CSPM) multi-cloud avec détection continue de mauvaises configurations.")
                                .methodology("DevSecOps, Infra-as-Code scanning, Open Policy Agent (OPA), AWS Well-Architected.")
                                .results("Temps de remédiation réduit de 60%, 100% de conformité CIS Benchmark atteinte.")
                                .completionDate(LocalDate.of(2025, 7, 10))
                                .build(),
                        PfeCompletedProject.builder()
                                .title("Portail RH intelligent avec parsing sémantique de CV")
                                .studentName("Khaled Trabelsi")
                                .academicYear("2023-2024")
                                .summary("Outil de recrutement automatisé analysant les candidatures et calculant le matching avec les fiches de poste via NLP.")
                                .methodology("Agile Kanban, NLP sémantique, SpaCy, Microservices Spring Boot et Angular.")
                                .results("Temps de tri des candidatures divisé par 4, adopté par 3 clients entreprises de CodingFactory.")
                                .completionDate(LocalDate.of(2024, 6, 28))
                                .build(),
                        PfeCompletedProject.builder()
                                .title("Architecture Microservices Event-Driven pour la logistique")
                                .studentName("Cyrine Rekik")
                                .academicYear("2024-2025")
                                .summary("Système de suivi et de dispatching de flotte en temps réel avec messagerie haute performance.")
                                .methodology("Domain-Driven Design, CQRS & Event Sourcing, Apache Kafka, Docker & Kubernetes.")
                                .results("Traitement de 10 000 événements/sec avec une disponibilité mesurée à 99.98%.")
                                .completionDate(LocalDate.of(2025, 6, 20))
                                .build()
                ));
            }
        };
    }
}
