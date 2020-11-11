// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager;

import org.junit.Test;

import static org.junit.Assert.*;

import com.amazonaws.saas.tokenmanager.TokenInterface;
import com.amazonaws.saas.tokenmanager.JwtTokenManager;

public class MetricsPublisherFactoryTest {
    private TokenInterface tokenService = new JwtTokenManager();
    @Test
    public void testMultipleMetricLoggerCreationShouldReturnTheSameObject() {
        assertEquals(MetricsPublisherFactory.getPublisher(tokenService), MetricsPublisherFactory.getPublisher(tokenService));
    }

    @Test
    public void testMultipleBatchMetricLoggerCreationShouldReturnTheSameObject() {
        assertEquals(MetricsPublisherFactory.getBatchPublisher(tokenService), MetricsPublisherFactory.getBatchPublisher(tokenService));
    }

    @Test
    public void testBatchMetricLoggerShouldDifferentThanMetricLogger() {
        assertNotEquals(MetricsPublisherFactory.getBatchPublisher(tokenService), MetricsPublisherFactory.getPublisher(tokenService));
    }
}
