import React, { useEffect, useState } from 'react';
import './css/Cart.css'; // 필요한 스타일을 여기에 추가할 수 있습니다.
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import axios from 'axios';

const Cart = () => {
    const [cartItems, setCartItems] = useState([]); // 기본적으로 빈 배열로 초기화
    const [totalPrice, setTotalPrice] = useState(0);
    const [loading, setLoading] = useState(true); // 로딩 상태 추가
    const [error, setError] = useState(null); // 에러 상태 추가

    useEffect(() => {
        // 서버에서 장바구니에 저장된 항목을 가져옴
        axios.get('/api/cart')
            .then(response => {
                setCartItems(response.data.savedItems || []); // 데이터가 없으면 빈 배열로 초기화
                calculateTotalPrice(response.data.savedItems || []); // 데이터가 없으면 빈 배열로 처리
                setLoading(false); // 로딩 종료
            })
            .catch(error => {
                console.error('Error fetching cart data:', error);
                setError(error); // 에러 상태 설정
                setLoading(false); // 로딩 종료
            });
    }, []);

    // 수량 변경 시 호출되는 함수
    const handleQuantityChange = (itemId, newQuantity) => {
        if (newQuantity < 1) {
            alert('Quantity must be 1 or more.');
            return;
        }

        // 서버에 수량 업데이트 요청
        axios.post('/api/updatecart', { itemId, quantity: newQuantity })
            .then(() => {
                const updatedItems = cartItems.map(item =>
                    item.id === itemId ? { ...item, quantity: newQuantity } : item
                );
                setCartItems(updatedItems);
                calculateTotalPrice(updatedItems);
            })
            .catch(error => {
                console.error('Failed to update quantity:', error);
                alert('Failed to update quantity. Please try again.');
            });
    };

    // 삭제 버튼 클릭 시 호출되는 함수
    const handleDeleteItem = (itemId) => {
        axios.post('/api/deletecart', { itemId })
            .then(() => {
                const updatedItems = cartItems.filter(item => item.id !== itemId);
                setCartItems(updatedItems);
                calculateTotalPrice(updatedItems);
                alert('Item deleted successfully!');
            })
            .catch(error => {
                console.error('Failed to delete item:', error);
                alert('Failed to delete item. Please try again.');
            });
    };

    // 총합계 계산 함수
    const calculateTotalPrice = (items) => {
        let total = 0;
        items.forEach(item => {
            total += item.post.price * item.quantity;
        });
        setTotalPrice(total);
    };

    const handleCheckout = () => {
        if (totalPrice === 0) {
            alert('Your cart is empty. Please add items to your cart before checking out.');
            return;
        }

        const userNickname = "anonymous"; // 실제로는 서버에서 사용자 정보를 가져와야 합니다.
        if (userNickname === "anonymous") {
            window.location.href = '/demo/signin';
        } else {
            window.location.href = "/demo/purchase";
        }
    };

    // 로딩 상태 처리
    if (loading) {
        return <div>Loading...</div>;
    }

    // 에러 발생 시 처리
    if (error) {
        return <div>Error: {error.message}</div>;
    }

    return (
        <div>
            <section className="py-5">
                <div className="container px-4 px-lg-5 my-5">
                    {cartItems.length === 0 ? (
                        <div>No items in your cart.</div>
                    ) : (
                        cartItems.map(item => (
                            <div className="row gx-4 gx-lg-5 align-items-center mb-5" key={item.id}>
                                <div className="col-md-6" style={{ maxWidth: "200px" }}>
                                    <img className="card-img-top mb-5 mb-md-0" src="https://dummyimage.com/200x250/dee2e6/6c757d.jpg" alt={item.post.itemname} style={{ maxWidth: "100%" }} />
                                </div>
                                <div className="col-md-6" style={{ maxWidth: "300px", whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>
                                    <div className="small mb-1" style={{ fontSize: "0.8rem" }}>SKU: BST-498</div>
                                    <h1 className="display-6 fw-bolder" style={{ fontSize: "1.5rem" }}>{item.post.itemname}</h1>
                                    <div className="fs-5 mb-3">
                                        <span className="text-decoration-line-through" style={{ fontSize: "1rem" }}>${(item.post.price * item.quantity).toFixed(2)}</span>
                                        <span className="price" style={{ fontSize: "1rem" }} data-unit-price={item.post.price}>${(item.post.price * item.quantity).toFixed(2)}</span>
                                    </div>
                                    <div className="d-flex">
                                        <input className="form-control text-center me-3 quantity-input"
                                               type="number"
                                               value={item.quantity}
                                               min="1"
                                               onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value))}
                                               style={{ maxWidth: "3rem" }}
                                        />
                                        <button className="btn deleteCart" type="button" onClick={() => handleDeleteItem(item.id)}
                                                style={{ backgroundColor: "black", color: "white", border: "none", padding: "0.5rem 1rem", display: "flex", alignItems: "center" }}>
                                            <i className="bi-trash" style={{ fontSize: "1.2rem", marginRight: "0.5rem" }}></i>
                                            Delete
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                    <div className="row gx-4 gx-lg-5 justify-content-end align-items-center">
                        <div className="col-md-4 text-end">
                            <h2 className="text-end" id="totalPrice">Total: ${totalPrice.toFixed(2)}</h2>
                        </div>
                        <div className="col-md-2 text-end">
                            <button id="checkoutButton" className="btn btn-outline-dark d-flex align-items-center justify-content-center" type="button" onClick={handleCheckout} style={{ whiteSpace: "nowrap" }}>
                                <i className="bi-credit-card-fill me-2"></i>
                                Checkout
                            </button>
                        </div>
                    </div>
                </div>
            </section>

            <footer className="py-5 bg-dark">
                <div className="container"><p className="m-0 text-center text-white">Copyright &copy; Your Website 2023</p></div>
            </footer>
        </div>
    );
};

export default Cart;
