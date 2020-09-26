package com.amazonaws.saas.metrics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class JWTClaimMetricLoggerFactoryTest {

    @Test
    public void testMultipleMetricLoggerCreationShouldReturnTheSameObject() {
        assertEquals(JWTClaimMetricLoggerFactory.getLogger(), JWTClaimMetricLoggerFactory.getLogger());
    }

    @Test
    public void testMultipleBatchMetricLoggerCreationShouldReturnTheSameObject() {
        assertEquals(JWTClaimMetricLoggerFactory.getBatchLogger(), JWTClaimMetricLoggerFactory.getBatchLogger());
    }

    @Test
    public void testBatchMetricLoggerShouldDifferentThanMetricLogger() {
        assertNotEquals(JWTClaimMetricLoggerFactory.getBatchLogger(), JWTClaimMetricLoggerFactory.getLogger());
    }
}
