package com.alex.sistema.auth.mapper;

import com.alex.sistema.auth.dto.response.UserResponse;
import com.alex.sistema.auth.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        var roles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roles,
                user.getActive(),
                user.getCreatedAt()
        );
    }
}