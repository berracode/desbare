package co.com.bancolombia.desbare.ui.viewmodel.gpg;

import java.util.List;

import co.com.bancolombia.desbare.core.domain.model.GpgKey;
import co.com.bancolombia.desbare.core.domain.usecase.ListGpgKeysUseCase;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.dto.GpgKeyDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ListKeysViewModel {

    private final ListGpgKeysUseCase listGpgKeysUseCase;

    // Getter expuesto para que la vista amarre el TableView directamente aquí
    @Getter
    private final ObservableList<GpgKeyDto> keysList = FXCollections.observableArrayList();

    /**
     * Carga o refresca los datos consultando al caso de uso de dominio
     */
    public void loadKeys() {
        //TODO: agregar un task o service para que no bloquee la interfaz esta carga
        try {
            log.info("Ejecutando caso de uso para listar llaves GPG");
            List<GpgKey> domainKeys = listGpgKeysUseCase.execute();
            var gpgListDto =
                    domainKeys.stream()
                            .map(g -> GpgKeyDto.builder()
                                    .fingerprint(g.fingerprint())
                                    .name(g.name())
                                    .email(g.email())
                                    .createdAt(g.createdAt())
                                    .build())
                            .toList();

            // Actualizamos la lista observable de manera segura en el hilo de JavaFX
            keysList.setAll(gpgListDto);
            log.info("Llaves cargadas exitosamente en el ViewModel. Cantidad: {}", keysList.size());
        } catch (Exception e) {
            log.error("Error al refrescar la lista de llaves en el ViewModel", e);
        }
    }
}
