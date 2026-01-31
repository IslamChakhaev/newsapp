package com.example.springbootnewsportal.controller;

import com.example.springbootnewsportal.dto.request.NewsRequestDto;
import com.example.springbootnewsportal.dto.response.NewsDetailsResponseDto;
import com.example.springbootnewsportal.dto.response.NewsResponseDto;
import com.example.springbootnewsportal.service.api.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    // Создание новой новости
    @PostMapping
    public ResponseEntity<NewsResponseDto> createNews(@Valid @RequestBody NewsRequestDto dto) {
        NewsResponseDto response = newsService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Получение списка новостей
    @GetMapping
    public ResponseEntity<Page<NewsResponseDto>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long categoryId) {

        Page<NewsResponseDto> newsPage = newsService.findAll(page, size, authorId, categoryId);
        return ResponseEntity.ok(newsPage);
    }


    // Получение одной новости
    @GetMapping("/{id}")
    public ResponseEntity<NewsDetailsResponseDto> getNewsById(@PathVariable Long id) {
        NewsDetailsResponseDto response = newsService.findById(id);
        return ResponseEntity.ok(response);
    }

    // Обновление новости
    @PutMapping("/{id}")
    public ResponseEntity<NewsResponseDto> updateNews(
            @PathVariable Long id,
            @Valid @RequestBody NewsRequestDto dto) {
        NewsResponseDto response = newsService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    // Удаление новости
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
