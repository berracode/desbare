package com.ritallus.desvare.core.domain.usecase;

import com.ritallus.desvare.core.domain.model.GpgKey;
import com.ritallus.desvare.core.domain.ports.outbound.GpgKeyRepositoryPort;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListGpgKeysUseCase {

    private final GpgKeyRepositoryPort repositoryPort;

    public List<GpgKey> execute() {
        return repositoryPort.findAll();
    }
}