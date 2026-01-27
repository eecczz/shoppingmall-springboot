package com.example.paymentapi.repository;

import com.example.paymentapi.entity.Cart;
import com.example.paymentapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMember(Member member);
}
