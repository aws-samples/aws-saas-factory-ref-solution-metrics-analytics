// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class PropertiesUtil {

    public static final Properties properties = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(MetricEventLogger.class);

    public PropertiesUtil() {
        this("lib-config.properties");
    }

    public PropertiesUtil(String propertiesFileName) {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(propertiesFileName));
        } catch (Exception e) {
            String message = "Unable to load properties file";
            logger.error(String.format("ERROR: %s", message));
            throw new RuntimeException(message);
        }
    }

    public  String getPropertyOrDefault(String propertyName, String defaultValue) {
        if (isNotNullOrEmpty(propertyName)) {
            String propertyValue = (String) properties.get(propertyName);
            if(isNullOrEmpty(propertyValue)) {
                propertyValue = defaultValue;
            }
            return propertyValue;
        }
        return defaultValue;
    }


    private  boolean isNotNullOrEmpty (String value) {
        return !isNullOrEmpty(value);
    }

    private  boolean isNullOrEmpty (String value) {
        return value == null || "".equals(value);
    }

}
