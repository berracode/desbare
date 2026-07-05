package com.ritallus.desvare.ui.viewmodel.gpg;

import com.ritallus.desvare.core.domain.enums.ExpirationUnit;
import com.ritallus.desvare.core.domain.enums.KeyType;
import com.ritallus.desvare.core.domain.exception.custom.GeneratingKeyException;
import com.ritallus.desvare.core.domain.model.GenerateKeyRequest;
import com.ritallus.desvare.core.domain.usecase.GenerateKeyUseCase;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@RequiredArgsConstructor
public class CreateKeyViewModel {

    private final BooleanProperty isGenerating = new SimpleBooleanProperty(false);

    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty email = new SimpleStringProperty();

    private final StringProperty passphrase = new SimpleStringProperty();

    private final StringProperty status = new SimpleStringProperty("Ready");

    private final ObjectProperty<Integer> keySize = new SimpleObjectProperty<>(4096);

    private final BooleanProperty neverExpire = new SimpleBooleanProperty(true);

    private final ObjectProperty<Integer> expirationAmount = new SimpleObjectProperty<>(1);

    private final ObjectProperty<ExpirationUnit> expirationUnit = new SimpleObjectProperty<>(ExpirationUnit.YEARS);

    private final ObjectProperty<KeyType> keyType = new SimpleObjectProperty<>(KeyType.RSA_RSA);

    private final GenerateKeyUseCase useCase;

    public void generate() {
        var nameInput = name.get();
        var emailInput = email.get();
        var passInput = passphrase.get();

        if (isBlank(nameInput) || isBlank(emailInput) || isBlank(passInput)) {
            status.set("Debes llenar el nombre, email y la contraseña");
            return;
        }

        isGenerating.set(true);
        status.set("");

        Thread.startVirtualThread(() -> {
            try {
                GenerateKeyRequest request =
                        GenerateKeyRequest.builder()
                                .name(name.get())
                                .email(email.get())
                                .passphrase(passphrase.get())
                                .keyType(keyType.get())
                                .keySize(keySize.get())
                                .neverExpire(neverExpire.get())
                                .expirationAmount(
                                        expirationAmount.get()
                                )
                                .expirationUnit(
                                        expirationUnit.get()
                                )
                                .build();
                log.info("Llave a generar: {} - {} - {}", request.email(), request.name(), request.passphrase());
                var result = useCase.execute(request);
                Platform.runLater(() -> status.set("Key generada: " + result.fingerprint()));
            } catch (GeneratingKeyException e) {
                Platform.runLater(() -> status.set("Error generando key. Revise sus datos."));
            } catch (Exception e) {
                Platform.runLater(() -> status.set("Error desconocido generando key"));
            } finally {
                Platform.runLater(() -> {
                    name.set("");
                    email.set("");
                    passphrase.set("");
                    isGenerating.set(false);
                });
            }

        });


    }

    private Boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
