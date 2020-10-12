// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

import com.amazonaws.saas.metrics.builder.TenantContextBuilder;
import com.amazonaws.saas.metrics.domain.TenantContext;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JwtTokenJose4jServiceImplTest {

    private JwtTokenJose4jServiceImpl jwtTokenManager;
    private RsaJsonWebKey rsaJsonWebKey;

    @Before
    public void setup() throws Exception {
        jwtTokenManager = new JwtTokenJose4jServiceImpl();
        rsaJsonWebKey = new RsaKeyFactory().getRsaKey();
        jwtTokenManager.setRsaJsonWebKey(rsaJsonWebKey);
    }

    @Test
    public void testIssueAndVerificationOfJwtToken() throws Exception {
        TenantContext expectedTenantContext = new TenantContextBuilder().withId("123").withName("XYZ").withTier("Free").build();
        String jwtToken = issueTokenFor(expectedTenantContext);
        assertNotNull(jwtToken);

        TenantContext tenantContext = jwtTokenManager.extractTenantContextFrom(jwtToken);

        assertEquals(expectedTenantContext, tenantContext);
    }

    String issueTokenFor(TenantContext tenantContext) throws Exception {
        JwtClaims claims = createJwtClaimsFor(tenantContext);

        return createSignedJwtTokenFor(claims);
    }

    private JwtClaims createJwtClaimsFor(TenantContext tenantContext) {
        JwtClaims claims =  new JwtClaims();
        claims.setIssuer("Issuer");
        claims.setAudience("Audience");
        claims.setExpirationTimeMinutesInTheFuture(10);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setSubject("subject");
        claims.setClaim("tenant-id", tenantContext.getId());
        claims.setClaim("tenant-name", tenantContext.getName());
        claims.setClaim("tenant-tier", tenantContext.getTier());
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
