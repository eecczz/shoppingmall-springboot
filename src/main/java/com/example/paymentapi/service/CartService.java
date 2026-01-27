package com.example.paymentapi.service;

import com.example.paymentapi.entity.Cart;
import com.example.paymentapi.entity.Member;
import com.example.paymentapi.entity.Post;

public interface CartService {
    void insert(Cart cart, Post post, Long quantity);
    void delete(Cart cart, Long savedItemId);
    void update(Cart cart, Long savedItemId, Long newQuantity);
    void deleteAll(Cart cart);
    void purchaseAll(Member member);
}
