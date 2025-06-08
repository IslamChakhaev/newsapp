package com.example.springbootnewsportal.aop;

import com.example.springbootnewsportal.entity.Comment;
import com.example.springbootnewsportal.entity.News;
import com.example.springbootnewsportal.exception.OwnershipViolationException;
import com.example.springbootnewsportal.exception.ResourceNotFoundException;
import com.example.springbootnewsportal.repository.CommentRepository;
import com.example.springbootnewsportal.repository.NewsRepository;
import com.example.springbootnewsportal.scopes.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class OwnershipCheckAspect {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final AuthenticatedUser authenticatedUser;

    @Before("@annotation(CheckNewsOwnership)")
    public void checkNewsOwner(JoinPoint joinPoint) {
        Long newsId = (Long) joinPoint.getArgs()[0]; // предполагаем, что ID — первый аргумент
        Long currentUserId = authenticatedUser.getUserId();

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News", newsId));

        if (!news.getAuthor().getId().equals(currentUserId)) {
            throw new OwnershipViolationException("You are not allowed to modify news with ID " + newsId);
        }
    }

    @Before("@annotation(CheckCommentOwnership)")
    public void checkCommentOwner(JoinPoint joinPoint) {
        Long commentId = (Long) joinPoint.getArgs()[0]; // предполагаем, что ID — первый аргумент
        Long currentUserId = authenticatedUser.getUserId();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));

        if (!comment.getAuthor().getId().equals(currentUserId)) {
            throw new OwnershipViolationException("You are not allowed to modify comment with ID " + commentId);
        }
    }
}
