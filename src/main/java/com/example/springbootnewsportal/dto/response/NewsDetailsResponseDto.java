package com.example.springbootnewsportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;


@Getter
@AllArgsConstructor
public class NewsDetailsResponseDto {
    private Long id;
    private String title;
    private String text;
    private Instant createdAt;
    private Instant updatedAt;
    private String authorUsername;
    private String categoryName;
    private List<CommentResponseDto> comments;
}

