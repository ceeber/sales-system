package com.alex.sistema.auth.service.impl;

import com.alex.sistema.auth.dto.request.RegisterRequest;
import com.alex.sistema.auth.dto.response.UserResponse;
import com.alex.sistema.auth.entity.Role;
import com.alex.sistema.auth.entity.User;
import com.alex.sistema.auth.mapper.UserMapper;
import com.alex.sistema.auth.repository.RoleRepository;
import com.alex.sistema.auth.repository.UserRepository;
import com.alex.sistema.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alex.sistema.auth.exception.BusinessException;
import com.alex.sistema.auth.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new ResourceNotFoundException("ROLE_USER não encontrada"));

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(new java.util.HashSet<>())
                .build();

        user.getRoles().add(roleUser);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário não encontrado"
                        ));

        return userMapper.toResponse(user);
    }
}