package com.example.springbootnewsportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequestDto {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Text must not be blank")
    private String text;

    @NotNull(message = "Category ID must not be null")
    private Long categoryId;
}
