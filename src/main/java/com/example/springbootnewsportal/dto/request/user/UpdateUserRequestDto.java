    package com.example.springbootnewsportal.dto.request.user;

    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.Size;
    import lombok.*;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class UpdateUserRequestDto {

        @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
        private String username;

        @Email(message = "Email must be valid")
        private String email;
    }
