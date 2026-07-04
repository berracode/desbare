package co.com.bancolombia.desbare.core.domain.usecase;

import co.com.bancolombia.desbare.core.domain.model.GpgKey;
import co.com.bancolombia.desbare.core.domain.ports.outbound.GpgKeyRepositoryPort;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class ListGpgKeysUseCase {

    private final GpgKeyRepositoryPort repositoryPort;

    public List<GpgKey> execute() {
        return repositoryPort.findAll();
    }
}