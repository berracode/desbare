package co.com.bancolombia.desbare.core.domain.ports.outbound;

public interface GpgEncryptionPort {

    String encrypt(
            String plainText,
            String publicKey
    );

}
