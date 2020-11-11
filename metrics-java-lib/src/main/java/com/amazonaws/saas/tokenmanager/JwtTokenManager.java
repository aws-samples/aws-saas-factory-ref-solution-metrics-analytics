// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.tokenmanager;

import com.amazonaws.saas.metricsmanager.builder.TenantBuilder;
import com.amazonaws.saas.metricsmanager.entities.Tenant;
import com.amazonaws.saas.metricsmanager.PropertiesUtil;
import com.amazonaws.saas.tokenmanager.TokenInterface;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a sample implementation of JwtTokenManager based on jose4j library
 */
public class JwtTokenManager implements TokenInterface{

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenManager.class);
    private static PropertiesUtil propUtil = new PropertiesUtil();
    public static final String TENANT_ID_DEFAULT_CLAIM_NAME = "tenant-id";
    public static final String TENANT_NAME_DEFAULT_CLAIM_NAME = "tenant-name";
    public static final String TENANT_TIER_DEFAULT_CLAIM_NAME = "tenant-tier";
    private String issuer;
    private String audience;
    private RsaJsonWebKey rsaJsonWebKey;

    public JwtTokenManager() {
        issuer = propUtil.getPropertyOrDefault("issuer", "Issuer");
        audience = propUtil.getPropertyOrDefault("audience", "Audience");
    }

    /**
     * The implementation of this method relies on jose4j library and the jwtTokens signed by Rsa algorithm.
     * It returns tenant context extracted from the jwtToken.
     * @param jwtToken
     * @return Tenant
     */
    @Override
    public Tenant extractTenantFrom(String jwtToken) {
        JwtClaims jwtClaim = extractJwtClaim(jwtToken);
        if (jwtClaim != null) {
            String tenantId = extractTenantIdFrom(jwtClaim);
            String tenantName = extractTenantNameFrom(jwtClaim);
            String tenantTier = extractTenantTierFrom(jwtClaim);
            return new TenantBuilder()
                    .withId(tenantId)
                    .withName(tenantName)
                    .withTier(tenantTier)
                    .build();
        } else {
            return new TenantBuilder().empty();
        }
    }

    public void setRsaJsonWebKey(RsaJsonWebKey rsaJsonWebKey) {
        this.rsaJsonWebKey = rsaJsonWebKey;
    }


    private JwtClaims extractJwtClaim(String jwt) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer(issuer)
                .setExpectedAudience(audience)
                .setVerificationKey(rsaJsonWebKey.getKey())
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
                .build();

        try {
            return  jwtConsumer.processToClaims(jwt);
        }
        catch (Exception e) {
            logger.error("Error: Unable to process JWT Token", e);
        }
        return null;
    }

    private String extractTenantIdFrom(JwtClaims jwtClaims) {
        String claimName = propUtil.getPropertyOrDefault("tenant.id.claim.field", TENANT_ID_DEFAULT_CLAIM_NAME);
        return (String) jwtClaims.getClaimValue(claimName);
    }

    private String extractTenantNameFrom(JwtClaims jwtClaims) {
        String claimName = propUtil.getPropertyOrDefault("tenant.name.claim.field", TENANT_NAME_DEFAULT_CLAIM_NAME);
        return (String) jwtClaims.getClaimValue(claimName);
    }

    private String extractTenantTierFrom(JwtClaims jwtClaims) {
        String claimName = propUtil.getPropertyOrDefault("tenant.tier.claim.field", TENANT_TIER_DEFAULT_CLAIM_NAME);
        return (String) jwtClaims.getClaimValue(claimName);
    }

}
