package com.example.paymentapi.repository;

import com.example.paymentapi.entity.Member;
import com.example.paymentapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByMember(Member member);
}
