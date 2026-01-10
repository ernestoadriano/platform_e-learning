import { useState } from 'react';
import "./Register.css";
import logo from "../../assets/logo.png";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function RegisterPage() {

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("STUDENT");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setSuccess("");

        try {
            await axios.post("http://localhost:8081/api/users", {
                name,
                email,
                password,
                role
            });
            setSuccess("You sign up with success");
            setTimeout(() => navigate("/login"), 1500);
        } catch (err) {
            if (err.response && err.response.data && err.response.data.message) {
                setError(err.response.data.message);
            } else {
                setError("Error on sign up")
            }
        }
    }

    return (
    <div className="auth-bg">
      <div className="auth-container">
          <div className="auth-header">
                            <img src={logo} alt="Logo Aprenda+" className="auth-logo" />
                            <h1 className="auth-title">E-learning</h1>
                        </div>
        <form className="auth-form" onSubmit={handleSubmit}>
          <input type="text" placeholder="Name" className="auth-input" value={name} onChange={e => setName(e.target.value)} required />
          <input type="email" placeholder="Email" className="auth-input" value={email} onChange={e => setEmail(e.target.value)} required />
          <input type="password" placeholder="Password" className="auth-input" value={password} onChange={e => setPassword(e.target.value)} required />
          <button type="submit" className="auth-btn">Sign Up</button>
        </form>
        {error && <div style={{color: "#d63031", marginTop: "1rem"}}>{error}</div>}
        {success && <div style={{color: "#36b37e", marginTop: "1rem"}}>{success}</div>}
      </div>
    </div>
  );
}