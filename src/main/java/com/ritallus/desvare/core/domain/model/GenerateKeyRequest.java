package com.ritallus.desvare.core.domain.model;


import com.ritallus.desvare.core.domain.enums.ExpirationUnit;
import com.ritallus.desvare.core.domain.enums.KeyType;

import lombok.Builder;

@Builder
public record GenerateKeyRequest(
        String name,
        String email,
        String passphrase,
        KeyType keyType,
        int keySize,
        boolean neverExpire,
        Integer expirationAmount,
        ExpirationUnit expirationUnit
) {
}

