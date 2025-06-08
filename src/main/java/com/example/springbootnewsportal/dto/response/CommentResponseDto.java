package com.example.springbootnewsportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;


@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private Instant createdAt;
    private String authorUsername;
}
