// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager;

import com.amazonaws.saas.metricsmanager.entities.MetricEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.CharsetUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.firehose.FirehoseClient;
import software.amazon.awssdk.services.firehose.model.PutRecordBatchRequest;
import software.amazon.awssdk.services.firehose.model.Record;

/**
 * FirehosePublishService is used to log the metric event by sending it to kinsis data firehose.
 * It can be used in a batch and single mode of communication.
 * In log running tasks batch mode is preferred, batch size and time window can be configured via
 * properties files.
 */
public class FirehosePublishService {
    private final Logger logger = LoggerFactory.getLogger(FirehosePublishService.class);

    public static final int DEFAULT_FLUSH_TIME_IN_SECS = 30;
    private final FirehoseClient firehose;
    private final String streamName;
    private final int bufferSize;
    private List<Record> recordBuffer;
    private Long startTime;
    private int flushTimeWindowInSeconds;

    protected FirehosePublishService(String kinesisStreamName, Region region, int batchSize, int flushTimeWindow) {
        this.bufferSize = batchSize;
        this.streamName = kinesisStreamName;
        this.flushTimeWindowInSeconds = flushTimeWindow;
        this.initializeBuffer();
        this.firehose = getFirehoseClientIn(region);
    }

    public void publishEvent(MetricEvent event) {
        String eventJsonString = "";

        try {
            eventJsonString = (new ObjectMapper()).writeValueAsString(event);
            logger.debug(String.format("Metric Event Json: %s", eventJsonString));
            Record record = Record.builder().data(SdkBytes.fromByteArray(eventJsonString.getBytes(CharsetUtil.UTF_8))).build();
            recordBuffer.add(record);

            synchronized (this) {
                if (shouldSendToKinesis()) {
                    writeToKinesisFirehose();
                }
            }
        } catch (Exception exception) {
            logger.debug(String.format("Error: Unable to log metric: %s", eventJsonString), exception);
        }

    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void shutdown() {
        logger.debug("Clean shutdown, sending buffer data to kinesis");
        writeToKinesisFirehose();
    }

    protected FirehoseClient getFirehoseClientIn(Region region) {
        return FirehoseClient.builder().region(region).build();
    }

    protected boolean shouldSendToKinesis() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000L;
        if (recordBuffer.size() >= bufferSize) {
            logger.debug("Buffer full, writing to kinesis");
            return true;
        } else if (elapsedTime >= flushTimeWindowInSeconds) {
            logger.debug("Time elapsed, writing to kinesis");
            return true;
        } else {
            return false;
        }
    }

    protected void writeToKinesisFirehose() {
        logger.debug(streamName);
        firehose.putRecordBatch(PutRecordBatchRequest.builder().deliveryStreamName(streamName).records(recordBuffer).build());
        initializeBuffer();
    }

    private void initializeBuffer() {
        logger.debug("Initializing Buffer");
        startTime = System.currentTimeMillis();
        recordBuffer = Collections.synchronizedList(new ArrayList<>());
    }
}
