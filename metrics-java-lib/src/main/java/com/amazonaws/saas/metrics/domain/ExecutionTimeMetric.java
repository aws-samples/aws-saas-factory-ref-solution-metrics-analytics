// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics.domain;

public class ExecutionTimeMetric extends Metric {

    public static final String METRIC_NAME = "ExecutionTime";
    public static final String DEFAULT_UNIT = "msec";

    public ExecutionTimeMetric(Integer value) {
        super(METRIC_NAME, DEFAULT_UNIT, value);
    }

    public ExecutionTimeMetric(String unit, Integer value) {
        super(METRIC_NAME, unit, value);
    }


}
