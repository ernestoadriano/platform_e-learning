import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { api } from '../services/api';
import { Mail, ArrowLeft, CheckCircle } from 'lucide-react';

export const ForgotPassword = () => {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const navigate = useNavigate();

  const validateEmail = () => {
    if (!email) {
      setError('Please enter your email address');
      return false;
    }
    if (!email.includes('@') || !email.includes('.')) {
      setError('Please enter a valid email address');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!validateEmail()) return;

    setIsLoading(true);
    try {
      await api.post('/auth/forgot-password', { email });
      setIsSubmitted(true);
    } catch (err: any) {
      setIsSubmitted(true);
    } finally {
      setIsLoading(false);
    }
  };

  if (isSubmitted) {
    return (
      <div className="auth-page">
        <div className="auth-container one-column">
          <div className="auth-card centered fade-in">
            <div className="auth-header">
              <div className="auth-logo">
                <CheckCircle size={40} className="text-success" />
              </div>
              <h2>Check Your Email</h2>
              <p>
                We've sent password reset instructions to:<br />
                <strong style={{ color: 'var(--accent)' }}>{email}</strong>
              </p>
            </div>

            <div className="success-message" style={{ marginTop: 'var(--spacing-md)' }}>
              If an account exists with this email, you will receive a password reset link shortly.
            </div>

            <div className="auth-footer" style={{ marginTop: 'var(--spacing-xl)' }}>
              <Link to="/login">
                <ArrowLeft size={16} />
                Back to Sign In
              </Link>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-page">
      <div className="auth-container one-column">
        <div className="auth-card centered fade-in">
          <div className="auth-header">
            <div className="auth-logo">
              <Mail size={32} />
            </div>
            <h2>Forgot Password?</h2>
            <p>Enter your email address and we'll send you a link to reset your password.</p>
          </div>

          <form onSubmit={handleSubmit} className="auth-form">
            <div className="form-group">
              <label htmlFor="email">Email Address</label>
              <div className="input-icon">
                <Mail size={16} />
                <input
                  type="email"
                  id="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="johndoe@example.com"
                  required
                  disabled={isLoading}
                  autoComplete="email"
                  autoFocus
                />
              </div>
            </div>

            {error && <div className="error-message">{error}</div>}

            <button type="submit" className="btn btn-primary auth-submit" disabled={isLoading}>
              {isLoading ? (
                <>
                  <span className="spinner"></span>
                  Sending...
                </>
              ) : (
                'Send Reset Link'
              )}
            </button>

            <div className="auth-footer">
              <Link to="/login">
                <ArrowLeft size={14} />
                Back to Sign In
              </Link>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};