package com.alex.sistema.auth.service;

import com.alex.sistema.auth.dto.request.RegisterRequest;
import com.alex.sistema.auth.dto.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

    UserResponse findByEmail(String email);

}