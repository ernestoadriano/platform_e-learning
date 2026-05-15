import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
//import { useAuth } from '../contexts/AuthContext';
import { Mail, Lock, Eye, EyeOff, LogIn } from 'lucide-react';

export const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  //const { login } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    document.title = 'Login - E-Learning Platform';
  }, []);

  /*const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    if (!email || !password) {
      setError('Please fill in all fields');
      setIsLoading(false);
      return;
    }

    if (!email.includes('@')) {
      setError('Please enter a valid email address');
      setIsLoading(false);
      return;
    }

    try {
      await login(email, password);
      navigate('/dashboard');
    } catch (err) {
      setError('Invalid email or password. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };*/

  return (
    <div className="auth-page">
      <div className="auth-container two-columns">
        {/* Info Section - Esquerda */}
        <div className="auth-info">
          <div className="auth-info-content">
            <h3>Why join us?</h3>
            <ul>
              <li>✓ Access to 500+ expert-led courses</li>
              <li>✓ Earn recognized certificates</li>
              <li>✓ Learn at your own pace</li>
              <li>✓ Join a global community of learners</li>
              <li>✓ Get personalized course recommendations</li>
              <li>✓ 24/7 access to all content</li>
            </ul>
            
            <div className="auth-testimonial">
              <p>"This platform transformed my career! The structured learning path and quizzes helped me master new skills quickly."</p>
              <div className="testimonial-author">
                <strong>Jane Doe</strong>
                <span>Software Engineer</span>
              </div>
            </div>
          </div>
        </div>

        {/* Login Form - Direita */}
        <div className="auth-card">
          <div className="auth-form-wrapper">
            <div className="auth-header">
              <div className="auth-logo">
                <LogIn size={32} />
              </div>
              <h2>Welcome Back</h2>
              <p>Sign in to continue your learning journey</p>
            </div>

            <form className="auth-form">
              <div className="form-group">
                <label htmlFor="email">Email Address</label>
                <div className="input-icon">
                  <Mail size={18} />
                  <input
                    type="email"
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="johndoe@example.com"
                    required
                    disabled={isLoading}
                    autoComplete="email"
                  />
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="password">Password</label>
                <div className="input-icon">
                  <Lock size={18} />
                  <input
                    type={showPassword ? 'text' : 'password'}
                    id="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="••••••••"
                    required
                    disabled={isLoading}
                    autoComplete="current-password"
                  />
                  <button
                    type="button"
                    className="toggle-password"
                    onClick={() => setShowPassword(!showPassword)}
                    tabIndex={-1}
                  >
                    {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                  </button>
                </div>
              </div>

              <div className="form-options">
                <label className="checkbox-label">
                  <input type="checkbox" />
                  <span>Remember me</span>
                </label>
                <Link to="/forgot-password" className="forgot-link">
                  Forgot Password?
                </Link>
              </div>

              {error && <div className="error-message">{error}</div>}

              <button type="submit" className="btn btn-primary auth-submit" disabled={isLoading}>
                {isLoading ? (
                  <>
                    <span className="spinner"></span>
                    Signing in...
                  </>
                ) : (
                  'Sign In'
                )}
              </button>
            </form>

            <div className="auth-footer">
              <p>
                Don't have an account? <Link to="/register">Create Account</Link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};