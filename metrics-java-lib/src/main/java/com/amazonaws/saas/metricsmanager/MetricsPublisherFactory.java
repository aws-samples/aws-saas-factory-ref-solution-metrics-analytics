// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager;

import com.amazonaws.saas.tokenmanager.TokenInterface;
/**
 * This is a factory class used to create and configure a batch or single publisher.
 */
public class MetricsPublisherFactory {

    private static MetricsPublisher publisher;
    private static MetricsPublisher batchPublisher;
    /**
     * This method is used to initialize and get a simple publisher, which will send the metric event to
     * kinesis data firehose as soon as it is received. This should be used in environments like lambda.
     * @return Metricspublisher
     */
    public synchronized static MetricsPublisher getPublisher(TokenInterface tokenService) {
        if(publisher == null) {
            publisher = new MetricsPublisher(1);            
            publisher.setTokenService(tokenService);
        }
        return publisher;
    }

    /**
     * This method is used to initialize and get a batch publisher, which will keep collecting  metric events
     * in cache. When the buffer is full or the specified time has elapsed then it will send
     * the batch to kinesis data firehose. This should be used in long running processes such as applications running
     * in containers or EC2.
     * @return Metricspublisher
     */
    public synchronized static MetricsPublisher getBatchPublisher(TokenInterface tokenService) {
        if(batchPublisher == null) {
            batchPublisher = new MetricsPublisher();      
            publisher.setTokenService(tokenService);      
        }
        return batchPublisher;
    }


}
