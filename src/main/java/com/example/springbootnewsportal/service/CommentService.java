package com.example.springbootnewsportal.service;

import com.example.springbootnewsportal.dto.request.CommentRequestDto;
import com.example.springbootnewsportal.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto create(Long newsId, CommentRequestDto dto);

    List<CommentResponseDto> findByNewsId(Long newsId);

    CommentResponseDto update(Long commentId, CommentRequestDto dto);

    void delete(Long commentId);

}
