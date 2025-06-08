package com.example.springbootnewsportal.service;

import com.example.springbootnewsportal.dto.request.NewsRequestDto;
import com.example.springbootnewsportal.dto.response.NewsDetailsResponseDto;
import com.example.springbootnewsportal.dto.response.NewsResponseDto;
import org.springframework.data.domain.Page;

public interface NewsService {

    NewsResponseDto create(NewsRequestDto dto);

    Page<NewsResponseDto> findAll(int page, int size, Long authorId, Long categoryId);

    NewsDetailsResponseDto findById(Long id);

    NewsResponseDto update(Long id, NewsRequestDto dto);

    void delete(Long id);
}
