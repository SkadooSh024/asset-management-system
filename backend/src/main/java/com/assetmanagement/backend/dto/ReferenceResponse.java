package com.assetmanagement.backend.dto;


public class ReferenceResponse {

    private Long id;

        private String code;

        private String name;

    public ReferenceResponse() {
        }

    public ReferenceResponse(Long id, String code, String name) {
            this.id = id;
            this.code = code;
            this.name = name;
        }

    public Long getId() {
            return id;
        }

    public void setId(Long id) {
            this.id = id;
        }

    public String getCode() {
            return code;
        }

    public void setCode(String code) {
            this.code = code;
        }

    public String getName() {
            return name;
        }

    public void setName(String name) {
            this.name = name;
        }

    public static ReferenceResponseBuilder builder() {
            return new ReferenceResponseBuilder();
        }

    public static class ReferenceResponseBuilder {
            private Long id;
            private String code;
            private String name;

            public ReferenceResponseBuilder id(Long id) {
                this.id = id;
                return this;
            }

            public ReferenceResponseBuilder code(String code) {
                this.code = code;
                return this;
            }

            public ReferenceResponseBuilder name(String name) {
                this.name = name;
                return this;
            }

            public ReferenceResponse build() {
                return new ReferenceResponse(id, code, name);
            }
        }
}
