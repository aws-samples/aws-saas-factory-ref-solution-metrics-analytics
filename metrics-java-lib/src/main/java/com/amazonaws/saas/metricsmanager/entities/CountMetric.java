// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager.entities;

public class CountMetric extends Metric {

    public static final String METRIC_NAME = "Count";
    public static final String DEFAULT_UNIT = "unit";

    public CountMetric(Long value) {
        super(METRIC_NAME, DEFAULT_UNIT, value);
    }


}
