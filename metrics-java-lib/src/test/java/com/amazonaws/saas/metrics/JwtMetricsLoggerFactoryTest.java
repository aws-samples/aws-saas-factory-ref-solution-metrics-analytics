// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

import org.junit.Test;

import static org.junit.Assert.*;

public class JwtMetricsLoggerFactoryTest {

    private JwtTokenService tokenService = new JwtTokenJose4jServiceImpl();
    @Test
    public void testMultipleMetricLoggerCreationShouldReturnTheSameObject() {
        assertEquals(JwtMetricsLoggerFactory.getLogger(tokenService), JwtMetricsLoggerFactory.getLogger(tokenService));
    }

    @Test
    public void testMultipleBatchMetricLoggerCreationShouldReturnTheSameObject() {
        assertEquals(JwtMetricsLoggerFactory.getBatchLogger(tokenService), JwtMetricsLoggerFactory.getBatchLogger(tokenService));
    }

    @Test
    public void testBatchMetricLoggerShouldDifferentThanMetricLogger() {
        assertNotEquals(JwtMetricsLoggerFactory.getBatchLogger(tokenService), JwtMetricsLoggerFactory.getLogger(tokenService));
    }
}
