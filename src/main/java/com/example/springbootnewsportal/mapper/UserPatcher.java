package com.example.springbootnewsportal.mapper;

import com.example.springbootnewsportal.dto.request.user.UpdateUserRequestDto;
import com.example.springbootnewsportal.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserPatcher {

    public User patch(User existing, UpdateUserRequestDto patch) {

        if(StringUtils.hasText(patch.getUsername())) {
            existing.setUsername(patch.getUsername());
        }

        if(StringUtils.hasText(patch.getEmail())) { //
            existing.setEmail(patch.getEmail());
        }

        return existing;
    }
}
