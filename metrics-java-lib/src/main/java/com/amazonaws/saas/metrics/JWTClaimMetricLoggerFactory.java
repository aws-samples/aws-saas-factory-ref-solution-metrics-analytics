// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

public class JWTClaimMetricLoggerFactory {

    private static JWTClaimContextMetricLogger logger;
    private static JWTClaimContextMetricLogger batchLogger;

    /**
     * This method is used to initialize and get a simple logger, which will send the metric event to
     * kinesis data firehose as soon as it is received. This should be used in environments like lambda.
     * @return JWTClaimContextMetricLogger
     */
    public synchronized static JWTClaimContextMetricLogger getLogger() {
        if(logger == null) {
            logger = new JWTClaimContextMetricLogger(1);
        }
        return logger;
    }

    /**
     * This method is used to initialize and get a batch logger, which will keep collecting  metric events
     * in cache. When the buffer is full or the specified time has elapsed then it will send
     * the batch to kinesis data firehose. This should be used in long running processes such as applications running
     * in containers or EC2.
     * @return JWTClaimContextMetricLogger
     */
    public synchronized static JWTClaimContextMetricLogger getBatchLogger() {
        if(batchLogger == null) {
            batchLogger = new JWTClaimContextMetricLogger();
        }
        return batchLogger;
    }


}
