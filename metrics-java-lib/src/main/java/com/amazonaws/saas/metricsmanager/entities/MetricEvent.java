// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MetricEvent {

    public static enum Type {
        Application,
        System;
    }

    private MetricEvent.Type type;
    private String workload;
    private String context;
    private Tenant Tenant;
    private Map<String, String> metaData;
    private Metric metric;
    private Long timestamp;

    public MetricEvent() {
        this.type = MetricEvent.Type.Application;
        this.timestamp = Instant.now().getEpochSecond();
        this.metaData = new HashMap<>();
        this.Tenant = new Tenant();
        this.metric = new Metric();
    }

    @JsonIgnore
    public boolean isValid() {
        return !this.getWorkload().isEmpty() && this.Tenant.isValid() && this.metric.isValid();
    }

    public void setType(MetricEvent.Type type) {
        this.type = type;
    }

    @JsonGetter("type")
    public MetricEvent.Type getType() {
        return this.type;
    }

    @JsonGetter("workload")
    public String getWorkload() {
        return this.workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    @JsonGetter("context")
    public String getContext() {
        return this.context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @JsonGetter("tenant")
    public Tenant getTenant() {
        return this.Tenant;
    }

    public void setTenant(Tenant Tenant) {
        this.Tenant = Tenant;
    }

    @JsonGetter("meta-data")
    public Map<String, String> getMetaData() {
        return this.metaData;
    }

    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }

    @JsonGetter("metric")
    public Metric getMetric() {
        return this.metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    @JsonGetter("timestamp")
    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MetricEvent{" +
                "type=" + type +
                ", workload='" + workload + '\'' +
                ", context='" + context + '\'' +
                ", tenant=" + Tenant +
                ", metaData=" + metaData +
                ", metric=" + metric +
                ", timestamp=" + timestamp +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            MetricEvent that = (MetricEvent)o;
            return this.type == that.type && Objects.equals(this.workload, that.workload) && Objects.equals(this.context, that.context) && Objects.equals(this.Tenant, that.Tenant) && Objects.equals(this.metaData, that.metaData) && Objects.equals(this.metric, that.metric) && Objects.equals(this.timestamp, that.timestamp);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.type, this.workload, this.context, this.Tenant, this.metaData, this.metric, this.timestamp});
    }

}

