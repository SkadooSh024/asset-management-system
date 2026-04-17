package com.assetmanagement.backend.dto;


public class UserReferenceResponse {

    private Long id;

        private Long departmentId;

        private String username;

        private String fullName;

        private String roleCode;

        private String departmentName;

    public UserReferenceResponse() {
        }

    public UserReferenceResponse(Long id, Long departmentId, String username, String fullName, String roleCode, String departmentName) {
            this.id = id;
            this.departmentId = departmentId;
            this.username = username;
            this.fullName = fullName;
            this.roleCode = roleCode;
            this.departmentName = departmentName;
        }

    public Long getId() {
            return id;
        }

    public void setId(Long id) {
            this.id = id;
        }

    public Long getDepartmentId() {
            return departmentId;
        }

    public void setDepartmentId(Long departmentId) {
            this.departmentId = departmentId;
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

    public String getRoleCode() {
            return roleCode;
        }

    public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

    public String getDepartmentName() {
            return departmentName;
        }

    public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

    public static UserReferenceResponseBuilder builder() {
            return new UserReferenceResponseBuilder();
        }

    public static class UserReferenceResponseBuilder {
            private Long id;
            private Long departmentId;
            private String username;
            private String fullName;
            private String roleCode;
            private String departmentName;

            public UserReferenceResponseBuilder id(Long id) {
                this.id = id;
                return this;
            }

            public UserReferenceResponseBuilder departmentId(Long departmentId) {
                this.departmentId = departmentId;
                return this;
            }

            public UserReferenceResponseBuilder username(String username) {
                this.username = username;
                return this;
            }

            public UserReferenceResponseBuilder fullName(String fullName) {
                this.fullName = fullName;
                return this;
            }

            public UserReferenceResponseBuilder roleCode(String roleCode) {
                this.roleCode = roleCode;
                return this;
            }

            public UserReferenceResponseBuilder departmentName(String departmentName) {
                this.departmentName = departmentName;
                return this;
            }

            public UserReferenceResponse build() {
                return new UserReferenceResponse(id, departmentId, username, fullName, roleCode, departmentName);
            }
        }
}
