import React from "react";
import './Login.css'
import  logo from "../../assets/logo.png";


export default function LoginPage() {
   return (
    <div className="auth-bg">
        <div className="auth-container">
            <div className="auth-header">
                <img src={logo} alt="Logo" className="auth-logo" />
                <h1 className="auth-title">E-Learning</h1>
            </div>
            <form className="auth-form">
                <input type="email" className="auth-input" placeholder="Email" value="" required/>
                <input type="password" className="auth-input" placeholder="Password" value="" required/>
                <button type="submit" className="auth-btn">Sign In</button>
            </form>
        </div>
    </div>
   );
}