package com.ritallus.desvare.core.domain.usecase;

import com.ritallus.desvare.core.domain.model.GpgKey;
import com.ritallus.desvare.core.domain.ports.outbound.GpgEncryptionPort;
import com.ritallus.desvare.core.domain.ports.outbound.GpgKeyRepositoryPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EncryptUseCase {

    private final GpgEncryptionPort encryptionPort;
    private final GpgKeyRepositoryPort repositoryPort;

    public String execute(String plainText, String recipientFingerprint) {

        GpgKey recipient =
                repositoryPort
                        .findByFingerprint(recipientFingerprint)
                        .orElseThrow(() -> new IllegalArgumentException("No existe la llave " + recipientFingerprint));

        return encryptionPort.encrypt(plainText, recipient.publicKey());
    }
}