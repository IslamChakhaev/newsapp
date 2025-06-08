package com.example.springbootnewsportal.mapper;

import com.example.springbootnewsportal.dto.request.CommentRequestDto;
import com.example.springbootnewsportal.dto.response.CommentResponseDto;
import com.example.springbootnewsportal.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "news", ignore = true)
    Comment toEntity(CommentRequestDto dto);

    @Mapping(source = "author.username", target = "authorUsername")
    CommentResponseDto toResponseDto(Comment comment);
}

