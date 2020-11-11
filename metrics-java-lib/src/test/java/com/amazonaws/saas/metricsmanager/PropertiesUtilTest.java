package com.amazonaws.saas.metricsmanager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertiesUtilTest {

    private PropertiesUtil propertiesUtil = new PropertiesUtil();

    @Test(expected = RuntimeException.class)
    public void testConfigFileNotFound() {
        new PropertiesUtil("xyz");
    }

    @Test()
    public void testConfigFileFound() {
        new PropertiesUtil();
    }

    @Test()
    public void loadPropertyFromFile() {
        String propertyValue = propertiesUtil.getPropertyOrDefault("aws.region", "DefaultRegion");
        assertEquals("us-east-1", propertyValue);
    }

    @Test()
    public void whenPropertyIsNotFoundInFile() {
        String propertyValue = propertiesUtil.getPropertyOrDefault("kinesis.streams.name", "DefaultStreamName");
        assertEquals("DefaultStreamName", propertyValue);
    }

    @Test
    public void testLibraryProperties() throws Exception{
        assertEquals("tenant-id", propertiesUtil.getPropertyOrDefault("Tenant.id.claim.field", "None"));
        assertEquals("tenant-name", propertiesUtil.getPropertyOrDefault("Tenant.name.claim.field", "None"));
        assertEquals("tenant-tier", propertiesUtil.getPropertyOrDefault("Tenant.tier.claim.field", "None"));
        assertEquals("user-id", propertiesUtil.getPropertyOrDefault("user.id.claim.field", "None"));
        assertEquals("us-east-1", propertiesUtil.getPropertyOrDefault("aws.region", "None"));
        assertEquals("Application Name", propertiesUtil.getPropertyOrDefault("workload", "None"));
        assertEquals("25", propertiesUtil.getPropertyOrDefault("batch.size", "None"));
        assertEquals("30", propertiesUtil.getPropertyOrDefault("flush.time.window.in.seconds", "None"));
    }
}
