export interface User {
  id: string;
  email: string;
  name: string;
  role: 'ADMIN' | 'TEACHER' | 'STUDENT';
  createdAt: Date;
}

export interface Course {
  id: string;
  title: string;
  description: string;
  price: number;
  imageUrl: string;
  instructor: string;
  modules: Module[];
  totalDuration: number;
  enrolledStudents: number;
  createdAt: Date;
}

export interface Module {
  id: string;
  title: string;
  description: string;
  order: number;
  lessons: Lesson[];
  quiz: Quiz;
  isCompleted?: boolean;
  examPassed?: boolean;
}

export interface Lesson {
  id: string;
  title: string;
  description: string;
  videoUrl: string;
  duration: number;
  order: number;
  questions: Question[];
  isCompleted?: boolean;
}

export interface Question {
  id: string;
  text: string;
  options: string[];
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
  certificateUrl: string;
}

export interface AuthResponse {
  access_token: string;
  refresh_token: string;
  user: User;
}

export interface ApiError {
  message: string;
  status: number;
}