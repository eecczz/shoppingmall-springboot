package com.example.paymentapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="POST")
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "POST_ID")
    private Long id;
    private String title;
    private String itemname;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member seller;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<SavedItem> savedItems = new ArrayList<>();
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    private Long price;
    private Long quantity;
    private String category;

    public void setSeller(Member seller){
        this.seller = seller;
        seller.getPosts().add(this);
    }
}
