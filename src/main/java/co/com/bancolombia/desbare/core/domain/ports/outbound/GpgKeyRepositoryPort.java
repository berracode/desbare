package co.com.bancolombia.desbare.core.domain.ports.outbound;

import java.util.List;
import java.util.Optional;

import co.com.bancolombia.desbare.core.domain.model.GpgKey;

public interface GpgKeyRepositoryPort {

    void save(GpgKey key);

    List<GpgKey> findAll();

    Optional<GpgKey> findByFingerprint(String fingerprint);

    void delete(String fingerprint);
}
