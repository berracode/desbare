package com.ritallus.desvare.ui.viewmodel.gpg;

import com.ritallus.desvare.core.domain.usecase.DecryptUseCase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DecryptViewModel {

    private final DecryptUseCase decryptUseCase;

    private final StringProperty fingerprint =
            new SimpleStringProperty();

    private final StringProperty encryptedText =
            new SimpleStringProperty();

    private final StringProperty passphrase =
            new SimpleStringProperty();

    private final StringProperty decryptedText =
            new SimpleStringProperty();

    public void decrypt() {

        String result = decryptUseCase.execute(
                encryptedText.get(),
                fingerprint.get(),
                passphrase.get()
        );

        decryptedText.set(result);
    }
}
