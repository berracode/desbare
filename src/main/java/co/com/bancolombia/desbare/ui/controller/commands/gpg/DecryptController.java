package co.com.bancolombia.desbare.ui.controller.commands.gpg;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecryptController {

    @FXML
    private PasswordField txtPassphrase;

    @FXML
    private TextArea txtEncryptedText;

    @FXML
    private TextArea txtDecryptedText;

    @FXML
    public void initialize() {
        log.info("Initialize DecryptController");
    }

    @FXML
    private void handleDecrypt() {

        log.info("Descifrando contenido");

        // TODO:
        // txtDecryptedText.setText(...)
    }
}