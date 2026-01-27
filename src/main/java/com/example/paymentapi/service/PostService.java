package com.example.paymentapi.service;

import com.example.paymentapi.dto.PostDto;
import com.example.paymentapi.dto.SearchCondition;
import com.example.paymentapi.entity.Cart;
import com.example.paymentapi.entity.Member;
import com.example.paymentapi.entity.Post;
import com.example.paymentapi.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostService {
    void init();
    Member exMember();
    Cart exCart(Member member);
    Page<PostDto> search(SearchCondition condition, Member member);
    PostDto read(Long pno);

    default PostDto entityToDto(Post entity){

        PostDto dto  = PostDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .itemname(entity.getItemname())
                //.seller(entity.getSeller().getUsername())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .build();

        return dto;
    }

    default Post dtoToEntity(PostDto postDto){

        Post entity  = Post.builder()
                .id(postDto.getId())
                .title(postDto.getTitle())
                .itemname(postDto.getItemname())
                .quantity(postDto.getQuantity())
                .price(postDto.getPrice())
                //.seller(postDto.getSeller().getUsername())
                .build();

        return entity;
    }
}
