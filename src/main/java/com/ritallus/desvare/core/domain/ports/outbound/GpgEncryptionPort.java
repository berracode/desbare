package com.ritallus.desvare.core.domain.ports.outbound;

public interface GpgEncryptionPort {

    String encrypt(
            String plainText,
            String publicKey
    );

}
