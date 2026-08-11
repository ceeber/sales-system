package com.alex.sistema.auth.dto.response;

import java.util.Set;

/*/////// SEM USAR O MÉTODO "RECORD"/////////*/
public class LoginResponse {

        String accessToken;
        String tokenType;
        Long expiresIn;
        UserResponse user;

        public LoginResponse(String accessToken,
                             String tokenType,
                             Long expiresIn,
                             UserResponse user) {

            this.accessToken = accessToken;
            this.tokenType = tokenType;
            this.expiresIn = expiresIn;
            this.user = user;
        }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}