package com.example.paymentapi.service;

import com.example.paymentapi.dto.PostDto;
import com.example.paymentapi.dto.SearchCondition;
import com.example.paymentapi.entity.*;
import com.example.paymentapi.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CartRepository cartRepository;

    @PostConstruct
    public void init() {
        try {
            /*Member member = new Member();
            member.setUsername("daf");
            member.setNickname("anonymous");
            member.setAdress("daf");
            memberRepository.save(member);

            Cart cart = new Cart();
            cart.setMember(member);
            cartRepository.save(cart);

            Post post = new Post();
            post.setItemname("item");
            post.setCategory("extra");
            post.setPrice(40L);
            post.setQuantity(100L);
            postRepository.save(post);*/
        } catch (Exception e) {
            // 예외를 로깅하거나 처리합니다.
            System.err.println("초기화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    public Member exMember() {
            Optional<Member> result = memberRepository.findByUsername("daf");
        return result.orElse(null);
    }

    @Override
    public Cart exCart(Member member) {
        Optional<Cart> result = cartRepository.findByMember(member);
        return result.orElse(null);
    }

    @Override
    public Page<PostDto> search(SearchCondition condition, Member member) {
        PageRequest pageRequest = PageRequest.of(0, 3);
        return postRepository.searchPage(condition, pageRequest);
    }

    @Override
    public PostDto read(Long id){
        Optional<Post> result = postRepository.findById(id);
        return result.isPresent()? entityToDto(result.get()): null;
    }
}
