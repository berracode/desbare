package co.com.bancolombia.desbare.core.domain.usecase;

import co.com.bancolombia.desbare.core.domain.model.GpgKey;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgEncryptionPort;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgKeyRepositoryPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EncryptUseCase {

    private final GpgEncryptionPort encryptionPort;
    private final GpgKeyRepositoryPort repositoryPort;

    public String execute(
            String plainText,
            String recipientFingerprint
    ) {

        GpgKey recipient =
                repositoryPort
                        .findByFingerprint(recipientFingerprint)
                        .orElseThrow(() ->
                                             new IllegalArgumentException(
                                                     "No existe la llave "
                                                             + recipientFingerprint));

        return encryptionPort.encrypt(
                plainText,
                recipient.publicKey()
        );
    }
}