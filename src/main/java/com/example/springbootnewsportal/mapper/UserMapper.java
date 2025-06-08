package com.example.springbootnewsportal.mapper;

import com.example.springbootnewsportal.dto.request.UserRequestDto;
import com.example.springbootnewsportal.dto.response.UserResponseDto;
import com.example.springbootnewsportal.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "newsList", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User toEntity(UserRequestDto dto);

    UserResponseDto toResponseDto(User user);
}

