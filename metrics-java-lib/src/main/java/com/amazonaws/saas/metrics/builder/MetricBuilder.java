// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics.builder;

import com.amazonaws.saas.metrics.domain.Metric;

public class MetricBuilder {
    private Metric metric = new Metric();

    public MetricBuilder withName(String name) {
        this.metric.setName(name);
        return this;
    }

    public MetricBuilder withValue(Integer value) {
        this.metric.setValue(value);
        return this;
    }

    public MetricBuilder withUnit(String unit) {
        this.metric.setUnit(unit);
        return this;
    }

    public Metric build() {
        return this.metric;
    }
}

