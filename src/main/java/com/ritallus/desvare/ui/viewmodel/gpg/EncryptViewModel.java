package com.ritallus.desvare.ui.viewmodel.gpg;

import com.ritallus.desvare.core.domain.usecase.EncryptUseCase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EncryptViewModel {

    private final EncryptUseCase encryptUseCase;

    private final StringProperty plainText =
            new SimpleStringProperty();

    private final StringProperty recipient =
            new SimpleStringProperty();

    private final StringProperty encrypted =
            new SimpleStringProperty();

    public void encrypt() {

        encrypted.set(
                encryptUseCase.execute(
                        plainText.get(),
                        recipient.get()
                )
        );
    }
}