package com.ritallus.desvare.core.domain.usecase;

import com.ritallus.desvare.core.domain.model.GenerateKeyRequest;
import com.ritallus.desvare.core.domain.model.GeneratedKeyPair;
import com.ritallus.desvare.core.domain.model.GpgKey;
import com.ritallus.desvare.core.domain.ports.outbound.GpgGeneratorPort;
import com.ritallus.desvare.core.domain.ports.outbound.GpgKeyRepositoryPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenerateKeyUseCase {

    private final GpgGeneratorPort generatorPort;
    private final GpgKeyRepositoryPort repositoryPort;

    public GpgKey execute(GenerateKeyRequest generateKeyRequest) {

        GeneratedKeyPair pair = generatorPort.generate(generateKeyRequest);

        GpgKey key = new GpgKey(
                null,
                generateKeyRequest.name(),
                generateKeyRequest.email(),
                pair.publicKey(),
                pair.privateKey(),
                pair.fingerprint()
        );

        repositoryPort.save(key);
        return key;
    }
}
