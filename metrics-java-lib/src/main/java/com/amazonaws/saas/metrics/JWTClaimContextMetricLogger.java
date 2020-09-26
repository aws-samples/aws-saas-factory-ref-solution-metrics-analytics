// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

import com.amazonaws.saas.metrics.builder.MetricEventBuilder;
import com.amazonaws.saas.metrics.builder.TenantBuilder;
import com.amazonaws.saas.metrics.domain.Metric;
import com.amazonaws.saas.metrics.domain.MetricEvent;
import org.jose4j.jwt.JwtClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a sample wrapper based on jose4j library.
 */
public class JWTClaimContextMetricLogger {
    private final Logger logger = LoggerFactory.getLogger(JWTClaimContextMetricLogger.class);

    public static final String TENANT_ID_DEFAULT_CLAIM_NAME = "tenant-id";
    public static final String TENANT_NAME_DEFAULT_CLAIM_NAME = "tenant-name";
    public static final String TENANT_TIER_DEFAULT_CLAIM_NAME = "tenant-tier";

    private static PropertiesUtil propUtil = new PropertiesUtil();
    private MetricEventLogger metricEventLogger;

    public JWTClaimContextMetricLogger() {
        int batchSize = Integer.parseInt(propUtil.getPropertyOrDefault("batch.size", "25"));
        initializeMetricEventLogger(batchSize);
    }

    public JWTClaimContextMetricLogger(int batchSize) {
        initializeMetricEventLogger(batchSize);
    }

    /**
     * This method is used to log different types of metrics
     * It will extract tenant data from jwtClaims.
     * @param metric
     * @param jwtClaims
     */
    public void log(Metric metric, JwtClaims jwtClaims) {
        MetricEvent event = getMetricEvent(metric, jwtClaims, new HashMap<>());
        logger.debug(String.format("Logging Metric Event: %s", metric));
        metricEventLogger.logEvent(event);
    }


    /**
     * This method is used to log different types of metrics
     * If there is meta-data that needs to be part of the
     * metric payload then it can be sent as a key:value pair in Map.
     * It will extract tenant data from jwtClaims.
     * @param metric
     * @param jwtClaims
     * @param metaData
     */
    public void log(Metric metric, JwtClaims jwtClaims, Map<String, String> metaData) {
        MetricEvent event = getMetricEvent(metric, jwtClaims, metaData);
        logger.debug(String.format("Logging Metric Event: %s", metric));
        metricEventLogger.logEvent(event);
    }

    protected MetricEvent getMetricEvent(Metric metric, JwtClaims jwtClaims, Map<String, String> metaData) {
        String tenantId = extractTenantIdFrom(jwtClaims);
        String tenantName = extractTenantNameFrom(jwtClaims);
        String tenantTier = extractTenantTierFrom(jwtClaims);
        String workload = propUtil.getPropertyOrDefault("workload", "No Workload Info In ENV Variable.");

        return new MetricEventBuilder()
                .withType(MetricEvent.Type.Application)
                .withWorkload(workload)
                .withMetric(metric)
                .withTenant(new TenantBuilder()
                        .withId(tenantId)
                        .withName(tenantName)
                        .withTier(tenantTier)
                        .build())
                .withMetaData(metaData)
                .build();
    }

    public MetricEventLogger getMetricEventLogger() {
        return metricEventLogger;
    }

    private void initializeMetricEventLogger(int batchSize) {
        logger.debug("Initializing Metric Event Logger");
        int flushTimeWindow = Integer.parseInt(propUtil.getPropertyOrDefault("flush.time.window.in.seconds", "5"));
        String kinesisStreamName = propUtil.getPropertyOrDefault("kinesis.stream.name", "Metrics");
        Region region = Region.of(propUtil.getPropertyOrDefault("aws.region", "us-east-1"));
        metricEventLogger = new MetricEventLogger(kinesisStreamName, region, batchSize, flushTimeWindow);
    }

    private String extractTenantIdFrom(JwtClaims jwtClaims) {
        String claimName = propUtil.getPropertyOrDefault("TENANT_ID_CLAIM_FIELD", TENANT_ID_DEFAULT_CLAIM_NAME);
        return (String) jwtClaims.getClaimValue(claimName);
    }

    private String extractTenantNameFrom(JwtClaims jwtClaims) {
        String claimName = propUtil.getPropertyOrDefault("TENANT_NAME_CLAIM_FIELD", TENANT_NAME_DEFAULT_CLAIM_NAME);
        return (String) jwtClaims.getClaimValue(claimName);
    }

    private String extractTenantTierFrom(JwtClaims jwtClaims) {
        String claimName = propUtil.getPropertyOrDefault("TENANT_TIER_CLAIM_FIELD", TENANT_TIER_DEFAULT_CLAIM_NAME);
        return (String) jwtClaims.getClaimValue(claimName);
    }

}
