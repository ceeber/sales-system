package com.alex.sistema.auth.service.impl;

import com.alex.sistema.auth.dto.request.LoginRequest;
import com.alex.sistema.auth.dto.response.LoginResponse;
import com.alex.sistema.auth.dto.response.UserResponse;
import com.alex.sistema.auth.entity.User;
import com.alex.sistema.auth.mapper.UserMapper;
import com.alex.sistema.auth.repository.UserRepository;
import com.alex.sistema.auth.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void deveRealizarLoginComSucesso() {

        LoginRequest request = new LoginRequest(
                "alex@email.com",
                "123456"
        );

        User user = User.builder()
                .id(1L)
                .name("Alex")
                .email("alex@email.com")
                .active(true)
                .build();

        UserResponse userResponse = new UserResponse(
                1L,
                "Alex",
                "alex@email.com",
                Set.of("ROLE_USER"),
                true,
                LocalDateTime.now()
        );

        when(userRepository.findByEmail("alex@email.com"))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken("alex@email.com"))
                .thenReturn("jwt-token");

        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        LoginResponse result =
                authService.login(request);

        assertNotNull(result);

        assertEquals(
                "jwt-token",
                result.getAccessToken()
        );

        assertEquals(
                "Bearer",
                result.getTokenType()
        );

        assertEquals(
                3600L,
                result.getExpiresIn()
        );

        assertEquals(
                "alex@email.com",
                result.getUser().email()
        );

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(userRepository)
                .findByEmail("alex@email.com");

        verify(jwtService)
                .generateToken("alex@email.com");

        verify(userMapper)
                .toResponse(user);
    }

    @Test
    void deveRejeitarLoginComCredenciaisInvalidas() {

        // Arrange

        LoginRequest request = new LoginRequest(
                "alex@email.com",
                "senha-errada"
        );

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenThrow(
                new BadCredentialsException("Credenciais inválidas")
        );

        // Act + Assert

        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(request)
        );

        // Verifica que não continuou o processamento

        verify(authenticationManager)
                .authenticate(
                        any(UsernamePasswordAuthenticationToken.class)
                );

        verify(userRepository, never())
                .findByEmail(anyString());

        verify(jwtService, never())
                .generateToken(anyString());

        verify(userMapper, never())
                .toResponse(any(User.class));
    }
}