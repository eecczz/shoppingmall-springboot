// signin.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './App.css'; // 스타일 파일을 유지하거나 필요에 따라 변경하세요.

const SignIn = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        // 로그인 API 호출 (추후 Spring Boot와 연동)
        fetch('http://localhost:8080/demo/signin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password, rememberMe }),
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    // 로그인 성공 시 홈으로 리다이렉트
                    navigate('/');
                } else {
                    alert('Login failed: ' + data.message);
                }
            })
            .catch((error) => console.error('Login error:', error));
    };

    return (
        <div className="container">
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Username</label>
                    <input
                        type="text"
                        className="form-control"
                        id="username"
                        name="username"
                        placeholder="Enter your username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        id="password"
                        name="password"
                        placeholder="Enter your password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="rememberMe">
                        <input
                            type="checkbox"
                            id="rememberMe"
                            name="rememberMe"
                            checked={rememberMe}
                            onChange={() => setRememberMe(!rememberMe)}
                        />
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

export default SignIn;
