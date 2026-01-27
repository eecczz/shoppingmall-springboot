package com.example.paymentapi.repository;

import com.example.paymentapi.dto.PostDto;
import com.example.paymentapi.dto.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostDto> search(SearchCondition condition);
    Page<PostDto> searchPage(SearchCondition condition, Pageable pageable);
}
