package co.com.bancolombia.desbare.core.domain.usecase;

import co.com.bancolombia.desbare.core.domain.model.GenerateKeyRequest;
import co.com.bancolombia.desbare.core.domain.model.GeneratedKeyPair;
import co.com.bancolombia.desbare.core.domain.model.GpgKey;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgGeneratorPort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgKeyRepositoryPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenerateKeyUseCase {

    private final GpgGeneratorPort generatorPort;
    private final GpgKeyRepositoryPort repositoryPort;

    public GpgKey execute(
            GenerateKeyRequest generateKeyRequest
    ) {

        GeneratedKeyPair pair =
                generatorPort.generate(generateKeyRequest);

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
