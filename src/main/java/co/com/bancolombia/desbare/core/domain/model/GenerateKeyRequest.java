package co.com.bancolombia.desbare.core.domain.model;


import co.com.bancolombia.desbare.core.domain.enums.ExpirationUnit;
import co.com.bancolombia.desbare.core.domain.enums.KeyType;

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

