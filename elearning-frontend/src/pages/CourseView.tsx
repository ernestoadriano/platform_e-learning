import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import type { Course, Module } from '../types';
import { Play, CheckCircle, Lock, ChevronRight } from 'lucide-react';

export const CourseView = () => {
  const { courseId } = useParams<{ courseId: string }>();
  const [course, setCourse] = useState<Course | null>(null);
  const [progress, setProgress] = useState<any>({});
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchCourseData();
  }, [courseId]);

  const fetchCourseData = async () => {
    try {
      const [courseRes, progressRes] = await Promise.all([
        api.get(`/courses/${courseId}`),
        api.get(`/courses/${courseId}/progress`)
      ]);
      setCourse(courseRes.data);
      setProgress(progressRes.data);
    } catch (error) {
      console.error('Failed to fetch course:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const isModuleLocked = (moduleOrder: number) => {
    if (moduleOrder === 0) return false;
    const previousModule = course?.modules[moduleOrder - 1];
    return previousModule ? !progress[previousModule.id]?.examPassed : false;
  };

  const isLessonLocked = (module: Module, lessonOrder: number) => {
    if (lessonOrder === 0) return false;
    const previousLesson = module.lessons[lessonOrder - 1];
    return previousLesson ? !progress[previousLesson.id]?.completed : false;
  };

  if (isLoading) {
    return <div className="loading">Loading course...</div>;
  }

  if (!course) {
    return <div className="error">Course not found</div>;
  }

  return (
    <div className="course-view">
      <div className="course-header">
        <div className="container">
          <h1>{course.title}</h1>
          <p>{course.description}</p>
        </div>
      </div>

      <div className="container">
        <div className="course-content">
          <div className="modules-list">
            {course.modules.map((module) => {  // ← REMOVIDO o moduleIndex não usado
              const moduleLocked = isModuleLocked(module.order);
              
              return (
                <div key={module.id} className="module-card card">
                  <div className="module-header">
                    <h3>Module {module.order + 1}: {module.title}</h3>
                    {moduleLocked ? <Lock size={18} /> : <CheckCircle size={18} className="success" />}
                  </div>
                  
                  <div className="lessons-list">
                    {module.lessons.map((lesson) => {
                      const lessonLocked = moduleLocked || isLessonLocked(module, lesson.lessonOrder);
                      const isCompleted = progress[lesson.id]?.completed;
                      
                      return (
                        <div 
                          key={lesson.id} 
                          className={`lesson-item ${isCompleted ? 'completed' : ''}`}
                          onClick={() => !lessonLocked && navigate(`/courses/${courseId}/modules/${module.id}/lessons/${lesson.id}`)}
                        >
                          <div className="lesson-icon">
                            {isCompleted ? <CheckCircle size={18} /> : <Play size={18} />}
                          </div>
                          <div className="lesson-info">
                            <span>{lesson.title}</span>
                            <small>{lesson.duration} min</small>
                          </div>
                          {lessonLocked && <Lock size={16} />}
                          {!lessonLocked && !isCompleted && <ChevronRight size={16} />}
                        </div>
                      );
                    })}
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
};