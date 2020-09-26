package com.amazonaws.saas.metrics;

import com.amazonaws.saas.metrics.domain.CountMetric;
import com.amazonaws.saas.metrics.domain.ExecutionTimeMetric;
import com.amazonaws.saas.metrics.domain.Metric;
import com.amazonaws.saas.metrics.domain.MetricEvent;
import org.jose4j.jwt.JwtClaims;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class JWTClaimLoggerUnitTest {

    private JWTClaimContextMetricLogger metricLogger;
    private JwtClaims claims = new JwtClaims();

    @Before
    public void setup() {
        metricLogger = JWTClaimMetricLoggerFactory.getLogger();
        claims.setClaim("tenant-id", "123");
        claims.setClaim("tenant-name", "ABC");
        claims.setClaim("tenant-tier", "Free");
    }

    @Test
    public void testJWTClaimBasedMetricLogging() {
        JwtClaims claims = new JwtClaims();
        claims.setClaim("tenant-id", "123456");
        claims.setClaim("tenant-name", "Tenant");
        claims.setClaim("tenant-tier", "Free");

        Metric countMetric = new CountMetric(1);
        MetricEvent event = metricLogger.getMetricEvent(countMetric, claims, new HashMap<>());
        assertEquals("123456", event.getTenant().getId());
        assertEquals("Tenant", event.getTenant().getName());
        assertEquals("Free", event.getTenant().getTier());
    }

    @Test
    public void testJWTClaimBasedExecutionTimeMetric() {
        Metric metric = new ExecutionTimeMetric(700);
        MetricEvent event = metricLogger.getMetricEvent(metric, claims, new HashMap<>());
        assertEquals(metric.getName(), event.getMetric().getName());
        assertEquals(metric.getUnit(), event.getMetric().getUnit());
        assertEquals(metric.getValue(), event.getMetric().getValue());
    }

    @Test
    public void testBatchLogger() {
        JWTClaimContextMetricLogger logger = JWTClaimMetricLoggerFactory.getBatchLogger();
        assertEquals(logger.getMetricEventLogger().getBufferSize(), 25);
    }


}
