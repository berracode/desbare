package co.com.bancolombia.desbare.ui.viewmodel.gpg;

import co.com.bancolombia.desbare.core.domain.model.GpgKey;
import co.com.bancolombia.desbare.core.domain.usecase.GenerateKeyUseCase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateKeyViewModel {

    private final GenerateKeyUseCase generateKeyUseCase;

    private final StringProperty name =
            new SimpleStringProperty();

    private final StringProperty email =
            new SimpleStringProperty();

    private final StringProperty passphrase =
            new SimpleStringProperty();

    private final StringProperty status =
            new SimpleStringProperty("None");

    public void generate() {

        GpgKey key = generateKeyUseCase.execute(
                name.get(),
                email.get(),
                passphrase.get()
        );

        status.set(
                "Llave creada: " +
                        key.fingerprint()
        );
    }
}
