package com.example.springbootnewsportal.service.impl;

import com.example.springbootnewsportal.dto.request.CategoryRequestDto;
import com.example.springbootnewsportal.dto.response.CategoryResponseDto;
import com.example.springbootnewsportal.entity.Category;
import com.example.springbootnewsportal.exception.ResourceNotFoundException;
import com.example.springbootnewsportal.mapper.CategoryMapper;
import com.example.springbootnewsportal.repository.CategoryRepository;
import com.example.springbootnewsportal.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    @Transactional
    public CategoryResponseDto create(CategoryRequestDto dto) {
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);

        return categoryMapper.toResponseDto(saved);
    }

    @Override
    public Page<CategoryResponseDto> findAll(int page, int size) {
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(page, size));
        return categories.map(categoryMapper::toResponseDto);
    }


    @Override
    public CategoryResponseDto findById(Long id) {
        Category category = findByIdOrThrow(id);

        return categoryMapper.toResponseDto(category) ;
    }

    @Override
    @Transactional
    public CategoryResponseDto update(Long id, CategoryRequestDto dto) {
        Category category = findByIdOrThrow(id);

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        Category saved = categoryRepository.save(category);

        return categoryMapper.toResponseDto(saved);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        findByIdOrThrow(id);
        categoryRepository.deleteById(id);
    }

    public Category findByIdOrThrow(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }
}
