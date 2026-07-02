package co.com.bancolombia.desbare.core.domain.usecase;

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
            String name,
            String email,
            String passphrase
    ) {

        GeneratedKeyPair pair =
                generatorPort.generate(name, email, passphrase);

        GpgKey key = new GpgKey(
                null,
                name,
                email,
                pair.publicKey(),
                pair.privateKey(),
                pair.fingerprint()
        );

        repositoryPort.save(key);

        return key;
    }
}
