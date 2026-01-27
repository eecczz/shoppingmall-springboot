package com.example.paymentapi.service;

import com.example.paymentapi.entity.*;
import com.example.paymentapi.repository.CartRepository;
import com.example.paymentapi.repository.SavedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final OrderService orderService;
    private final CartRepository cartRepository;
    private final SavedItemRepository savedItemRepository;
    @Override
    @Transactional
    public void insert(Cart cart, Post post, Long quantity){
        SavedItem savedItem = new SavedItem();
        savedItem.setCart(cart);
        savedItem.setPost(post);
        savedItem.setQuantity(quantity);
        savedItem.setPrice(post.getPrice()*quantity);
        savedItemRepository.save(savedItem);
    }

    @Override
    @Transactional
    public void delete(Cart cart, Long savedItemId) {
        SavedItem savedItem = cart.getSavedItems().stream()
                .filter(item -> item.getId().equals(savedItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("SavedItem not found"));

        cart.getSavedItems().remove(savedItem);
        savedItemRepository.delete(savedItem);
    }

    @Override
    @Transactional
    public void update(Cart cart, Long savedItemId, Long newQuantity) {
        SavedItem savedItem = cart.getSavedItems().stream()
                .filter(item -> item.getId().equals(savedItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("SavedItem not found"));

        savedItem.setQuantity(newQuantity); // 수량 업데이트
        savedItem.setPrice(savedItem.getPost().getPrice()*newQuantity);
    }

    @Override
    @Transactional
    public void deleteAll(Cart cart) {
        List<SavedItem> savedItems = savedItemRepository.findByCart(cart);
        for (SavedItem savedItem : savedItems) {
            cart.getSavedItems().remove(savedItem);
            savedItemRepository.delete(savedItem);
        }
    }

    @Override
    @Transactional
    public void purchaseAll(Member member) {
        Optional<Cart> cart = cartRepository.findByMember(member);
        if(cart.isPresent()) {
            Cart result = cart.get();
            orderService.purchase(member);
            deleteAll(result);
        }
    }
}
