package com.ritallus.desvare.core.domain.ports.outbound;

public interface GpgDecryptionPort {


    String decrypt(
            String encryptedText,
            String armoredPrivateKey,
            String passphrase
    );

}
