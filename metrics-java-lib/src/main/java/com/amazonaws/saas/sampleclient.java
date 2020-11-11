package com.amazonaws.saas;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwk.RsaJwkGenerator;

import com.amazonaws.saas.metricsmanager.MetricsPublisherFactory;
import com.amazonaws.saas.metricsmanager.MetricsPublisher;
import com.amazonaws.saas.metricsmanager.entities.*;
import com.amazonaws.saas.tokenmanager.*;
import com.amazonaws.saas.metricsmanager.builder.TenantBuilder;

import java.util.HashMap;

public class sampleclient {
    
    private static JwtTokenManager jwtTokenManager = new JwtTokenManager();
    private static final MetricsPublisher metricPublisher = MetricsPublisherFactory.getPublisher(jwtTokenManager);
    private static RsaJsonWebKey rsaJsonWebKey;

    public static void main(String[] args) throws Exception{
        rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setKeyId("k1");
        jwtTokenManager.setRsaJsonWebKey(rsaJsonWebKey);
        
        Tenant tenant = new TenantBuilder().withId("Tenant1").withName("My First Tenant").withTier("Free").build();
        String jwtToken = issueTokenFor(tenant);

        metricPublisher.publishMetricEvent(new ExecutionTimeMetric(100L), jwtToken, new HashMap<>());
    }

    
    private static String issueTokenFor(Tenant Tenant) throws Exception {
        JwtClaims claims = createJwtClaimsFor(Tenant);

        return createSignedJwtTokenFor(claims);
    }

    private static JwtClaims createJwtClaimsFor(Tenant Tenant) {
        JwtClaims claims =  new JwtClaims();
        claims.setIssuer("Issuer");
        claims.setAudience("Audience");
        claims.setExpirationTimeMinutesInTheFuture(10);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject("subject");
        claims.setClaim("tenant-id", Tenant.getId());
        claims.setClaim("tenant-name", Tenant.getName());
        claims.setClaim("tenant-tier", Tenant.getTier());
        return claims;
    }

    private static String createSignedJwtTokenFor(JwtClaims claims) throws Exception {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        return jws.getCompactSerialization();
    }
    
}
