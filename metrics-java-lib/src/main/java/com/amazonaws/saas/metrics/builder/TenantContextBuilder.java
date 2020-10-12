// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics.builder;

import com.amazonaws.saas.metrics.domain.TenantContext;

public class TenantContextBuilder {
    private TenantContext tenantContext = new TenantContext();

    public TenantContextBuilder() {
    }

    public TenantContextBuilder withId(String id) {
        this.tenantContext.setId(id);
        return this;
    }

    public TenantContextBuilder withName(String name) {
        this.tenantContext.setName(name);
        return this;
    }

    public TenantContextBuilder withTier(String tier) {
        this.tenantContext.setTier(tier);
        return this;
    }


    public TenantContext build() {
        return this.tenantContext;
    }

    public TenantContext empty() {
        return new TenantContext();
    }

}
