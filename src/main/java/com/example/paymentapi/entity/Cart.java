package com.example.paymentapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue
    @Column(name = "CART_ID")
    private Long id;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<SavedItem> savedItems = new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        member.setCart(this);
    }
}
