package com.ritallus.desvare.core.domain.usecase;

import java.util.List;

import com.ritallus.desvare.core.domain.model.GpgKey;
import com.ritallus.desvare.core.domain.ports.outbound.GpgKeyRepositoryPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListGpgKeysUseCase {

    private final GpgKeyRepositoryPort repositoryPort;

    public List<GpgKey> execute() {
        return repositoryPort.findAll();
    }
}