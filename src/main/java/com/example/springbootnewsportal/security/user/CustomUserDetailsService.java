package com.example.springbootnewsportal.security.user;

import com.example.springbootnewsportal.model.User;
import com.example.springbootnewsportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[AUTH] Loading user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("[AUTH] User with username '{}' not found", username);
                    return new UsernameNotFoundException(
                            MessageFormat.format("User with username: {0} is not found", username));
                });

        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(Long id) {
        log.info("[JWT] Loading user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[JWT] User with ID '{}' not found", id);
                    return new UsernameNotFoundException(
                            MessageFormat.format("User with id: {0} is not found", id));
                });

        return new CustomUserDetails(user);
    }


}
