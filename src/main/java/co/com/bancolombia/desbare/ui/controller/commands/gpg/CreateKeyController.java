package co.com.bancolombia.desbare.ui.controller.commands.gpg;

import co.com.bancolombia.desbare.ui.viewmodel.gpg.CreateKeyViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateKeyController {

    private final CreateKeyViewModel createKeyViewModel;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassphrase;

    @FXML
    private Label lblStatus;


    public CreateKeyController(CreateKeyViewModel createKeyViewModel) {
        this.createKeyViewModel = createKeyViewModel;
    }

    @FXML
    public void initialize() {
        log.info("Initialize CreateKeyController");


        txtName.textProperty().bindBidirectional(
                createKeyViewModel.getName()
        );

        txtEmail.textProperty().bindBidirectional(
                createKeyViewModel.getEmail()
        );

        txtPassphrase.textProperty().bindBidirectional(
                createKeyViewModel.getPassphrase()
        );

        lblStatus.textProperty().bind(
                createKeyViewModel.getStatus()
        );
    }

    @FXML
    private void handleGenerateKey() {

        log.info("Solicitando generación de llave");

        createKeyViewModel.generate();
    }

}