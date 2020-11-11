// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager.entities;

public class StorageMetric extends Metric {

    public static final String METRIC_NAME = "Storage";
    public static final String DEFAULT_UNIT = "MB";

    public StorageMetric(Long value) {
        super(METRIC_NAME, DEFAULT_UNIT, value);
    }

    public StorageMetric(String unit, Long value) {
        super(METRIC_NAME, unit, value);
    }
}
