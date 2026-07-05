package com.ritallus.desvare.ui.viewmodel.gpg;

import java.util.List;

import com.ritallus.desvare.core.domain.model.GpgKey;
import com.ritallus.desvare.core.domain.ports.outbound.Base64ServicePort;
import com.ritallus.desvare.core.domain.usecase.ListGpgKeysUseCase;
import com.ritallus.desvare.ui.viewmodel.gpg.dto.GpgKeyDto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ListKeysViewModel {

    private final ListGpgKeysUseCase listGpgKeysUseCase;
    private final Base64ServicePort base64Service;

    @Getter
    private final ObservableList<GpgKeyDto> keysList = FXCollections.observableArrayList();

    public void loadKeys() {
        log.info("Iniciando tarea asíncrona para listar llaves GPG");

        // Creamos un Task para procesar los datos en un hilo de background y no congelar la UI
        Task<List<GpgKeyDto>> loadTask = new Task<>() {
            @Override
            protected List<GpgKeyDto> call() throws Exception {
                List<GpgKey> domainKeys = listGpgKeysUseCase.execute();

                return domainKeys.stream().map(g -> {
                    String fullBase64 = base64Service.encode(g.publicKey());


                    return GpgKeyDto.builder()
                            .fingerprint(g.fingerprint())
                            .publicKey(g.publicKey())
                            .publicKeyBase64(fullBase64)
                            .name(g.name())
                            .email(g.email())
                            .createdAt(g.createdAt())
                            .build();
                }).toList();
            }
        };

        // Al completarse con exito, actualizamos la lista en el hilo principal de JavaFX (FX Application Thread)
        loadTask.setOnSucceeded(event -> {
            keysList.setAll(loadTask.getValue());
            log.info("Llaves cargadas exitosamente en el ViewModel. Cantidad: {}", keysList.size());
        });

        loadTask.setOnFailed(event -> {
            Throwable e = loadTask.getException();
            log.error("Error asíncrono al refrescar la lista de llaves", e);
        });

        // Ejecutar en un hilo separado
        Thread backgroundThread = new Thread(loadTask);
        backgroundThread.setDaemon(true); // Evita que el hilo bloquee el cierre de la app
        backgroundThread.start();
    }
}