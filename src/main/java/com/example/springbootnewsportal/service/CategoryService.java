package com.example.springbootnewsportal.service;

import com.example.springbootnewsportal.dto.request.CategoryRequestDto;
import com.example.springbootnewsportal.dto.response.CategoryResponseDto;
import org.springframework.data.domain.Page;

public interface CategoryService {

    CategoryResponseDto create(CategoryRequestDto dto);

    Page<CategoryResponseDto> findAll(int page, int size);

    CategoryResponseDto findById(Long id);

    CategoryResponseDto update(Long id, CategoryRequestDto dto);

    void delete(Long id);
}
