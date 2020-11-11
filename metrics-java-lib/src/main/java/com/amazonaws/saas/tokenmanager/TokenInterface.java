// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.tokenmanager;

import com.amazonaws.saas.metricsmanager.entities.Tenant;

public interface TokenInterface {

    public Tenant extractTenantFrom(String token);
}