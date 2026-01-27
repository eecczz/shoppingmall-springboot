import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './css/Signin.css'; // Importing the CSS file

const Signin = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post('/demo/signin', {
                username: username,
                password: password,
            });

            if (response.data.success) {
                navigate(response.data.redirectUrl); // Redirect on successful login
            } else {
                setError(response.data.message); // Set error message on failure
            }
        } catch (error) {
            setError('Login failed. Please check your credentials and try again.');
        }
    };

    return (
        <div className="container">
            <h2>Login</h2>
            {error && <div className="error-message">{error}</div>} {/* Show error message if any */}
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    <label htmlFor="username">Username</label>
                    <input
                        type="text"
                        className="form-control"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Enter your username"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Enter your password"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="rememberMe">
                        <input type="checkbox" id="rememberMe" />
                        Remember Me
                    </label>
                </div>
                <button type="submit" className="btn">Login</button>
            </form>
            <div className="form-group text-center">
                <a href="#">Forgot your password?</a><br />
                <a href="/demo/signup">Don't have an account? Sign up</a>
            </div>
        </div>
    );
};

export default Signin;
