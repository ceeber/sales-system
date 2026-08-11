package com.alex.sistema.auth.service.impl;

import com.alex.sistema.auth.dto.request.LoginRequest;
import com.alex.sistema.auth.dto.response.LoginResponse;
import com.alex.sistema.auth.dto.response.UserResponse;
import com.alex.sistema.auth.entity.User;
import com.alex.sistema.auth.mapper.UserMapper;
import com.alex.sistema.auth.repository.UserRepository;
import com.alex.sistema.auth.security.jwt.JwtService;
import com.alex.sistema.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new IllegalStateException("Usuário não encontrado"));

        String token = jwtService.generateToken(user.getEmail());

        UserResponse userResponse = userMapper.toResponse(user);

        return new LoginResponse(
                token,
                "Bearer",
                3600L,
                userResponse
        );
    }
}