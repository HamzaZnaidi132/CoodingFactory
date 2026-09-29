package com.codingfactory.pfe.service;

import com.codingfactory.pfe.domain.PfeCompletedProject;
import com.codingfactory.pfe.dto.PfeProjectRequest;
import com.codingfactory.pfe.dto.PfeProjectView;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PfeCompletedProjectService {

    private final Map<Long, PfeCompletedProject> projects = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public PfeCompletedProjectService() {
        seed();
    }

    public List<PfeProjectView> listProjects() {
        return projects.values().stream().map(this::toView).toList();
    }

    public PfeProjectView getProject(Long id) {
        return toView(findProject(id));
    }

    public PfeCompletedProject create(PfeProjectRequest request) {
        Long id = sequence.incrementAndGet();
        PfeCompletedProject project = new PfeCompletedProject(id, request.title(), request.studentName(), request.academicYear(), request.summary(), request.methodology(), request.results(), LocalDate.parse(request.completionDate()));
        projects.put(id, project);
        return project;
    }

    public PfeProjectView update(Long id, PfeProjectRequest request) {
        findProject(id);
        PfeCompletedProject project = new PfeCompletedProject(id, request.title(), request.studentName(), request.academicYear(), request.summary(), request.methodology(), request.results(), LocalDate.parse(request.completionDate()));
        projects.put(id, project);
        return toView(project);
    }

    public void delete(Long id) {
        if (projects.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
    }

    private PfeCompletedProject findProject(Long id) {
        PfeCompletedProject project = projects.get(id);
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        return project;
    }

    private PfeProjectView toView(PfeCompletedProject project) {
        return new PfeProjectView(project.id(), project.title(), project.studentName(), project.academicYear(), project.summary(), project.methodology(), project.results(), project.completionDate().toString());
    }

    private void seed() {
        create(new PfeProjectRequest(
                "Marketplace B2B pour artisans",
                "Sarra Haddad",
                "2024-2025",
                "Place de marché connectant artisans tunisiens et fournisseurs régionaux, avec catalogues, devis et suivi de commandes.",
                "Scrum, Domain-Driven Design, API REST Spring Boot, Angular, PostgreSQL.",
                "MVP déployé pour 40 artisans, délai de traitement des commandes réduit de 35%.",
                "2025-06-15"));
        create(new PfeProjectRequest(
                "Système de recommandation de formations",
                "Mehdi Jlassi",
                "2023-2024",
                "Moteur de recommandation personnalisé selon le profil, les compétences et les objectifs professionnels des apprenants.",
                "CRISP-DM, filtrage collaboratif, A/B testing, API Python + Spring Boot.",
                "Précision@5 améliorée de 22% et taux de clics sur les parcours recommandé +18%.",
                "2024-07-02"));
        create(new PfeProjectRequest(
                "Plateforme e-learning adaptative",
                "Nour Ben Amor",
                "2024-2025",
                "Espace d'apprentissage qui adapte les modules selon le niveau, le rythme et les résultats des quiz.",
                "Spring Boot, Angular, PostgreSQL, scoring adaptatif, CI/CD GitHub Actions.",
                "Taux de completion des modules porté à 71% sur un pilote de 120 étudiants.",
                "2025-05-28"));
        create(new PfeProjectRequest(
                "Assistant conversationnel IA & RAG",
                "Yassine Gharbi",
                "2024-2025",
                "Chatbot documentaire pour orienter les clients CodingFactory à partir de la base d'offres et des FAQ internes.",
                "RAG, embeddings, Ollama, Spring Boot, Angular, évaluation humaine des réponses.",
                "Temps moyen de première orientation réduit de 8 minutes à moins de 90 secondes.",
                "2025-06-30"));
        create(new PfeProjectRequest(
                "Observabilité Cloud-Native et GitOps",
                "Lina Chebbi",
                "2023-2024",
                "Chaîne GitOps multi-environnements avec métriques, traces et alertes pour les microservices CodingFactory.",
                "Kubernetes, Argo CD, Prometheus, Grafana, OpenTelemetry.",
                "MTTR divisé par deux et déploiements reproductibles sur trois environnements.",
                "2024-06-20"));
        create(new PfeProjectRequest(
                "Portail cybersécurité pour TPE/PME",
                "Omar Ferchichi",
                "2023-2024",
                "Tableau de bord d'hygiène numérique : scan de surface d'attaque, checklist conformité et sensibilisation des équipes.",
                "Spring Boot, Angular, scans automatisés, scoring de risque, rapports PDF.",
                "12 PME accompagnées, 64% des recommandations critiques traitées en moins de 30 jours.",
                "2024-07-18"));
        create(new PfeProjectRequest(
                "Pipeline data & indicateurs RH",
                "Ines Kacem",
                "2022-2023",
                "Collecte et visualisation des indicateurs recrutement, formation et rétention pour le pôle consulting.",
                "Airflow, dbt, PostgreSQL, dashboards Angular, API Spring Boot.",
                "Reporting hebdomadaire automatisé, suppression de 12 fichiers Excel manuels.",
                "2023-06-12"));
        create(new PfeProjectRequest(
                "Application mobile de suivi PFE",
                "Aymen Trabelsi",
                "2022-2023",
                "Application mobile pour le suivi des jalons, des livrables et des échanges encadrant-étudiant.",
                "Ionic/Angular, Spring Boot, notifications, rôles candidat et encadrant.",
                "Adhésion de 3 promotions, 90% des jalons saisis dans les délais.",
                "2023-07-05"));
    }
}