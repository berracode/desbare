package com.ritallus.desvare.core.domain.usecase;

import com.ritallus.desvare.core.domain.model.GpgKey;
import com.ritallus.desvare.core.domain.ports.outbound.GpgDecryptionPort;
import com.ritallus.desvare.core.domain.ports.outbound.GpgKeyRepositoryPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DecryptUseCase {

    private final GpgDecryptionPort decryptionPort;
    private final GpgKeyRepositoryPort repositoryPort;

    public String execute(
            String encryptedText,
            String fingerprint,
            String passphrase
    ) {

        GpgKey key = repositoryPort
                .findByFingerprint(fingerprint)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Llave no encontrada: " + fingerprint
                ));

        return decryptionPort.decrypt(
                encryptedText,
                key.privateKey(),
                passphrase
        );
    }
}
