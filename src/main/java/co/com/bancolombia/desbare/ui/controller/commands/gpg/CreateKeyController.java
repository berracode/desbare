package co.com.bancolombia.desbare.ui.controller.commands.gpg;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateKeyController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassphrase;

    @FXML
    public void initialize() {
        log.info("Initialize CreateKeyController");
    }

    @FXML
    private void handleGenerateKey() {

        String name = txtName.getText();
        String email = txtEmail.getText();

        log.info("Generando llave para {} - {}", name, email);

        // TODO:
        // createKeyViewModel.generate(name, email, txtPassphrase.getText());
    }
}