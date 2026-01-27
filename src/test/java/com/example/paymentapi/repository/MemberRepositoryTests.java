package com.example.paymentapi.repository;

import com.example.paymentapi.dto.PostDto;
import com.example.paymentapi.dto.SearchCondition;
import com.example.paymentapi.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;


    @Test
    public void testEntity(){
        //consumer
        Member member = new Member();
        member.setUsername("user");
        member.setAdress("daf");
        memberRepository.save(member);
        //seller
        Member member1 = new Member();
        member1.setUsername("gun");
        member1.setAdress("daf1");
        memberRepository.save(member1);

        //item1
        Post post = new Post();
        post.setItemname("gun");
        post.setCategory("extra");
        post.setSeller(member);
        post.setPrice(1000L);
        post.setQuantity(100L);
        postRepository.save(post);

        //이거까지 given
        Order order = new Order();
        order.setMember(member);
        orderRepository.save(order);

        //최종주문로직 - order와 post가 주어졌으므로 세터로 매핑하고 orderitem만 저장해도 매핑된 나머지도 반영됨.
        OrderItem orderItem = new OrderItem();
        orderItem.setPost(post);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);

        //item2
        Post post1 = new Post();
        post1.setItemname("knife");
        post1.setCategory("extra");
        post1.setSeller(member1);
        post1.setPrice(1000L);
        post1.setQuantity(100L);
        postRepository.save(post1);

        //item3
        Post post2= new Post();
        post2.setItemname("hammer");
        post2.setCategory("extra");
        post2.setSeller(member1);
        post2.setPrice(1000L);
        post2.setQuantity(100L);
        postRepository.save(post2);

        //item4
        Post post3 = new Post();
        post3.setItemname("bat");
        post3.setCategory("extra");
        post3.setSeller(member1);
        post3.setPrice(1000L);
        post3.setQuantity(100L);
        postRepository.save(post3);

        SearchCondition condition = new SearchCondition();
        //condition.setItemname("gun");
        //condition.setSeller("gun");
        //postRepository.search(condition);
        PageRequest pageRequest = PageRequest.of(1, 3);
        Page<PostDto> result = postRepository.searchPage(condition, pageRequest);
        System.out.println("result = " + result.getContent());
        System.out.println("result = " + result.getTotalPages());
    }

    @Test
    public void testPage(){
        Member member = new Member();
        member.setUsername("daf");
        member.setAdress("daf");
        memberRepository.save(member);

        Post post = new Post();
        post.setItemname("gun");
        post.setCategory("extra");
        post.setPrice(1000L);
        post.setQuantity(100L);
        postRepository.save(post);

        Order order = new Order();
        order.setMember(member);
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setPost(post);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);
        SearchCondition condition = new SearchCondition();
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<PostDto> result = postRepository.searchPage(condition, pageRequest);
        System.out.println("result = " + result.getContent());
        System.out.println("result = " + result.getTotalPages());
    }
}
