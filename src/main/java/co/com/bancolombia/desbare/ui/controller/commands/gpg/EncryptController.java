package co.com.bancolombia.desbare.ui.controller.commands.gpg;

import co.com.bancolombia.desbare.ui.viewmodel.gpg.EncryptViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptController {

    private final EncryptViewModel encryptViewModel;
    @FXML
    private TextField txtRecipient;
    @FXML
    private TextArea txtPlainText;
    @FXML
    private TextArea txtEncrypted;

    public EncryptController(EncryptViewModel encryptViewModel) {
        this.encryptViewModel = encryptViewModel;
    }

    @FXML
    public void initialize() {
        log.info("Initialize EncryptController");

        txtRecipient.textProperty().bindBidirectional(
                encryptViewModel.getRecipient()
        );

        txtPlainText.textProperty().bindBidirectional(
                encryptViewModel.getPlainText()
        );

        txtEncrypted.textProperty().bindBidirectional(
                encryptViewModel.getEncrypted()
        );
    }

    @FXML
    private void handleEncrypt() {

        // TODO:
        // String result = encryptViewModel.encrypt(...)
        encryptViewModel.encrypt();

        // txtEncrypted.setText(result);
    }


}