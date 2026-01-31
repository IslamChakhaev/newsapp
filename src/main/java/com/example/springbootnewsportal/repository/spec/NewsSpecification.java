package com.example.springbootnewsportal.repository.spec;

import com.example.springbootnewsportal.model.News;
import org.springframework.data.jpa.domain.Specification;

public class NewsSpecification {
    public static Specification<News> hasAuthor(Long authorId) {
        return (root, query, cb) ->
                authorId == null ? null : cb.equal(root.get("author").get("id"), authorId);
    }


    public static Specification<News> hasCategory(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null? null : cb.equal(root.get("category").get("id"), categoryId);
    }

}
