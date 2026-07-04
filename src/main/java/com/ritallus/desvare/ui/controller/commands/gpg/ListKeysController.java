package com.ritallus.desvare.ui.controller.commands.gpg;

import com.ritallus.desvare.ui.viewmodel.gpg.ListKeysViewModel;
import com.ritallus.desvare.ui.viewmodel.gpg.dto.GpgKeyDto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

        // 2. HACER EL FINGERPRINT SELECCIONABLE Y COPIABLE
        colId.setCellFactory(column -> createSelectableCell());
        // 3. HACER EL BASE64 SELECCIONABLE, COMPLETO Y CON SCROLL HORIZONTAL (SIN ENGORDAR LA FILA)
        colPublicKey.setCellFactory(column -> createSelectableCell());

        // 2. Unidirectional Binding: La tabla observa directamente la lista reactiva del VM
        tblKeys.setItems(viewModel.getKeysList());

        // 3. Carga inicial automática al montar la vista
        viewModel.loadKeys();
    }

    /**
     * Factory utilitario que reemplaza la celda común por un TextField plano e inmutable
     */
    private TableCell<GpgKeyDto, String> createSelectableCell() {
        return new TableCell<>() {
            private final TextField textField = new TextField();

            {
                textField.setEditable(false); // Estrictamente de solo lectura (no actualizable)

                // Quitamos el diseño nativo de caja de texto para que parezca una celda plana de tabla
                textField.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-text-fill: inherit; " + // Hereda el color de la fila (blanco o azul de acento si está seleccionada)
                                "-fx-padding: 0; " +
                                "-fx-font-family: 'Consolas', 'Monaco', monospace; " + // Look de terminal limpio
                                "-fx-font-size: 12px;"
                );
                /*
                textField.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-background-insets: 0; " +        // Elimina el marco o sombras de fondo anidadas
                                "-fx-background-radius: 0; " +
                                "-fx-border-color: transparent; " +    // Forzar a que no dibuje ninguna línea de borde
                                "-fx-border-width: 0; " +
                                "-fx-padding: 0; " +                   // Se funde perfectamente con el alineado de la celda
                                "-fx-text-fill: inherit; " +           // Mantiene el color blanco o azul si la fila está seleccionada
                                "-fx-font-family: 'Consolas', 'Monaco', monospace; " +
                                "-fx-font-size: 12px;"
                );*/

                // Si el usuario hace clic en el TextField, le avisa a la tabla que seleccione la fila entera
                textField.setOnMouseClicked(e -> {
                    if (getTableRow() != null) {
                        tblKeys.getSelectionModel().select(getTableRow().getIndex());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    textField.setText(item);
                    textField.positionCaret(0);
                    setGraphic(textField);
                }
            }
        };
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