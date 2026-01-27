import React, { useEffect, useState } from 'react';
import './css/List.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

const List = () => {
    const [data, setData] = useState([]); // 상품 데이터를 저장
    const [loading, setLoading] = useState(true); // 로딩 상태 저장
    const [error, setError] = useState(null); // 에러 상태 저장
    const [totalPages, setTotalPages] = useState(0); // 총 페이지 수 저장
    const [cartCount, setCartCount] = useState(0); // 장바구니 상품 개수 저장

    useEffect(() => {
        fetch('http://localhost:8080/api/list')  // API 경로
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 실패했습니다.');
                }
                return response.json(); // JSON 형식으로 변환
            })
            .then(data => {
                setData(data.posts); // 상품 데이터를 설정
                setTotalPages(data.totalPages); // 페이지 수 설정
                setCartCount(data.cartCount); // 장바구니 상품 개수 설정
                setLoading(false); // 로딩 상태 해제
            })
            .catch(error => {
                console.error('데이터 로딩 중 오류:', error);
                setError(error); // 에러 상태 설정
                setLoading(false); // 로딩 상태 해제
            });
    }, []);

    if (loading) {
        return <div>Loading...</div>; // 로딩 중일 때 표시
    }

    if (error) {
        return <div>Error: {error.message}</div>; // 에러 발생 시 표시
    }

    return (
        <div>
            {/* 네비게이션 바 */}
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="container px-4 px-lg-5">
                    <a className="navbar-brand" href="#!">Start Bootstrap</a>
                    <div className="d-flex">
                        <a id="cart" href="/demo/cart" style={{ textDecoration: 'none', marginLeft: '10px' }}>
                            <button className="btn btn-outline-dark" type="submit">
                                <i className="bi-cart-fill me-1"></i> Cart
                                <span className="badge bg-dark text-white ms-1 rounded-pill">{cartCount}</span>
                            </button>
                        </a>
                    </div>
                </div>
            </nav>

            {/* 메인 섹션 */}
            <section className="py-5">
                <div className="container px-4 px-lg-5 mt-5">
                    <div className="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
                        {data.map(item => (
                            <div className="col mb-5" key={item.id}>
                                <div className="card h-100">
                                    <img className="card-img-top" src={item.imageUrl || 'https://dummyimage.com/450x300/dee2e6/6c757d.jpg'} alt={item.name} />
                                    <div className="card-body p-4">
                                        <div className="text-center">
                                            <h5 className="fw-bolder">{item.name}</h5>
                                            <span>${item.price}</span>
                                        </div>
                                    </div>
                                    <div className="card-footer p-4 pt-0 border-top-0 bg-transparent">
                                        <div className="text-center">
                                            <a className="btn btn-outline-dark mt-auto" href="#">View options</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* 페이지 네비게이션 */}
                    <nav aria-label="Page navigation example">
                        <ul className="pagination justify-content-center">
                            {[...Array(totalPages)].map((_, i) => (
                                <li className="page-item" key={i}>
                                    <a className="page-link" href="#" data-page={i}>{i + 1}</a>
                                </li>
                            ))}
                        </ul>
                    </nav>
                </div>
            </section>
        </div>
    );
}

export default List;
