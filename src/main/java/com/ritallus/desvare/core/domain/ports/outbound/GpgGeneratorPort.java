package com.ritallus.desvare.core.domain.ports.outbound;

import com.ritallus.desvare.core.domain.model.GenerateKeyRequest;
import com.ritallus.desvare.core.domain.model.GeneratedKeyPair;

public interface GpgGeneratorPort {


    GeneratedKeyPair generate(
            GenerateKeyRequest request
    );

}
