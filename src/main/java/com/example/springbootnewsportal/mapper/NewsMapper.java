package com.example.springbootnewsportal.mapper;

import com.example.springbootnewsportal.dto.request.NewsRequestDto;
import com.example.springbootnewsportal.dto.response.NewsDetailsResponseDto;
import com.example.springbootnewsportal.dto.response.NewsResponseDto;
import com.example.springbootnewsportal.model.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "commentList", ignore = true)
    News toEntity(NewsRequestDto dto);

    @Mapping(source = "author.username", target = "authorUsername")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "commentsCount", expression = "java(news.getCommentList() != null ? news.getCommentList().size() : 0)")
    NewsResponseDto toResponseDto(News news);

    @Mapping(source = "author.username", target = "authorUsername")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "commentList", target = "comments")
    NewsDetailsResponseDto toResponseDtoInDetails(News news);
}

