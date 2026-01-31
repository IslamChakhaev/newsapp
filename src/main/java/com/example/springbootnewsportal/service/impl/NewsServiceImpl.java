package com.example.springbootnewsportal.service.impl;

import com.example.springbootnewsportal.aop.annotation.CheckNewsOwnership;
import com.example.springbootnewsportal.aop.universal.Authorize;
import com.example.springbootnewsportal.aop.universal.EntityType;
import com.example.springbootnewsportal.dto.request.NewsRequestDto;
import com.example.springbootnewsportal.dto.response.NewsDetailsResponseDto;
import com.example.springbootnewsportal.dto.response.NewsResponseDto;
import com.example.springbootnewsportal.model.Category;
import com.example.springbootnewsportal.model.News;
import com.example.springbootnewsportal.model.User;
import com.example.springbootnewsportal.exception.ResourceNotFoundException;
import com.example.springbootnewsportal.mapper.NewsMapper;
import com.example.springbootnewsportal.repository.CategoryRepository;
import com.example.springbootnewsportal.repository.NewsRepository;
import com.example.springbootnewsportal.repository.UserRepository;
import com.example.springbootnewsportal.repository.spec.NewsSpecification;
import com.example.springbootnewsportal.security.scopes.AuthenticatedUser;
import com.example.springbootnewsportal.service.api.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AuthenticatedUser authenticatedUser;

    /**
     * Создание новой новости.
     * Сначала проверяем, что существует автор и категория, затем сохраняем новость.
     */
    @Override
    @Authorize(
            roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"}
    )
    @Transactional
    public NewsResponseDto create(NewsRequestDto dto) {
        // Получаем ID текущего пользователя из запроса (через RequestScope)
        Long userId = authenticatedUser.getUserId();

        // Получаем объект автора по его ID
        // Если автор не найден — выбрасываем исключение
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", userId));

        // получаем категорию по её ID из запроса и тоже выбрасываем исключение, если не найдено
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", dto.getCategoryId()));

        // Преобразуем NewsRequestDto в сущность News
        News news = newsMapper.toEntity(dto);

        news.setAuthor(author);
        news.setCategory(category);

        // Сохраняем новость в базу данных
        News saved = newsRepository.save(news);

        // Возвращаем NewsResponseDto
        return newsMapper.toResponseDto(saved);
    }

    @Override
    @Authorize(
            roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"}
    )
    public Page<NewsResponseDto> findAll(int page, int size, Long authorId, Long categoryId) {
        Specification<News> spec = Specification.allOf(
                NewsSpecification.hasAuthor(authorId),
                NewsSpecification.hasCategory(categoryId)
        );

        Page<News> newsPage = newsRepository.findAll(spec, PageRequest.of(page, size));

        return newsPage.map(newsMapper::toResponseDto);
    }


    @Override
    @Authorize(
            roles = {"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR"}
    )
    public NewsDetailsResponseDto findById(Long id) {
        // Получаем новость по ID, если не существует — бросаем исключение
        News news = findByIdOrThrow(id);

        // Маппим сущность в DTO с деталями (включая, например, автора и комментарии)
        return newsMapper.toResponseDtoInDetails(news);
    }

    /**
     * Обновление новости.
     * Метод защищён через AOP — только владелец может выполнить.
     */
    @Override
    @Authorize(
            roles = {"ROLE_ADMIN", "ROLE_MODERATOR"},
            checkOwnership = true,
            entity = EntityType.NEWS,
            idParam = "id"
    )
    @Transactional
    public NewsResponseDto update(Long id, NewsRequestDto dto) {
        // Проверяем, существует ли новость с таким ID или выбрасываем исключение
        News news = findByIdOrThrow(id);

        // Проверяем, существует ли новая категория переданная через NewsRequestDto
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", dto.getCategoryId()));

        // Обновляем содержимое новости, беря данные из NewsRequestDto
        news.setText(dto.getText());
        news.setTitle(dto.getTitle());
        news.setCategory(category); // меняем категорию
        news.setUpdatedAt(Instant.now());

        // Сохраняем изменения в базу
        News saved = newsRepository.save(news);

        // Возвращаем обновлённую версию как DTO
        return newsMapper.toResponseDto(saved);
    }

    /**
     * Удаление новости по ID.
     * Только автор может удалить (проверка через AOP).
     */
    @Override
    @Authorize(
            roles = {"ROLE_ADMIN", "ROLE_MODERATOR"},
            checkOwnership = true,
            entity = EntityType.NEWS,
            idParam = "id"
    )
    @Transactional
    public void delete(Long id) {
        // Проверяем есть ли новость в бд или выбрасываем исключение
        findByIdOrThrow(id);
        // это выполнится, если не было исключения
        newsRepository.deleteById(id);
    }

    /**
     * Вспомогательный метод: получить новость или выбросить исключение.
     */
    public News findByIdOrThrow(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News", id));
    }
}
