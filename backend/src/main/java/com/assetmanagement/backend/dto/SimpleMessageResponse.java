package com.assetmanagement.backend.dto;


public class SimpleMessageResponse {

    private boolean success;

        private String message;

    public SimpleMessageResponse() {
        }

    public SimpleMessageResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
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

    public static SimpleMessageResponseBuilder builder() {
            return new SimpleMessageResponseBuilder();
        }

    public static class SimpleMessageResponseBuilder {
            private boolean success;
            private String message;

            public SimpleMessageResponseBuilder success(boolean success) {
                this.success = success;
                return this;
            }

            public SimpleMessageResponseBuilder message(String message) {
                this.message = message;
                return this;
            }

            public SimpleMessageResponse build() {
                return new SimpleMessageResponse(success, message);
            }
        }
}
