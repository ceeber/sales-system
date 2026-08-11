package com.alex.sistema.auth.controller;

import com.alex.sistema.auth.dto.response.UserResponse;
import com.alex.sistema.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                userService.findByEmail(email)
        );
    }
}