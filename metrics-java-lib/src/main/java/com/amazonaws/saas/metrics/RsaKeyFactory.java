// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.saas.metrics;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;

/**
 * RsaKeyFactory is used for testing purpose only and is used to generate a sample RsaKey pair and keep it in cache.
 * It is used to sign and verify JWT tokens.
 */
public class RsaKeyFactory {

    private static RsaJsonWebKey rsaJsonWebKey;

    public synchronized RsaJsonWebKey getRsaKey() throws Exception {
        if(rsaJsonWebKey == null) {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
            rsaJsonWebKey.setKeyId("k1");
        }
        return rsaJsonWebKey;
    }
}
