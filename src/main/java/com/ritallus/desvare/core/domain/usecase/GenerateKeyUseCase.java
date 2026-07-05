package com.ritallus.desvare.core.domain.usecase;

import com.ritallus.desvare.core.domain.exception.custom.GeneratingKeyException;
import com.ritallus.desvare.core.domain.model.GenerateKeyRequest;
import com.ritallus.desvare.core.domain.model.GeneratedKeyPair;
import com.ritallus.desvare.core.domain.model.GpgKey;
import com.ritallus.desvare.core.domain.ports.outbound.GpgGeneratorPort;
import com.ritallus.desvare.core.domain.ports.outbound.GpgKeyRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GenerateKeyUseCase {

    private final GpgGeneratorPort generatorPort;
    private final GpgKeyRepositoryPort repositoryPort;

    public GpgKey execute(GenerateKeyRequest generateKeyRequest) {

        GpgKey key;
        try {
            GeneratedKeyPair pair = generatorPort.generate(generateKeyRequest);

            key = new GpgKey(
                    null,
                    generateKeyRequest.name(),
                    generateKeyRequest.email(),
                    pair.publicKey(),
                    pair.privateKey(),
                    pair.fingerprint()
            );

            repositoryPort.save(key);
        } catch (GeneratingKeyException e) {
            log.info("Error generando key: BC");
            throw e;
        } catch (Exception e) {
            log.info("Erro desconocido generando key: ", e);
            throw new RuntimeException(e);
        }
        return key;
    }
}
