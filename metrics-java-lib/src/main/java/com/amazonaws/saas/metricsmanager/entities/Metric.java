// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metricsmanager.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class Metric {

    private String name;
    private String unit;
    private Long value;

    public Metric() {
    }

    public Metric(String name, String unit, Long value) {
        this.name = name;
        this.unit = unit;
        this.value = value;
    }

    @JsonIgnore
    public boolean isValid() {
        return this.getName() != null && !this.getUnit().isEmpty() && this.getValue() != null;
    }

    @JsonGetter("name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("unit")
    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonGetter("value")
    public Long getValue() {
        return this.value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String toString() {
        return "Metric{name=" + this.name + ", unit='" + this.unit + '\'' + ", value=" + this.value + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Metric metric = (Metric)o;
            return this.name == metric.name && Objects.equals(this.unit, metric.unit) && Objects.equals(this.value, metric.value);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.name, this.unit, this.value});
    }


}

