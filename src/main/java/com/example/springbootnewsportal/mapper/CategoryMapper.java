package com.example.springbootnewsportal.mapper;

import com.example.springbootnewsportal.dto.request.CategoryRequestDto;
import com.example.springbootnewsportal.dto.response.CategoryResponseDto;
import com.example.springbootnewsportal.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "newsList", ignore = true)
    Category toEntity(CategoryRequestDto dto);

    CategoryResponseDto toResponseDto(Category category);
}
