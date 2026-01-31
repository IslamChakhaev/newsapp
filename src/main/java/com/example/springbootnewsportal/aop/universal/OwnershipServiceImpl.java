package com.example.springbootnewsportal.aop.universal;

import com.example.springbootnewsportal.repository.CommentRepository;
import com.example.springbootnewsportal.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnershipServiceImpl implements OwnershipService {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;

    @Override
    public boolean isOwner(EntityType entity, Long entityId, Long userId) {

        return switch (entity) {

            case USER ->
                    userId.equals(entityId);

            case NEWS ->
                    newsRepository.findById(entityId)
                            .map(news -> news.getAuthor().getId().equals(userId))
                            .orElse(false);

            case COMMENT ->
                    commentRepository.findById(entityId)
                            .map(comment -> comment.getAuthor().getId().equals(userId))
                            .orElse(false);

            default -> false;
        };
    }
}
