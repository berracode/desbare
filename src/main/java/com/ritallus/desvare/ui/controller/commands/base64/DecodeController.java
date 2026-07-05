package com.ritallus.desvare.ui.controller.commands.base64;

import com.ritallus.desvare.ui.controller.custom.button.LoadingButton;
import com.ritallus.desvare.ui.viewmodel.base64.Base64ToolViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecodeController {

    private final Base64ToolViewModel viewModel;
    @FXML
    private TextArea txtInputBase64;
    @FXML
    private TextArea txtOutputText;
    @FXML
    private LoadingButton btnEncode;

    @FXML
    private Label lblError;

    public DecodeController(Base64ToolViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        log.info("Inicializando DecodeController con bindings reactivos");
        btnEncode.loadingProperty().bind(viewModel.isDecodingProperty());

        txtInputBase64.textProperty().bindBidirectional(viewModel.decodeInputProperty());
        txtOutputText.textProperty().bind(viewModel.decodeOutputProperty());

        lblError.textProperty().bind(viewModel.errorMessageProperty());

        // Si el mensaje está vacío, oculta el label por completo de la pantalla
        lblError.visibleProperty().bind(viewModel.errorMessageProperty().isNotEmpty());
        lblError.managedProperty().bind(lblError.visibleProperty());
    }

    @FXML
    void handleDecode(ActionEvent event) {
        viewModel.decode();
    }
}