package com.example.paymentapi.repository;

import com.example.paymentapi.entity.Cart;
import com.example.paymentapi.entity.SavedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedItemRepository extends JpaRepository<SavedItem, Long> {
    List<SavedItem> findByCart(Cart cart);
}
