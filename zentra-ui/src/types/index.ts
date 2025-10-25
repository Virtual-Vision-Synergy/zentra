export interface CandidateMinInfoDto {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  applicationId: number;
}

export interface ChoiceDto {
  id: number;
  libelle: string;
  correct?: boolean;
  questionId: number;
}

export interface QuestionDto {
  id: number;
  libelle: string;
  required: boolean;
  score: number;
  qcmId: number;
  choices: ChoiceDto[];
}

export interface QcmDto {
  id: number;
  title: string;
  description: string;
  durationMinutes: number;
  totalScore: number;
  requiredScore: number;
  questions: QuestionDto[];
}

export interface AnswerDto {
  choiceId: number;
}

export interface AttemptDto {
  answers: AnswerDto[];
}

// Types pour les formulaires de création/édition
export interface ChoiceFormDto {
  id?: number;
  libelle: string;
  correct: boolean;
}

export interface QuestionFormDto {
  id?: number;
  libelle: string;
  required: boolean;
  score: number;
  choices: ChoiceFormDto[];
}

export interface QcmFormDto {
  id?: number;
  title: string;
  description: string;
  durationMinutes: number;
  requiredScore: number;
  questions: QuestionFormDto[];
}

// Type pour la liste des QCMs
export interface QcmListItemDto {
  id: number;
  title: string;
  description: string;
  durationMinutes: number;
  totalScore: number;
  requiredScore: number;
  questionsCount: number;
}

export interface PublicationDto {
  id: number;
  title: string;
  description: string;
  publishedDate: string;
  closingDate?: string;
  numberOfPositions: number;
  status: string;
  jobId: number;
}

export interface PublicationFormDto {
  id?: number;
  title: string;
  description: string;
  publishedDate?: string;
  closingDate?: string;
  numberOfPositions: number;
  status: string;
  jobId: number;
}
