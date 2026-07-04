package co.com.bancolombia.desbare.ui.controller.commands.base64;

import co.com.bancolombia.desbare.ui.viewmodel.base64.Base64ToolViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncodeController {

    @FXML
    private TextArea txtInputText;

    @FXML
    private TextArea txtOutputBase64;

    private final Base64ToolViewModel viewModel;

    public EncodeController(Base64ToolViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        log.info("Inicializando EncodeController con bindings reactivos");

        // Binding bidireccional para la entrada (lo que escribe el usuario va directo al VM)
        // significa que el text area pintará lo que tenga el view model y el view model tendrá lo que tenga el text área
        txtInputText.textProperty().bindBidirectional(viewModel.encodeInputProperty());

        // Binding unidireccional para la salida (lo que procese el VM se pinta solo en el TextArea readonly)
        //significa que el textarea de salida escucha lo que tenga en view model y lo pinta
        txtOutputBase64.textProperty().bind(viewModel.encodeOutputProperty());
    }

    @FXML
    void handleEncode(ActionEvent event) {
        // El controlador no calcula nada, solo le avisa al ViewModel que ejecute la acción
        viewModel.encode();
    }
}