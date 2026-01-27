package com.example.paymentapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="MEMBER")
public class Member {

    @Id
    @GeneratedValue
    @Column(name="MEMBER_ID")
    private Long id;
    private String nickname;
    private String username;
    private String userpw;
    //private enum isSeller;
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
    private String adress;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Cart cart;
}
