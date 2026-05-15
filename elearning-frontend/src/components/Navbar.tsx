import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { Logo } from './Logo';  // ← Importe o Logo
import { Menu, X, LogOut, BookOpen, Award, LayoutDashboard } from 'lucide-react';

export const Navbar = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
    setIsProfileOpen(false);
  };

  const navLinks = [
    { path: '/', label: 'Home' },
    { path: '/courses', label: 'Courses' },
  ];

  const loggedInLinks = [
    { path: '/dashboard', label: 'Dashboard', icon: <LayoutDashboard size={18} /> },
  ];

  return (
    <nav className="navbar">
      <div className="navbar-container">
        {/* Logo - Link para página inicial */}
        <Link to="/" className="navbar-logo">
          <Logo size={159} className=''/>  
        </Link>

        {/* Desktop Menu */}
        <div className="navbar-links">
          {navLinks.map((link) => (
            <Link key={link.path} to={link.path} className="nav-link">
              {link.label}
            </Link>
          ))}
          
          {user && loggedInLinks.map((link) => (
            <Link key={link.path} to={link.path} className="nav-link">
              {link.icon}
              {link.label}
            </Link>
          ))}
        </div>

        {/* Right Side - Auth / Profile */}
        <div className="navbar-right">
          {user ? (
            <div className="profile-container">
              <button 
                className="profile-button"
                onClick={() => setIsProfileOpen(!isProfileOpen)}
              >
                <div className="profile-avatar">
                  {user.name?.charAt(0).toUpperCase() || 'U'}
                </div>
                <span className="profile-name">{user.name?.split(' ')[0]}</span>
              </button>

              {isProfileOpen && (
                <>
                  <div className="profile-dropdown-overlay" onClick={() => setIsProfileOpen(false)} />
                  <div className="profile-dropdown">
                    <div className="dropdown-header">
                      <div className="dropdown-avatar">
                        {user.name?.charAt(0).toUpperCase() || 'U'}
                      </div>
                      <div>
                        <p className="dropdown-name">{user.name}</p>
                        <p className="dropdown-email">{user.email}</p>
                      </div>
                    </div>
                    <div className="dropdown-divider" />
                    <Link to="/dashboard" className="dropdown-item" onClick={() => setIsProfileOpen(false)}>
                      <LayoutDashboard size={18} />
                      Dashboard
                    </Link>
                    <Link to="/my-courses" className="dropdown-item" onClick={() => setIsProfileOpen(false)}>
                      <BookOpen size={18} />
                      My Courses
                    </Link>
                    <Link to="/certificates" className="dropdown-item" onClick={() => setIsProfileOpen(false)}>
                      <Award size={18} />
                      Certificates
                    </Link>
                    <div className="dropdown-divider" />
                    <button className="dropdown-item logout" onClick={handleLogout}>
                      <LogOut size={18} />
                      Logout
                    </button>
                  </div>
                </>
              )}
            </div>
          ) : (
            <div className="auth-buttons">
              <Link to="/login" className="btn-outline-nav">
                Sign In
              </Link>
              <Link to="/register" className="btn-primary-nav">
                Sign Up
              </Link>
            </div>
          )}

          {/* Mobile Menu Button */}
          <button className="mobile-menu-btn" onClick={() => setIsMenuOpen(!isMenuOpen)}>
            {isMenuOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>

        {/* Mobile Menu */}
        {isMenuOpen && (
          <div className="mobile-menu">
            <div className="mobile-menu-links">
              {navLinks.map((link) => (
                <Link key={link.path} to={link.path} className="mobile-nav-link" onClick={() => setIsMenuOpen(false)}>
                  {link.label}
                </Link>
              ))}
              {user && loggedInLinks.map((link) => (
                <Link key={link.path} to={link.path} className="mobile-nav-link" onClick={() => setIsMenuOpen(false)}>
                  {link.icon}
                  {link.label}
                </Link>
              ))}
              {!user && (
                <div className="mobile-auth-buttons">
                  <Link to="/login" className="mobile-btn-outline" onClick={() => setIsMenuOpen(false)}>
                    Sign In
                  </Link>
                  <Link to="/register" className="mobile-btn-primary" onClick={() => setIsMenuOpen(false)}>
                    Sign Up
                  </Link>
                </div>
              )}
              {user && (
                <button className="mobile-logout-btn" onClick={handleLogout}>
                  <LogOut size={18} />
                  Logout
                </button>
              )}
            </div>
          </div>
        )}
      </div>
    </nav>
  );
};