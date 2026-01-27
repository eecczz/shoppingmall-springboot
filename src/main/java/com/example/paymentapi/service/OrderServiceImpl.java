package com.example.paymentapi.service;

import com.example.paymentapi.dto.PostDto;
import com.example.paymentapi.entity.*;
import com.example.paymentapi.repository.CartRepository;
import com.example.paymentapi.repository.OrderItemRepository;
import com.example.paymentapi.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;

    @Transactional
    public void updateOrder(Long orderId, String status){
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()){
            order.get().setStatus(Status.valueOf(status));
        }
    }

    @Transactional
    public void purchase(Member member){
        Optional<Cart> cart = cartRepository.findByMember(member);
        if(cart.isPresent()) {
            Cart result = cart.get();
            List<SavedItem> savedItems= result.getSavedItems();
            Order order = new Order();
            order.setMember(member);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            for (SavedItem savedItem : savedItems) {
                orderItem.setPost(savedItem.getPost());
                orderItem.setPrice(savedItem.getPrice());
                orderItem.setQuantity(savedItem.getQuantity());
            }
            orderItemRepository.save(orderItem);
        }
    }
}
