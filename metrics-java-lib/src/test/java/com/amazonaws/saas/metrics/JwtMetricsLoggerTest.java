// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

import com.amazonaws.saas.metrics.builder.TenantContextBuilder;
import com.amazonaws.saas.metrics.domain.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class JwtMetricsLoggerTest {

    private JwtMetricsLogger metricLogger;
    private final Mockery context = new Mockery();
    private final JwtTokenService mockJwtTokenJose4j = context.mock(JwtTokenService.class);

    @Before
    public void setup() {
        metricLogger = JwtMetricsLoggerFactory.getLogger(mockJwtTokenJose4j);
    }

    @Test
    public void testJwtBasedMetricLogging() {
        String jwtToken = "jwtToken";
        TenantContext expectedTenantContext = new TenantContextBuilder().withId("123").withName("XYZ").withTier("Free").build();
        Metric expectedMetric = new ExecutionTimeMetric(100L);

        context.checking(new Expectations() {{
            oneOf (mockJwtTokenJose4j).extractTenantContextFrom(jwtToken);
            will(returnValue(expectedTenantContext));
        }});

        MetricEvent event = metricLogger.getMetricEvent(expectedMetric, jwtToken, new HashMap<>());
        assertEquals(expectedTenantContext, event.getTenantContext());
        assertEquals(expectedMetric, event.getMetric());
    }

}
