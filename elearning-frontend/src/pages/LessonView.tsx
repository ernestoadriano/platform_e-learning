import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
// @ts-ignore
import ReactPlayer from 'react-player';
import { api } from '../services/api';
import type { Lesson, Question } from '../types';
import { CheckCircle, ArrowLeft, Play, Lock, XCircle } from 'lucide-react';

export const LessonView = () => {
  const { courseId, moduleId, lessonId } = useParams();
  
  const [lesson, setLesson] = useState<Lesson | null>(null);
  const [questions, setQuestions] = useState<Question[]>([]);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [answers, setAnswers] = useState<number[]>([]);
  const [selectedAnswer, setSelectedAnswer] = useState<number | null>(null);
  const [answerFeedback, setAnswerFeedback] = useState<{ show: boolean; isCorrect: boolean; message: string }>({
    show: false,
    isCorrect: false,
    message: ''
  });
  const [showQuiz, setShowQuiz] = useState(false);
  const [quizCompleted, setQuizCompleted] = useState(false);
  const [quizScore, setQuizScore] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [canAccess, setCanAccess] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    checkAccess();
    fetchLessonData();
  }, [lessonId]);
  // Adicione este useEffect após as declarações de estado
    useEffect(() => {
    console.log('Answers updated:', answers);
    }, [answers]);

  const checkAccess = async () => {
    try {
      const response = await api.get(`/modules/${moduleId}/lessons/${lessonId}/can-access`);
      setCanAccess(response.data);
    } catch (error) {
      console.error('Failed to check access:', error);
      setCanAccess(false);
    }
  };

  const fetchLessonData = async () => {
    try {
      const response = await api.get(`/modules/${moduleId}/lessons/${lessonId}`);
      setLesson(response.data);
      setQuestions(response.data.questions || []);
    } catch (error) {
      console.error('Failed to fetch lesson:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleAnswerSelect = (answerIndex: number) => {
    if (answerFeedback.show) return; // Não permite mudar resposta após feedback
    setSelectedAnswer(answerIndex);
  };

  const checkAnswer = () => {
    if (selectedAnswer === null) {
      alert('Please select an answer before continuing.');
      return;
    }

    const currentQuestionData = questions[currentQuestion];
    const isCorrect = selectedAnswer === currentQuestionData.correctAnswer;
    
    setAnswerFeedback({
      show: true,
      isCorrect: isCorrect,
      message: isCorrect 
        ? '✓ Correct! Well done!' 
        : `✗ Wrong! The correct answer is: ${currentQuestionData.options[currentQuestionData.correctAnswer]?.text}`
    });
  };

  const nextQuestion = () => {
  // Salvar a resposta
  const newAnswers = [...answers];
  newAnswers[currentQuestion] = selectedAnswer!;
  setAnswers(newAnswers);
  
  console.log('Saved answer for question', currentQuestion, ':', selectedAnswer);
  console.log('All answers so far:', newAnswers);
  
  // Resetar estado para próxima pergunta
  setSelectedAnswer(null);
  setAnswerFeedback({ show: false, isCorrect: false, message: '' });
  
  if (currentQuestion + 1 < questions.length) {
    setCurrentQuestion(currentQuestion + 1);
  } else {
    // Todas as perguntas respondidas, enviar para o backend usando newAnswers (mais atualizado)
    console.log('All questions answered. Submitting:', newAnswers);
    submitQuiz(newAnswers);
  }
};

const submitQuiz = async (finalAnswers: number[]) => {
  try {
    console.log('Final answers being submitted:', finalAnswers);
    console.log('Expected number of answers:', questions.length);
    
    const response = await api.post(`/modules/${moduleId}/lessons/${lessonId}/complete`, {
      answers: finalAnswers
    });
    
    console.log('Response:', response);
    
    if (response.data && response.data.score !== undefined) {
      setQuizScore(response.data.score);
    }
    
    setQuizCompleted(true);
  } catch (error: any) {
    console.error('Failed to submit quiz:', error);
    
    if (error.response?.data?.message) {
      alert(error.response.data.message);
    } else {
      alert('Error submitting quiz. Please try again.');
    }
    
    // Reset para tentar novamente
    setAnswers([]);
    setCurrentQuestion(0);
    setSelectedAnswer(null);
    setAnswerFeedback({ show: false, isCorrect: false, message: '' });
    setShowQuiz(false);
  }
};

  const handleVideoComplete = () => {
    if (questions.length > 0) {
      setShowQuiz(true);
    } else {
      completeLesson();
    }
  };

  const completeLesson = async () => {
    try {
      await api.post(`/modules/${moduleId}/lessons/${lessonId}/complete`, {
        answers: []
      });
      navigate(`/courses/${courseId}`);
    } catch (error) {
      console.error('Failed to complete lesson:', error);
      alert('Error completing lesson. Please try again.');
    }
  };

  const forceShowQuiz = () => {
    if (questions.length > 0) {
      setShowQuiz(true);
    } else {
      alert('No questions available for this lesson!');
    }
  };

  if (isLoading) {
    return <div className="loading">Loading lesson...</div>;
  }

  if (!canAccess) {
    return (
      <div className="lesson-view">
        <div className="lesson-container">
          <div className="access-denied card">
            <Lock size={48} />
            <h2>Access Denied</h2>
            <p>You need to complete the previous lesson before accessing this one.</p>
            <button className="btn btn-primary" onClick={() => navigate(`/courses/${courseId}`)}>
              Back to Course
            </button>
          </div>
        </div>
      </div>
    );
  }

  if (!lesson) {
    return <div className="error">Lesson not found</div>;
  }

  if (quizCompleted) {
    return (
      <div className="lesson-view">
        <div className="lesson-container">
          <div className="completion-section">
            <div className="completion-card card">
              <CheckCircle size={48} className="success-icon" />
              <h3>Lesson Completed! 🎉</h3>
              <p>You scored {Math.round(quizScore)}% on the quiz</p>
              <button className="btn btn-primary" onClick={() => navigate(`/courses/${courseId}`)}>
                Continue to Next Lesson
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (showQuiz) {
    const currentQuestionData = questions[currentQuestion];
    
    if (!currentQuestionData) {
      return <div className="loading">Loading quiz...</div>;
    }
    
    return (
      <div className="lesson-view">
        <div className="lesson-container">
          <button className="btn-back" onClick={() => navigate(`/courses/${courseId}`)}>
            <ArrowLeft size={20} /> Back to Course
          </button>
          
          <div className="quiz-section">
            <div className="quiz-header">
              <h3>📝 Lesson Quiz</h3>
              <p>Question {currentQuestion + 1} of {questions.length}</p>
            </div>
            
            <div className="quiz-content">
              <h4>{currentQuestionData.text}</h4>
              <div className="options">
                {currentQuestionData.options?.map((option, idx) => (
                  <button
                    key={option.id}
                    className={`option-btn ${selectedAnswer === idx ? 'selected' : ''} ${answerFeedback.show && idx === currentQuestionData.correctAnswer ? 'correct' : ''} ${answerFeedback.show && selectedAnswer === idx && selectedAnswer !== currentQuestionData.correctAnswer ? 'wrong' : ''}`}
                    onClick={() => handleAnswerSelect(idx)}
                    disabled={answerFeedback.show}
                  >
                    <span className="option-letter">{String.fromCharCode(65 + idx)}.</span>
                    {option.text}
                  </button>
                ))}
              </div>
              
              {answerFeedback.show && (
                <div className={`feedback-message ${answerFeedback.isCorrect ? 'feedback-correct' : 'feedback-wrong'}`}>
                  {answerFeedback.message}
                </div>
              )}
              
              <div className="quiz-actions">
                {!answerFeedback.show ? (
                  <button 
                    className="btn btn-primary"
                    onClick={checkAnswer}
                    disabled={selectedAnswer === null}
                  >
                    Check Answer
                  </button>
                ) : (
                  <button 
                    className="btn btn-primary"
                    onClick={nextQuestion}
                  >
                    {currentQuestion + 1 < questions.length ? 'Next Question →' : 'Submit Quiz →'}
                  </button>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="lesson-view">
      <div className="lesson-container">
        <button className="btn-back" onClick={() => navigate(`/courses/${courseId}`)}>
          <ArrowLeft size={20} /> Back to Course
        </button>
        
        <div style={{ padding: '10px 20px', textAlign: 'center' }}>
          <button 
            onClick={forceShowQuiz}
            style={{
              background: '#3b82f6',
              color: 'white',
              border: 'none',
              padding: '10px 20px',
              borderRadius: '8px',
              cursor: 'pointer',
              marginBottom: '20px'
            }}
          >
            🧪 Test: Show Quiz ({questions.length} questions available)
          </button>
        </div>
        
        <div className="video-section">
          {lesson.videoUrl && lesson.videoUrl !== '' ? (
            // @ts-ignore
            <ReactPlayer
              url={lesson.videoUrl}
              width="100%"
              height="100%"
              controls
              onEnded={handleVideoComplete}
              playing={true}
            />
          ) : (
            <div className="video-placeholder">
              <div className="card">
                <Play size={48} />
                <h3>Ready to learn?</h3>
                <p>Watch the video to continue</p>
                <button className="btn btn-primary" onClick={handleVideoComplete}>
                  Mark as Completed
                </button>
              </div>
            </div>
          )}
        </div>
        
        <div className="video-info">
          <div className="video-info-content">
            <h2>{lesson.title}</h2>
            <div className="video-meta">
              <span>⏱️ {lesson.duration} minutes</span>
              {questions.length > 0 && (
                <span>📋 {questions.length} quiz {questions.length === 1 ? 'question' : 'questions'}</span>
              )}
            </div>
            <p>{lesson.description}</p>
          </div>
        </div>
      </div>
    </div>
  );
};