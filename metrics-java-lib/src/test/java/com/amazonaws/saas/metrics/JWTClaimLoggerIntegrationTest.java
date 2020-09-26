package com.amazonaws.saas.metrics;

import com.amazonaws.saas.metrics.domain.CountMetric;
import com.amazonaws.saas.metrics.domain.ExecutionTimeMetric;
import com.amazonaws.saas.metrics.domain.StorageMetric;
import org.jose4j.jwt.JwtClaims;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class JWTClaimLoggerIntegrationTest {

    private JWTClaimContextMetricLogger metricLogger;
    private JwtClaims claims = new JwtClaims();

    @Before
    public void setup() {
        metricLogger = JWTClaimMetricLoggerFactory.getLogger();

        claims.setClaim("tenant-id", "123");
        claims.setClaim("tenant-name", "ABC");
        claims.setClaim("tenant-tier", "Free");
        claims.setClaim("user-id", "111");
    }

    @Ignore
    @Test
    public void logDifferentEvents() {
        metricLogger.log(new ExecutionTimeMetric(700), claims);
        metricLogger.log(new StorageMetric(500), claims);
        metricLogger.log(new CountMetric(1), claims);
    }

}
