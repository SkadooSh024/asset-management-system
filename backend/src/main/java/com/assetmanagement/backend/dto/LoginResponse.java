package com.assetmanagement.backend.dto;


public class LoginResponse {

    private boolean success;

        private String message;

        private UserSessionResponse user;

    public LoginResponse() {
        }

    public LoginResponse(boolean success, String message, UserSessionResponse user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }

    public boolean getSuccess() {
            return success;
        }

    public void setSuccess(boolean success) {
            this.success = success;
        }

    public String getMessage() {
            return message;
        }

    public void setMessage(String message) {
            this.message = message;
        }

    public UserSessionResponse getUser() {
            return user;
        }

    public void setUser(UserSessionResponse user) {
            this.user = user;
        }

    public static LoginResponseBuilder builder() {
            return new LoginResponseBuilder();
        }

    public static class LoginResponseBuilder {
            private boolean success;
            private String message;
            private UserSessionResponse user;

            public LoginResponseBuilder success(boolean success) {
                this.success = success;
                return this;
            }

            public LoginResponseBuilder message(String message) {
                this.message = message;
                return this;
            }

            public LoginResponseBuilder user(UserSessionResponse user) {
                this.user = user;
                return this;
            }

            public LoginResponse build() {
                return new LoginResponse(success, message, user);
            }
        }
}
