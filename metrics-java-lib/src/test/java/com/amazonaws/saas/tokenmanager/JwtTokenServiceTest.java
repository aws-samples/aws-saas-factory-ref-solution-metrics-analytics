// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.tokenmanager;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwk.RsaJwkGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.amazonaws.saas.metricsmanager.builder.TenantBuilder;
import com.amazonaws.saas.metricsmanager.entities.Tenant;

public class JwtTokenServiceTest {

    private JwtTokenManager jwtTokenManager;
    private RsaJsonWebKey rsaJsonWebKey;

    @Before
    public void setup() throws Exception {
        jwtTokenManager = new JwtTokenManager();

        rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setKeyId("k1");
        jwtTokenManager.setRsaJsonWebKey(rsaJsonWebKey);
    }

    @Test
    public void testIssueAndVerificationOfJwtToken() throws Exception {
        Tenant expectedTenant = new TenantBuilder().withId("123").withName("XYZ").withTier("Free").build();
        String jwtToken = issueTokenFor(expectedTenant);
        assertNotNull(jwtToken);

        Tenant Tenant = jwtTokenManager.extractTenantFrom(jwtToken);

        assertEquals(expectedTenant, Tenant);
    }

    String issueTokenFor(Tenant Tenant) throws Exception {
        JwtClaims claims = createJwtClaimsFor(Tenant);

        return createSignedJwtTokenFor(claims);
    }

    private JwtClaims createJwtClaimsFor(Tenant Tenant) {
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


    private String createSignedJwtTokenFor(JwtClaims claims) throws Exception {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        return jws.getCompactSerialization();
    }

}
