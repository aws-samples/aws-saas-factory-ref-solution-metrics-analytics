// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

public class TenantBuilder {
    private Tenant tenant = new Tenant();

    public TenantBuilder() {
    }

    public TenantBuilder withId(String id) {
        this.tenant.setId(id);
        return this;
    }

    public TenantBuilder withName(String name) {
        this.tenant.setName(name);
        return this;
    }

    public TenantBuilder withTier(String tier) {
        this.tenant.setTier(tier);
        return this;
    }

    public Tenant build() {
        return this.tenant;
    }
}
