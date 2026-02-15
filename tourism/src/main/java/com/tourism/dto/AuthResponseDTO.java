package com.tourism.dto;

public class AuthResponseDTO {

    private String token;
    private String email;

    public AuthResponseDTO(String token, String email) {
        this.token = token;
        this.email = email;
    }

    // getters

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

}

