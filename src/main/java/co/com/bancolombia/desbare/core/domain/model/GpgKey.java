package co.com.bancolombia.desbare.core.domain.model;

public record GpgKey(
        Long id,
        String name,
        String email,
        String publicKey,
        String privateKey,
        String fingerprint
) {
}
