export interface User {
  id: string;
  email: string;
  name: string;
  role: 'ADMIN' | 'TEACHER' | 'STUDENT';
  avatar?: string;
  createdAt: Date;
}

export interface Course {
  id: string;
  title: string;
  description: string;
  price: number;
  imageUrl: string;
  instructor?: User;  // Mudou de string para User
  teachers?: User[];  // Adicionado - múltiplos teachers
  modules: Module[];
  totalDuration: number;
  enrolledStudents: number;
  createdAt: Date;
}

export interface Module {
  id: string;
  title: string;
  description: string;
  order: number;  // Mudou de 'order' para 'moduleOrder'
  lessons: Lesson[];
  quiz?: Quiz;
  isCompleted?: boolean;
  examPassed?: boolean;
}

export interface Lesson {
  id: string;
  title: string;
  description: string;
  videoUrl: string;
  duration: number;
  lessonOrder: number;  // Mudou de 'order' para 'lessonOrder'
  questions: Question[];
  isCompleted?: boolean;
}

export interface Option {
  id: string;
  text: string;
  optionOrder: number;
}

export interface Question {
  id: string;
  text: string;
  options: Option[];  // Pode vir como string JSON ou array
  correctAnswer: number;
  explanation?: string;
}

export interface Quiz {
  id: string;
  title: string;
  questions: Question[];
  passingScore: number;
}

export interface Purchase {
  id: string;
  courseId: string;
  userId: string;
  purchaseDate: Date;
  amount: number;
  status: 'PENDING' | 'COMPLETED' | 'FAILED';
}

export interface Certificate {
  id: string;
  courseId: string;
  userId: string;
  userName: string;
  courseTitle: string;
  completionDate: Date;
  certificateCode: string;  // Adicionado
  certificateUrl: string;
}

export interface AuthResponse {
  accessToken: string;  // Mudou de access_token para accessToken
  refreshToken: string; // Mudou de refresh_token para refreshToken
  user: User;
}

export interface ProgressResponse {
  courseId: string;
  completedLessons: number;
  totalLessons: number;
  percentage: number;
  lessonStatus: Record<string, boolean>;
  lessonsScores: Record<string, number>;
}

export interface ApiError {
  message: string;
  status: number;
}