package co.com.bancolombia.desbare.core.domain.model;

public record GpgKey(
        Long id,
        String name,
        String email,
        String publicKey,
        String privateKey,
        String fingerprint,
        String createdAt // O LocalDateTime según manejes los tipos
) {
    public GpgKey(Long id, String name, String email, String publicKey, String privateKey, String fingerprint) {
        this(id, name, email, publicKey, privateKey, fingerprint, null);
    }
}
