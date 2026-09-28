export type PfeTopicStatus = 'OPEN' | 'CLOSED' | 'ASSIGNED';
export type PfeApplicationStatus = 'RECEIVED' | 'UNDER_REVIEW' | 'ACCEPTED' | 'REJECTED';

export interface PfeTopic {
  id: number;
  title: string;
  description: string;
  domain: string;
  technologies: string;
  supervisorName: string;
  status: PfeTopicStatus;
  maxCandidates: number;
  applicationCount: number;
}

export interface PfeCompletedProject {
  id: number;
  title: string;
  studentName: string;
  academicYear: string;
  summary: string;
  methodology: string;
  results: string;
  completionDate: string;
}

export interface PfeApplicationRequest {
  topicId: number;
  fullName: string;
  email: string;
  school: string;
  level: string;
  motivation: string;
  portfolioUrl?: string;
}

export interface PfeApplicationResponse {
  id: number;
  topicId: number;
  topicTitle: string;
  status: string;
  submittedAt: string;
}

export interface PfeApplicationDetail {
  id: number;
  topicId: number;
  topicTitle: string;
  fullName: string;
  email: string;
  school: string;
  level: string;
  motivation: string;
  portfolioUrl: string | null;
  status: PfeApplicationStatus;
  submittedAt: string;
}

export interface PfeTopicCreateRequest {
  title: string;
  description: string;
  domain: string;
  technologies: string;
  supervisorName: string;
  status: PfeTopicStatus;
  maxCandidates: number;
}

export interface PfeProjectCreateRequest {
  title: string;
  studentName: string;
  academicYear: string;
  summary: string;
  methodology: string;
  results: string;
  completionDate: string;
}

export interface PfeRecommendationRequest {
  skills: string;
  interests: string;
  level: string;
}

export interface PfeRecommendation {
  topicId: number;
  topicTitle: string;
  domain: string;
  technologies: string;
  supervisorName: string;
  score: number;
  matchReasons: string[];
}
