package co.com.bancolombia.desbare.ui.controller.commands.gpg;

import co.com.bancolombia.desbare.ui.viewmodel.gpg.ListKeysViewModel;
import co.com.bancolombia.desbare.ui.viewmodel.gpg.dto.GpgKeyDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListKeysController {

    private final ListKeysViewModel viewModel;
    @FXML
    private TableView<GpgKeyDto> tblKeys;
    @FXML
    private TableColumn<GpgKeyDto, String> colId;
    @FXML
    private TableColumn<GpgKeyDto, String> colPublicKey;
    @FXML
    private TableColumn<GpgKeyDto, String> colName;
    @FXML
    private TableColumn<GpgKeyDto, String> colEmail;
    @FXML
    private TableColumn<GpgKeyDto, String> colCreatedAt;

    public ListKeysController(ListKeysViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        log.info("Inicializando ListKeysController y configurando columnas");

        // 1. Mapeamos las columnas con los campos exactos del DTO GpgKey
        colId.setCellValueFactory(new PropertyValueFactory<>("fingerprint"));
        colPublicKey.setCellValueFactory(new PropertyValueFactory<>("publicKeyBase64"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // 2. Unidirectional Binding: La tabla observa directamente la lista reactiva del VM
        tblKeys.setItems(viewModel.getKeysList());

        // 3. Carga inicial automática al montar la vista
        viewModel.loadKeys();
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        viewModel.loadKeys();
    }

    @FXML
    void handleCopyPublicKey(ActionEvent event) {
        GpgKeyDto selectedKey = tblKeys.getSelectionModel().getSelectedItem();
        if (selectedKey == null || selectedKey.getPublicKey() == null) {
            log.warn("Intento de copiado sin ninguna llave seleccionada");
            return;
        }

        try {
            // Acceso nativo al portapapeles del Sistema Operativo
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();

            // Colocamos la llave limpia completa original
            content.putString(selectedKey.getPublicKey());
            clipboard.setContent(content);

            log.info("Llave pública completa copiada al portapapeles para: {}", selectedKey.getEmail());
            // TODO: Podrías disparar una notificación flotante o cambiar un label de estado aquí
        } catch (Exception e) {
            log.error("Error al copiar al portapapeles", e);
        }
    }

    @FXML
    void handleExport(ActionEvent event) {
        var selectedKey = tblKeys.getSelectionModel().getSelectedItem();
        if (selectedKey != null) {
            log.info("Exportando llave pública para: {}", selectedKey.getEmail());
            // Lógica para exportar...
        }
    }

    @FXML
    void handleByDelete(ActionEvent event) {
        var selectedKey = tblKeys.getSelectionModel().getSelectedItem();
        if (selectedKey != null) {
            log.info("Solicitud para eliminar llave: {}", selectedKey.getFingerprint());
            // Lógica para invocar caso de uso de borrado...
        }
    }
}