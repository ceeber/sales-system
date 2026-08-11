package com.alex.sistema.auth.service;

import com.alex.sistema.auth.dto.request.LoginRequest;
import com.alex.sistema.auth.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}