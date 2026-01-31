package com.example.springbootnewsportal.service.impl;

import com.example.springbootnewsportal.aop.annotation.CheckCommentOwnership;
import com.example.springbootnewsportal.aop.universal.Authorize;
import com.example.springbootnewsportal.aop.universal.EntityType;
import com.example.springbootnewsportal.dto.request.CommentRequestDto;
import com.example.springbootnewsportal.dto.response.CommentResponseDto;
import com.example.springbootnewsportal.model.Comment;
import com.example.springbootnewsportal.model.News;
import com.example.springbootnewsportal.model.User;
import com.example.springbootnewsportal.exception.ResourceNotFoundException;
import com.example.springbootnewsportal.mapper.CommentMapper;
import com.example.springbootnewsportal.repository.CommentRepository;
import com.example.springbootnewsportal.repository.NewsRepository;
import com.example.springbootnewsportal.repository.UserRepository;

import com.example.springbootnewsportal.security.scopes.AuthenticatedUser;
import com.example.springbootnewsportal.service.api.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final AuthenticatedUser authenticatedUser;

    @Override
    @Authorize(
            roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"}
    )
    @Transactional
    public CommentResponseDto create(Long newsId, CommentRequestDto dto) {
        Long userId = authenticatedUser.getUserId();

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", userId));

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News", newsId));

        Comment comment = commentMapper.toEntity(dto);
        comment.setAuthor(author);
        comment.setNews(news);

        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponseDto(saved);
    }

    @Override
    @Authorize(
            roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"}
    )
    public List<CommentResponseDto> findByNewsId(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News", newsId));

        return news.getCommentList().stream()
                .map(commentMapper::toResponseDto)
                .toList();
    }

    @Override
    @Authorize(
            roles = {"ROLE_ADMIN", "ROLE_MODERATOR"},
            checkOwnership = true,
            entity = EntityType.COMMENT,
            idParam = "commentId"
    )
    @Transactional
    public CommentResponseDto update(Long commentId, CommentRequestDto dto) {
        Comment comment = findByIdOrThrow(commentId);
        comment.setContent(dto.getContent());

        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponseDto(saved);
    }

    @Override
    @Authorize(
            roles = {"ROLE_ADMIN", "ROLE_MODERATOR"},
            checkOwnership = true,
            entity = EntityType.COMMENT,
            idParam = "commentId"
    )
    @Transactional
    public void delete(Long commentId) {
        findByIdOrThrow(commentId);
        commentRepository.deleteById(commentId);
    }

    public Comment findByIdOrThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", id));
    }
}
