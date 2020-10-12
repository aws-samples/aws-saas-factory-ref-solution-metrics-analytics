// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

import com.amazonaws.saas.metrics.builder.MetricEventBuilder;
import com.amazonaws.saas.metrics.domain.Metric;
import com.amazonaws.saas.metrics.domain.MetricEvent;
import com.amazonaws.saas.metrics.domain.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a sample wrapper for MetricEventLogger and uses JWT tokens to extract tenant context.
 * Its implementation is based on jose4j library.
 */
public class JwtMetricsLogger {
    private static final Logger logger = LoggerFactory.getLogger(JwtMetricsLogger.class);

    private JwtTokenService tokenService;
    private MetricEventLogger metricEventLogger;
    private static PropertiesUtil propUtil = new PropertiesUtil();

    public JwtMetricsLogger() {
        int batchSize = Integer.parseInt(propUtil.getPropertyOrDefault("batch.size", "25"));
        initializeMetricEventLogger(batchSize);
    }

    public JwtMetricsLogger(int batchSize) {
        initializeMetricEventLogger(batchSize);
    }

    /**
     * This method is used to log different types of metrics
     * It will extract tenant data from jwtToken.
     * @param metric
     * @param jwtToken
     */
    public void log(Metric metric, String jwtToken) {
        MetricEvent event = getMetricEvent(metric, jwtToken, new HashMap<>());
        logger.debug(String.format("Logging Metric Event: %s", metric));
        metricEventLogger.logEvent(event);
    }


    /**
     * This method is used to log different types of metrics
     * If there is meta-data that needs to be part of the
     * metric payload then it can be sent as a key:value pair in Map.
     * It will extract tenant data from jwtToken.
     * @param metric
     * @param jwtToken
     * @param metaData
     */
    public void log(Metric metric, String jwtToken, Map<String, String> metaData) {
        MetricEvent event = getMetricEvent(metric, jwtToken, metaData);
        logger.debug(String.format("Logging Metric Event: %s", metric));
        metricEventLogger.logEvent(event);
    }

    public void setTokenService(JwtTokenService tokenService) {
        this.tokenService = tokenService;
    }

    protected MetricEvent getMetricEvent(Metric metric, String jwtToken, Map<String, String> metaData) {
        TenantContext tenantContext = tokenService.extractTenantContextFrom(jwtToken);

        String workload = propUtil.getPropertyOrDefault("workload", "No Workload Info In ENV Variable.");

        return new MetricEventBuilder()
                .withType(MetricEvent.Type.Application)
                .withWorkload(workload)
                .withMetric(metric)
                .withTenantContext(tenantContext)
                .withMetaData(metaData)
                .build();
    }

    private void initializeMetricEventLogger(int batchSize) {
        logger.debug("Initializing Metric Event Logger");
        int flushTimeWindow = Integer.parseInt(propUtil.getPropertyOrDefault("flush.time.window.in.seconds", "5"));
        String kinesisStreamName = propUtil.getPropertyOrDefault("kinesis.stream.name", "Metrics");
        Region region = Region.of(propUtil.getPropertyOrDefault("aws.region", "us-east-1"));
        metricEventLogger = new MetricEventLogger(kinesisStreamName, region, batchSize, flushTimeWindow);
    }


}
