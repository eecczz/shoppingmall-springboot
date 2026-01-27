package com.example.paymentapi.repository;

import com.example.paymentapi.dto.PostDto;
import com.example.paymentapi.dto.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.paymentapi.entity.QMember.member;
import static com.example.paymentapi.entity.QPost.post;
import static org.springframework.util.StringUtils.hasText;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    public PostRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    public List<PostDto> search(SearchCondition condition){
        List<PostDto> result = queryFactory
                .select(Projections.bean(PostDto.class,
                        post.id, post.title, post.itemname, member.nickname))
                .from(post)
                .leftJoin(post.seller, member)
                .where(titleEq(condition.getTitle()),
                        itemnameEq(condition.getItemname()),
                        sellerEq(condition.getSeller()))
                .fetch();

        return result;
    }

    public Page<PostDto> searchPage(SearchCondition condition, Pageable pageable){
        BooleanBuilder builder = new BooleanBuilder();

        if(hasText(condition.getItemname())){
            builder.or(post.itemname.eq(condition.getItemname()));
        }
        if(hasText(condition.getSeller())){
            builder.or(member.nickname.eq(condition.getSeller()));
        }
        QueryResults<PostDto> results = queryFactory
                .select(Projections.bean(PostDto.class,
                        post.id, post.title, post.itemname, post.price, post.quantity, member.nickname.as("seller")))
                .from(post)
                .leftJoin(post.seller, member)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<PostDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    private BooleanExpression titleEq(String title){
        return hasText(title) ? post.title.eq(title) : null;
    }

    private BooleanExpression itemnameEq(String itemname){
        return hasText(itemname) ? post.itemname.eq(itemname) : null;
    }

    private BooleanExpression sellerEq(String seller){
        return hasText(seller) ? member.nickname.eq(seller) : null;
    }
}
