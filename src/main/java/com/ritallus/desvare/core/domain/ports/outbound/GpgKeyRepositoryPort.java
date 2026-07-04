package com.ritallus.desvare.core.domain.ports.outbound;

import java.util.List;
import java.util.Optional;

import com.ritallus.desvare.core.domain.model.GpgKey;

public interface GpgKeyRepositoryPort {

    void save(GpgKey key);

    List<GpgKey> findAll();

    Optional<GpgKey> findByFingerprint(String fingerprint);

    void delete(String fingerprint);
}
