// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics.domain;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

public class TenantContext {

    private String id;
    private String name;
    private String tier;

    public TenantContext() {
    }

    @JsonIgnore
    public boolean isValid() {
        return this.getId() != null;
    }

    @JsonGetter("id")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter("name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("tier")
    public String getTier() {
        return this.tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String toString() {
        return "Tenant{id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", tier=" + this.tier + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantContext tenantContext = (TenantContext) o;
        return Objects.equals(id, tenantContext.id) &&
                Objects.equals(name, tenantContext.name) &&
                Objects.equals(tier, tenantContext.tier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tier);
    }
}
