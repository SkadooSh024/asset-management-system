package com.assetmanagement.backend.dto;


public class UserSessionResponse {

    private Long userId;

        private String username;

        private String fullName;

        private String email;

        private String status;

        private String roleCode;

        private String roleName;

        private String departmentName;

    public UserSessionResponse() {
        }

    public UserSessionResponse(Long userId, String username, String fullName, String email, String status, String roleCode, String roleName, String departmentName) {
            this.userId = userId;
            this.username = username;
            this.fullName = fullName;
            this.email = email;
            this.status = status;
            this.roleCode = roleCode;
            this.roleName = roleName;
            this.departmentName = departmentName;
        }

    public Long getUserId() {
            return userId;
        }

    public void setUserId(Long userId) {
            this.userId = userId;
        }

    public String getUsername() {
            return username;
        }

    public void setUsername(String username) {
            this.username = username;
        }

    public String getFullName() {
            return fullName;
        }

    public void setFullName(String fullName) {
            this.fullName = fullName;
        }

    public String getEmail() {
            return email;
        }

    public void setEmail(String email) {
            this.email = email;
        }

    public String getStatus() {
            return status;
        }

    public void setStatus(String status) {
            this.status = status;
        }

    public String getRoleCode() {
            return roleCode;
        }

    public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

    public String getRoleName() {
            return roleName;
        }

    public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

    public String getDepartmentName() {
            return departmentName;
        }

    public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

    public static UserSessionResponseBuilder builder() {
            return new UserSessionResponseBuilder();
        }

    public static class UserSessionResponseBuilder {
            private Long userId;
            private String username;
            private String fullName;
            private String email;
            private String status;
            private String roleCode;
            private String roleName;
            private String departmentName;

            public UserSessionResponseBuilder userId(Long userId) {
                this.userId = userId;
                return this;
            }

            public UserSessionResponseBuilder username(String username) {
                this.username = username;
                return this;
            }

            public UserSessionResponseBuilder fullName(String fullName) {
                this.fullName = fullName;
                return this;
            }

            public UserSessionResponseBuilder email(String email) {
                this.email = email;
                return this;
            }

            public UserSessionResponseBuilder status(String status) {
                this.status = status;
                return this;
            }

            public UserSessionResponseBuilder roleCode(String roleCode) {
                this.roleCode = roleCode;
                return this;
            }

            public UserSessionResponseBuilder roleName(String roleName) {
                this.roleName = roleName;
                return this;
            }

            public UserSessionResponseBuilder departmentName(String departmentName) {
                this.departmentName = departmentName;
                return this;
            }

            public UserSessionResponse build() {
                return new UserSessionResponse(userId, username, fullName, email, status, roleCode, roleName, departmentName);
            }
        }
}
