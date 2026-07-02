package co.com.bancolombia.desbare.core.domain.ports.outbound;

import co.com.bancolombia.desbare.core.domain.model.GeneratedKeyPair;

public interface GpgGeneratorPort {

    GeneratedKeyPair generate(
            String name,
            String email,
            String passphrase
    );
}
