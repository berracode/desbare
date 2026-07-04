package com.ritallus.desvare.ui.viewmodel.gpg;

import com.ritallus.desvare.core.domain.enums.ExpirationUnit;
import com.ritallus.desvare.core.domain.enums.KeyType;
import com.ritallus.desvare.core.domain.model.GenerateKeyRequest;
import com.ritallus.desvare.core.domain.usecase.GenerateKeyUseCase;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class CreateKeyViewModel {

    private final StringProperty name =
            new SimpleStringProperty();

    private final StringProperty email =
            new SimpleStringProperty();

    private final StringProperty passphrase =
            new SimpleStringProperty();

    private final StringProperty status =
            new SimpleStringProperty("Ready");

    private final ObjectProperty<Integer> keySize =
            new SimpleObjectProperty<>(4096);

    private final BooleanProperty neverExpire =
            new SimpleBooleanProperty(true);

    private final ObjectProperty<Integer> expirationAmount =
            new SimpleObjectProperty<>(1);

    private final ObjectProperty<ExpirationUnit> expirationUnit = new SimpleObjectProperty<>(ExpirationUnit.YEARS);

    private final ObjectProperty<KeyType> keyType =
            new SimpleObjectProperty<>(KeyType.RSA_RSA);

    private final GenerateKeyUseCase useCase;

    public void generate() {

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

        var result =
                useCase.execute(request);

        status.set(
                "Key generated: "
                        + result.fingerprint()
        );
    }
}
