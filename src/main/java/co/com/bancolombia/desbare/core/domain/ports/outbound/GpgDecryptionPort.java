package co.com.bancolombia.desbare.core.domain.ports.outbound;

public interface GpgDecryptionPort {


    String decrypt(
            String encryptedText,
            String armoredPrivateKey,
            String passphrase
    );

}
