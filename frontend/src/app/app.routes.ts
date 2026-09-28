import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { ConsultingChatbotComponent } from './features/chatbot/consulting-chatbot.component';
import { PfeLayoutComponent } from './features/pfe/pfe-layout.component';
import { PfePortalComponent } from './features/pfe/pfe-portal.component';
import { PfeTopicsComponent } from './features/pfe/pfe-topics.component';
import { PfeProjectsComponent } from './features/pfe/pfe-projects.component';
import { PfeProjectDetailComponent } from './features/pfe/pfe-project-detail.component';
import { PfeApplicationComponent } from './features/pfe/pfe-application.component';
import { PfeRecommendationsComponent } from './features/pfe/pfe-recommendations.component';
import { AdminLayoutComponent } from './features/admin/admin-layout.component';
import { AdminDashboardComponent } from './features/admin/admin-dashboard.component';
import { AdminTopicsComponent } from './features/admin/admin-topics.component';
import { AdminProjectsComponent } from './features/admin/admin-projects.component';
import { AdminApplicationsComponent } from './features/admin/admin-applications.component';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'consulting/chatbot', component: ConsultingChatbotComponent },
  {
    path: 'pfe',
    component: PfeLayoutComponent,
    children: [
      { path: '', component: PfePortalComponent },
      { path: 'sujets', component: PfeTopicsComponent },
      { path: 'projets', component: PfeProjectsComponent },
      { path: 'projets/:id', component: PfeProjectDetailComponent },
      { path: 'candidature', component: PfeApplicationComponent },
      { path: 'recommandations', component: PfeRecommendationsComponent },
    ],
  },
  {
    path: 'admin',
    component: AdminLayoutComponent,
    canActivate: [adminGuard],
    children: [
      { path: '', component: AdminDashboardComponent },
      { path: 'sujets', component: AdminTopicsComponent },
      { path: 'projets', component: AdminProjectsComponent },
      { path: 'candidatures', component: AdminApplicationsComponent },
    ],
  },
  { path: '**', redirectTo: '' },
];
