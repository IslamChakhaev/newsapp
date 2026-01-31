package com.example.springbootnewsportal.controller;

import com.example.springbootnewsportal.dto.request.CommentRequestDto;
import com.example.springbootnewsportal.dto.response.CommentResponseDto;
import com.example.springbootnewsportal.service.api.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news/{newsId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getByNewsId(@PathVariable Long newsId) {
        return ResponseEntity.ok(commentService.findByNewsId(newsId));
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> create(
            @PathVariable Long newsId,
            @Valid @RequestBody CommentRequestDto dto) {
        CommentResponseDto response = commentService.create(newsId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> update(
            @PathVariable("newsId") Long newsId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.update(commentId, dto));
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable("newsId") Long newsId,
            @PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

}
