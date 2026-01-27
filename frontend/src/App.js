import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ListPage from './components/List'; // ListPage 컴포넌트 import
import Header from './components/Header';
import ItemPage from './components/Item';
import CartPage from './components/Cart';
import SigninPage from './components/Signin';
import SignupPage from './components/Signup';
import PurchasePage from './components/Purchase';
import OrderListPage from './components/OrderList';
import OrderListTotalPage from './components/OrderListTotal';

function App() {
  return (
      <BrowserRouter>
        <div className='App'>
          {/* 공통 헤더 */}
          <Header />
          {/* 각 경로에 따른 페이지 렌더링 */}
          <Routes>
            {/* 상품 리스트 페이지 */}
            <Route path="/demo/list" element={ <ListPage /> } />
            {/* 상품 상세 페이지 (동적 라우팅) */}
            <Route path="/demo/item-detail/:id" element={ <ItemPage /> } />
            {/* 장바구니 페이지 */}
            <Route path="/demo/cart" element={ <CartPage /> } />
            {/* 로그인 페이지 */}
            <Route path="/demo/signin" element={ <SigninPage /> } />
            {/* 회원가입 페이지 */}
            <Route path="/demo/signup" element={ <SignupPage /> } />
            {/* 구매 페이지 */}
            <Route path="/demo/purchase" element={ <PurchasePage /> } />
            {/* 주문 목록 페이지 */}
            <Route path="/demo/orderlist" element={ <OrderListPage /> } />
            {/* 전체 주문 목록 페이지 */}
            <Route path="/demo/orderlisttotal" element={ <OrderListTotalPage /> } />
          </Routes>
        </div>
      </BrowserRouter>
  );
}

export default App;
