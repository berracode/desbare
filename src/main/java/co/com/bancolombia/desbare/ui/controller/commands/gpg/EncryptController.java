package co.com.bancolombia.desbare.ui.controller.commands.gpg;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptController {

    @FXML
    private TextField txtRecipient;

    @FXML
    private TextArea txtPlainText;

    @FXML
    private TextArea txtEncrypted;

    @FXML
    public void initialize() {
        log.info("Initialize EncryptController");
    }

    @FXML
    private void handleEncrypt() {

        String recipient = txtRecipient.getText();

        log.info("Cifrando para {}", recipient);

        // TODO:
        // String result = encryptViewModel.encrypt(...)
        // txtEncrypted.setText(result);
    }
}