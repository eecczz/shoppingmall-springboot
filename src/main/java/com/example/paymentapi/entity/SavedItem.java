package com.example.paymentapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    public void setPost(Post post) {
        // 기존 연관관계 끊기(중복/꼬임 방지)
        if (this.post != null) {
            this.post.getSavedItems().remove(this);
        }

        this.post = post;

        if (post != null) {
            if (post.getSavedItems() == null) { // 혹시 모를 방어(초기화 했으면 사실 필요없음)
                // 초기화가 안 돼있을 경우 대비
                 post.setSavedItems(new ArrayList<>()); // setter 있을 때
            }
            if (!post.getSavedItems().contains(this)) { // 중복 방지
                post.getSavedItems().add(this);
            }
        }
    }

}
