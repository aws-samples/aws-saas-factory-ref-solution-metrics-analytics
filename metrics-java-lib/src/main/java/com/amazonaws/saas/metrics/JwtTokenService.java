// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

import com.amazonaws.saas.metrics.domain.TenantContext;

public interface JwtTokenService {

    public TenantContext extractTenantContextFrom(String jwtToken);
}
