package co.com.bancolombia.desbare.ui.controller.commands.gpg;

import co.com.bancolombia.desbare.ui.viewmodel.gpg.DecryptViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecryptController {

    private final DecryptViewModel viewModel;


    @FXML
    private PasswordField txtPassphrase;

    @FXML
    private TextArea txtEncryptedText;

    @FXML
    private TextArea txtDecryptedText;


    @FXML
    private TextField txtFingerprint;

    public DecryptController(DecryptViewModel viewModel) {
        this.viewModel = viewModel;
    }


    @FXML
    public void initialize() {
        log.info("Initialize DecryptController");

        txtFingerprint.textProperty()
                .bindBidirectional(
                        viewModel.getFingerprint()
                );

        txtPassphrase.textProperty()
                .bindBidirectional(
                        viewModel.getPassphrase()
                );

        txtEncryptedText.textProperty()
                .bindBidirectional(
                        viewModel.getEncryptedText()
                );

        txtDecryptedText.textProperty()
                .bind(
                        viewModel.getDecryptedText()
                );

    }

    @FXML
    private void handleDecrypt() {

        viewModel.decrypt();
    }

}