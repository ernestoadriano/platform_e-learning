import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
//import { useAuth } from '../contexts/AuthContext';
import { Mail, Lock, User, Eye, EyeOff, UserPlus } from 'lucide-react';

export const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  //const { register } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    document.title = 'Register - E-Learning Platform';
  }, []);

  const validateForm = () => {
    if (!name || !email || !password || !confirmPassword) {
      setError('Please fill in all fields');
      return false;
    }
    
    if (name.length < 2) {
      setError('Name must be at least 2 characters');
      return false;
    }
    
    if (!email.includes('@') || !email.includes('.')) {
      setError('Please enter a valid email address');
      return false;
    }
    
    if (password.length < 6) {
      setError('Password must be at least 6 characters');
      return false;
    }
    
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return false;
    }
    
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumbers = /\d/.test(password);
    
    if (!hasUpperCase || !hasLowerCase || !hasNumbers) {
      setError('Password must contain uppercase, lowercase and number');
      return false;
    }
    
    return true;
  };

  /*const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!validateForm()) return;

    setIsLoading(true);
    try {
      await register(name, email, password);
      navigate('/dashboard');
    } catch (err) {
      setError('Registration failed. Email may already be in use.');
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
            <h3>Benefits of joining</h3>
            <ul>
              <li>✓ Free access to introductory courses</li>
              <li>✓ Track your learning progress</li>
              <li>✓ Get personalized recommendations</li>
              <li>✓ Join study groups and forums</li>
              <li>✓ Earn certificates upon completion</li>
              <li>✓ Access to exclusive content</li>
            </ul>
            
            <div className="auth-stats">
              <div className="stat">
                <span className="stat-value">50K+</span>
                <span className="stat-label">Active Students</span>
              </div>
              <div className="stat">
                <span className="stat-value">500+</span>
                <span className="stat-label">Courses</span>
              </div>
              <div className="stat">
                <span className="stat-value">95%</span>
                <span className="stat-label">Success Rate</span>
              </div>
            </div>
          </div>
        </div>

        {/* Register Form - Direita */}
        <div className="auth-card">
          <div className="auth-form-wrapper">
            <div className="auth-header">
              <div className="auth-logo">
                <UserPlus size={32} />
              </div>
              <h2>Create Account</h2>
              <p>Start your learning journey today</p>
            </div>

            <form className="auth-form">
              <div className="form-group">
                <label htmlFor="name">Full Name</label>
                <div className="input-icon">
                  <User size={18} />
                  <input
                    type="text"
                    id="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="John Doe"
                    required
                    disabled={isLoading}
                    autoComplete="name"
                  />
                </div>
              </div>

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
                    autoComplete="new-password"
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
                <div className="password-strength">
                  <div className="strength-bars">
                    <div className={`strength-bar ${password.length >= 6 ? 'active' : ''}`}></div>
                    <div className={`strength-bar ${/[A-Z]/.test(password) ? 'active' : ''}`}></div>
                    <div className={`strength-bar ${/[a-z]/.test(password) ? 'active' : ''}`}></div>
                    <div className={`strength-bar ${/\d/.test(password) ? 'active' : ''}`}></div>
                  </div>
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="confirmPassword">Confirm Password</label>
                <div className="input-icon">
                  <Lock size={18} />
                  <input
                    type={showConfirmPassword ? 'text' : 'password'}
                    id="confirmPassword"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    placeholder="••••••••"
                    required
                    disabled={isLoading}
                    autoComplete="new-password"
                  />
                  <button
                    type="button"
                    className="toggle-password"
                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                    tabIndex={-1}
                  >
                    {showConfirmPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                  </button>
                </div>
              </div>

              <div className="form-options">
                <label className="checkbox-label">
                  <input type="checkbox" required />
                  <span>I agree to the <Link to="/terms">Terms</Link> and <Link to="/privacy">Privacy</Link></span>
                </label>
              </div>

              {error && <div className="error-message">{error}</div>}

              <button type="submit" className="btn btn-primary auth-submit" disabled={isLoading}>
                {isLoading ? (
                  <>
                    <span className="spinner"></span>
                    Creating...
                  </>
                ) : (
                  'Create Account'
                )}
              </button>
            </form>

            <div className="auth-footer">
              <p>
                Already have an account? <Link to="/login">Sign In</Link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};