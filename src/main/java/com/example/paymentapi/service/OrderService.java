package com.example.paymentapi.service;

import com.example.paymentapi.entity.Member;
import com.example.paymentapi.entity.Post;
import com.example.paymentapi.entity.SavedItem;

import java.util.List;

public interface OrderService {
    void updateOrder(Long orderId, String status);
    void purchase(Member member);
}
