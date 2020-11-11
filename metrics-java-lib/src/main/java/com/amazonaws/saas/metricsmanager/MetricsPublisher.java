// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.saas.metricsmanager.builder.MetricEventBuilder;
import com.amazonaws.saas.metricsmanager.entities.Metric;
import com.amazonaws.saas.metricsmanager.entities.MetricEvent;
import com.amazonaws.saas.metricsmanager.entities.Tenant;
import com.amazonaws.saas.tokenmanager.TokenInterface;

/**
 * This is a sample wrapper for FirehosePublishService and uses JWT tokens to extract tenant context. 
 */
public class MetricsPublisher {
    private static final Logger logger = LoggerFactory.getLogger(MetricsPublisher.class);

    private TokenInterface tokenService;
    private FirehosePublishService firehosePublisher;
    private static PropertiesUtil propUtil = new PropertiesUtil();

    public MetricsPublisher() {
        int batchSize = Integer.parseInt(propUtil.getPropertyOrDefault("batch.size", "25"));
        initializeFirehosePublishService(batchSize);
    }

    public MetricsPublisher(int batchSize) {
        initializeFirehosePublishService(batchSize);
    }

    /**
     * This method is used to log different types of metrics
     * It will extract tenant data from jwtToken.
     * @param metric
     * @param jwtToken
     */
    public void publishMetricEvent(Metric metric, String jwtToken) {
        MetricEvent event = buildMetricEvent(metric, jwtToken, new HashMap<>());
        logger.debug(String.format("Logging Metric Event: %s", metric));
        firehosePublisher.publishEvent(event);
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
    public void publishMetricEvent(Metric metric, String jwtToken, Map<String, String> metaData) {
        MetricEvent event = buildMetricEvent(metric, jwtToken, metaData);
        logger.debug(String.format("Logging Metric Event: %s", metric));
        firehosePublisher.publishEvent(event);
    }

    public void setTokenService(TokenInterface tokenService) {
        this.tokenService = tokenService;
    }

    protected MetricEvent buildMetricEvent(Metric metric, String jwtToken, Map<String, String> metaData) {
        Tenant Tenant = tokenService.extractTenantFrom(jwtToken);

        String workload = propUtil.getPropertyOrDefault("workload", "No Workload Info In ENV Variable.");

        return new MetricEventBuilder()
                .withType(MetricEvent.Type.Application)
                .withWorkload(workload)
                .withMetric(metric)
                .withTenant(Tenant)
                .withMetaData(metaData)
                .build();
    }

    private void initializeFirehosePublishService(int batchSize) {
        logger.debug("Initializing the service to publish to firehose");
        int flushTimeWindow = Integer.parseInt(propUtil.getPropertyOrDefault("flush.time.window.in.seconds", "5"));
        String kinesisStreamName = propUtil.getPropertyOrDefault("kinesis.stream.name", "Metrics");
        Region region = Region.of(propUtil.getPropertyOrDefault("aws.region", "us-east-1"));
        firehosePublisher = new FirehosePublishService(kinesisStreamName, region, batchSize, flushTimeWindow);
    }


}
