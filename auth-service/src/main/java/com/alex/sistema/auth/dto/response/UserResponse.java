package com.alex.sistema.auth.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(

        Long id,
        String name,
        String email,
        Set<String> roles,
        Boolean active,
        LocalDateTime createdAt
) {
}