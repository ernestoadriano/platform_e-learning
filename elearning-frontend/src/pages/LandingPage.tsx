import { useNavigate } from 'react-router-dom';
import { BookOpen, Award, Users, Clock, ChevronRight } from 'lucide-react';
import { useEffect } from 'react';

export const LandingPage = () => {

  useEffect(() => {
    document.title = 'E-Learning Platform';
  }, []);
  const navigate = useNavigate();

  const features = [
    { icon: <BookOpen />, title: 'Expert Courses', description: 'Learn from industry professionals with years of experience' },
    { icon: <Award />, title: 'Certificates', description: 'Earn recognized certificates upon completion' },
    { icon: <Users />, title: 'Community', description: 'Join a community of passionate learners' },
    { icon: <Clock />, title: 'Learn at Your Pace', description: 'Access content 24/7, learn whenever you want' },
  ];

  const stats = [
    { value: '50K+', label: 'Active Students' },
    { value: '200+', label: 'Expert Instructors' },
    { value: '500+', label: 'Courses' },
    { value: '95%', label: 'Completion Rate' },
  ];

  return (
    <div className="landing-page">
      <section className="hero">
        <div className="container">
          <div className="hero-content fade-in">
            <h1 className="hero-title">
              Master New Skills with
              <span className="gradient-text"> E-Learning Platform</span>
            </h1>
            <p className="hero-subtitle">
              Join thousands of students learning cutting-edge skills from industry experts.
              Get certified and advance your career today.
            </p>
            <div className="hero-buttons">
              <button className="btn btn-primary" onClick={() => navigate('/register')}>
                Start Learning Free <ChevronRight size={18} />
              </button>
              <button className="btn btn-secondary" onClick={() => navigate('/courses')}>
                Browse Courses
              </button>
            </div>
            <div className="hero-stats">
              {stats.map((stat, idx) => (
                <div key={idx} className="stat-item">
                  <span className="stat-value">{stat.value}</span>
                  <span className="stat-label">{stat.label}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      <section className="features">
        <div className="container">
          <h2 className="section-title">Why Choose Us</h2>
          <div className="features-grid">
            {features.map((feature, idx) => (
              <div key={idx} className="feature-card card">
                <div className="feature-icon">{feature.icon}</div>
                <h3>{feature.title}</h3>
                <p>{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
};