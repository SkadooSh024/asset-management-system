package com.assetmanagement.backend.dto;

import java.util.List;


public class LookupBundleResponse {

    private List<ReferenceResponse> departments;

        private List<UserReferenceResponse> users;

        private List<ReferenceResponse> assetCategories;

        private List<ReferenceResponse> assetStatuses;

    public LookupBundleResponse() {
        }

    public LookupBundleResponse(List<ReferenceResponse> departments, List<UserReferenceResponse> users, List<ReferenceResponse> assetCategories, List<ReferenceResponse> assetStatuses) {
            this.departments = departments;
            this.users = users;
            this.assetCategories = assetCategories;
            this.assetStatuses = assetStatuses;
        }

    public List<ReferenceResponse> getDepartments() {
            return departments;
        }

    public void setDepartments(List<ReferenceResponse> departments) {
            this.departments = departments;
        }

    public List<UserReferenceResponse> getUsers() {
            return users;
        }

    public void setUsers(List<UserReferenceResponse> users) {
            this.users = users;
        }

    public List<ReferenceResponse> getAssetCategories() {
            return assetCategories;
        }

    public void setAssetCategories(List<ReferenceResponse> assetCategories) {
            this.assetCategories = assetCategories;
        }

    public List<ReferenceResponse> getAssetStatuses() {
            return assetStatuses;
        }

    public void setAssetStatuses(List<ReferenceResponse> assetStatuses) {
            this.assetStatuses = assetStatuses;
        }

    public static LookupBundleResponseBuilder builder() {
            return new LookupBundleResponseBuilder();
        }

    public static class LookupBundleResponseBuilder {
            private List<ReferenceResponse> departments;
            private List<UserReferenceResponse> users;
            private List<ReferenceResponse> assetCategories;
            private List<ReferenceResponse> assetStatuses;

            public LookupBundleResponseBuilder departments(List<ReferenceResponse> departments) {
                this.departments = departments;
                return this;
            }

            public LookupBundleResponseBuilder users(List<UserReferenceResponse> users) {
                this.users = users;
                return this;
            }

            public LookupBundleResponseBuilder assetCategories(List<ReferenceResponse> assetCategories) {
                this.assetCategories = assetCategories;
                return this;
            }

            public LookupBundleResponseBuilder assetStatuses(List<ReferenceResponse> assetStatuses) {
                this.assetStatuses = assetStatuses;
                return this;
            }

            public LookupBundleResponse build() {
                return new LookupBundleResponse(departments, users, assetCategories, assetStatuses);
            }
        }
}
