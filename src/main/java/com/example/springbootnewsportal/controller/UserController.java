    package com.example.springbootnewsportal.controller;

    import com.example.springbootnewsportal.dto.request.user.ChangePasswordRequestDto;
    import com.example.springbootnewsportal.dto.request.user.UpdateUserRequestDto;
    import com.example.springbootnewsportal.dto.response.UserResponseDto;
    import com.example.springbootnewsportal.service.api.UserService;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/users")
    @RequiredArgsConstructor
    public class UserController {

        private final UserService userService;

        @GetMapping
        public ResponseEntity<Page<UserResponseDto>> findAll(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            Page<UserResponseDto> users = userService.findAll(page, size);
            return ResponseEntity.ok(users);
        }


        @GetMapping("/{id}")
        public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
            UserResponseDto responseDto = userService.findById(id);
            return ResponseEntity.ok(responseDto);
        }



        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }



        @PatchMapping("/{id}")
        public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequestDto dto) {
            UserResponseDto response = userService.update(id, dto);
            return ResponseEntity.ok(response);
        }


        // 4. POST /api/users/{id}/change-password
        @PostMapping("/{id}")
        public ResponseEntity<Void> changePassword(
                @PathVariable Long id,
                @RequestBody @Valid ChangePasswordRequestDto dto) {

            userService.changePassword(id, dto);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
