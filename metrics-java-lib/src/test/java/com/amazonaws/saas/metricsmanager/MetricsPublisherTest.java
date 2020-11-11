// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import com.amazonaws.saas.metricsmanager.MetricsPublisher;
import com.amazonaws.saas.metricsmanager.MetricsPublisherFactory;
import com.amazonaws.saas.metricsmanager.builder.TenantBuilder;
import com.amazonaws.saas.metricsmanager.entities.*;
import com.amazonaws.saas.tokenmanager.TokenInterface;

import static org.junit.Assert.*;

public class MetricsPublisherTest {

    private MetricsPublisher metricPublisher;
    private final Mockery context = new Mockery();
    private final TokenInterface mockJwtService = context.mock(TokenInterface.class);

    @Before
    public void setup() {
        metricPublisher = MetricsPublisherFactory.getPublisher(mockJwtService);
    }

    @Test
    public void testMetricLogging() {
        String jwtToken = "jwtToken";
        Tenant expectedTenant = new TenantBuilder().withId("123").withName("XYZ").withTier("Free").build();
        Metric expectedMetric = new ExecutionTimeMetric(100L);

        context.checking(new Expectations() {{
            oneOf (mockJwtService).extractTenantFrom(jwtToken);
            will(returnValue(expectedTenant));
        }});

        MetricEvent event = metricPublisher.buildMetricEvent(expectedMetric, jwtToken, new HashMap<>());
        assertEquals(expectedTenant, event.getTenant());
        assertEquals(expectedMetric, event.getMetric());
    }

}
