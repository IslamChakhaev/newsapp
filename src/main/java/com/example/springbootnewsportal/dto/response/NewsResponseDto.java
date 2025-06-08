package com.example.springbootnewsportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;


@Getter
@AllArgsConstructor
public class NewsResponseDto {
    private Long id;
    private String title;
    private String text;
    private Instant createdAt;
    private Instant updatedAt;
    private String authorUsername;
    private String categoryName;
    private int commentsCount;
}
