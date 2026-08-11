package com.alex.sistema.auth.service.impl;

import com.alex.sistema.auth.dto.request.RegisterRequest;
import com.alex.sistema.auth.dto.response.UserResponse;
import com.alex.sistema.auth.entity.Role;
import com.alex.sistema.auth.entity.User;
import com.alex.sistema.auth.exception.ResourceNotFoundException;
import com.alex.sistema.auth.mapper.UserMapper;
import com.alex.sistema.auth.repository.RoleRepository;
import com.alex.sistema.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import com.alex.sistema.auth.exception.BusinessException;
import java.util.Set;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        // Inicialização dos mocks feita pelo MockitoExtension
    }

    @Test
    void deveCadastrarUsuario() {

        RegisterRequest request = new RegisterRequest(
                "João",
                "joao@email.com",
                "123456"
        );

        Role role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        User user = User.builder()
                .name("João")
                .email("joao@email.com")
                .password("senha-criptografada")
                .active(true)
                .build();

        User savedUser = User.builder()
                .id(10L)
                .name("João")
                .email("joao@email.com")
                .password("senha-criptografada")
                .active(true)
                .build();

        UserResponse response = new UserResponse(
                10L,
                "João",
                "joao@email.com",
                Set.of("ROLE_USER"),
                true,
                null
        );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(java.util.Optional.of(role));

        when(passwordEncoder.encode(request.password()))
                .thenReturn("senha-criptografada");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        when(userMapper.toResponse(savedUser))
                .thenReturn(response);

        UserResponse result =
                userService.register(request);


        assertNotNull(result);
        assertEquals("joao@email.com", result.email());

        verify(userRepository)
                .existsByEmail("joao@email.com");

        verify(passwordEncoder)
                .encode("123456");

        verify(userRepository)
                .save(any(User.class));

        verify(userMapper)
                .toResponse(savedUser);
    }

    @Test
    void naoDeveCadastrarEmailDuplicado() {

        RegisterRequest request = new RegisterRequest(
                "João",
                "joao@email.com",
                "123456"
        );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);

        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () -> userService.register(request)
                );

        assertEquals(
                "Email já cadastrado",
                exception.getMessage()
        );

        verify(userRepository)
                .existsByEmail("joao@email.com");

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    void deveCriptografarSenha() {

        RegisterRequest request = new RegisterRequest(
                "Maria",
                "maria@email.com",
                "123456"
        );

        Role role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        User savedUser = User.builder()
                .id(11L)
                .name("Maria")
                .email("maria@email.com")
                .password("HASH_BCRYPT")
                .active(true)
                .build();

        UserResponse response = new UserResponse(
                11L,
                "Maria",
                "maria@email.com",
                Set.of("ROLE_USER"),
                true,
                null
        );

        when(userRepository.existsByEmail("maria@email.com"))
                .thenReturn(false);

        when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(java.util.Optional.of(role));

        when(passwordEncoder.encode("123456"))
                .thenReturn("HASH_BCRYPT");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        when(userMapper.toResponse(savedUser))
                .thenReturn(response);

        userService.register(request);

        verify(passwordEncoder)
                .encode("123456");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {

        when(userRepository.findByEmail("naoexiste@email.com"))
                .thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> userService.findByEmail(
                                "naoexiste@email.com"
                        )
                );

        assertEquals(
                "Usuário não encontrado",
                exception.getMessage()
        );

        verify(userRepository)
                .findByEmail("naoexiste@email.com");

        verify(userMapper, never())
                .toResponse(any(User.class));
    }
}