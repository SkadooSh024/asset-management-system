package com.assetmanagement.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class LogoutRequest {

    @NotBlank(message = "Ten dang nhap khong duoc de trong.")
        private String username;

    public LogoutRequest() {
        }

    public LogoutRequest(String username) {
            this.username = username;
        }

    public String getUsername() {
            return username;
        }

    public void setUsername(String username) {
            this.username = username;
        }
}
