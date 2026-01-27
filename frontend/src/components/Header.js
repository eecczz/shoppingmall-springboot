import React from 'react';
import { Link } from 'react-router-dom'; // 페이지 이동을 위한 react-router-dom 사용
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

const Header = ({ member, cartCount }) => {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container px-4 px-lg-5">
                <Link className="navbar-brand" to="/demo/list">Start Bootstrap</Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
                        <li className="nav-item">
                            <Link className="nav-link active" to="/">Home</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/about">About</Link>
                        </li>
                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">Shop</a>
                            <ul className="dropdown-menu" aria-labelledby="navbarDropdown">
                                <li><Link className="dropdown-item" to="/products">All Products</Link></li>
                                <li><hr className="dropdown-divider" /></li>
                                <li><Link className="dropdown-item" to="/popular">Popular Items</Link></li>
                                <li><Link className="dropdown-item" to="/new-arrivals">New Arrivals</Link></li>
                            </ul>
                        </li>
                    </ul>
                    <div className="d-flex">
                        {/* Member is not anonymous */}
                        {member && member.nickname !== 'anonymous' ? (
                            <div className="member-info">
                                <span>Hello, {member.nickname}</span>
                                <form action="/demo/logout" method="post">
                                    <button className="btn btn-outline-dark" type="submit">
                                        <i className="bi-box-arrow-right me-1"></i>
                                        Logout
                                    </button>
                                </form>
                            </div>
                        ) : (
                            <div>
                                <Link to="/demo/signin" style={{ textDecoration: 'none' }}>
                                    <button className="btn btn-outline-dark" type="button">
                                        <i className="bi-box-arrow-in-right me-1"></i>
                                        Sign In
                                    </button>
                                </Link>
                            </div>
                        )}

                        {/* Cart for anonymous or logged-in user */}
                        <Link to={member && member.nickname !== 'anonymous' ? '/demo/cart' : '/demo/signin'} style={{ textDecoration: 'none', marginLeft: '10px' }}>
                            <button className="btn btn-outline-dark" type="submit">
                                <i className="bi-cart-fill me-1"></i>
                                Cart
                                {member && member.nickname !== 'anonymous' && (
                                    <span id="cart-count" className="badge bg-dark text-white ms-1 rounded-pill">{cartCount}</span>
                                )}
                            </button>
                        </Link>
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Header;
