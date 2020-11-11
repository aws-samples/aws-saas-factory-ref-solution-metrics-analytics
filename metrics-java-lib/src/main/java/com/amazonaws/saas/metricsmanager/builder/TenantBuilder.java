// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager.builder;

import com.amazonaws.saas.metricsmanager.entities.Tenant;

public class TenantBuilder {
    private Tenant Tenant = new Tenant();

    public TenantBuilder() {
    }

    public TenantBuilder withId(String id) {
        this.Tenant.setId(id);
        return this;
    }

    public TenantBuilder withName(String name) {
        this.Tenant.setName(name);
        return this;
    }

    public TenantBuilder withTier(String tier) {
        this.Tenant.setTier(tier);
        return this;
    }


    public Tenant build() {
        return this.Tenant;
    }

    public Tenant empty() {
        return new Tenant();
    }

}
