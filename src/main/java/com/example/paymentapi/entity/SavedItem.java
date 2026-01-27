package com.example.paymentapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class SavedItem {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;
    private Long quantity;
    private Long price;
    public void setCart(Cart cart){
        this.cart = cart;
        cart.getSavedItems().add(this);
    }

    public void setPost(Post post){
        this.post = post;
        post.getSavedItems().add(this);
    }
}
