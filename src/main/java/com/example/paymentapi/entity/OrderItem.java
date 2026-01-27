package com.example.paymentapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="ORDER_ITEM")
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private Long quantity;
    private Long price;

    public void setOrder(Order order){
        this.order = order;
        order.getOrderItems().add(this);
    }

    public void setPost(Post post){
        this.post = post;
        post.getOrderItems().add(this);
    }
}
