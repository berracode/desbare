package com.ritallus.desvare.ui.controller.commands.gpg;

import java.io.IOException;

import com.ritallus.desvare.bootstrap.AppBootstrap;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GPGController {

    @FXML
    private TabPane commandTabPane;


    @FXML
    public void initialize() {
        log.info("INITIALIZE GPG TAB CONTROLLER");
        commandTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            log.info("tab: {}", newTab.getText());
            if (newTab != null) {
                log.info("tab {}", newTab.getText());
                //handleTabSelection(newTab);
            }
        });

        initializeCommandsTabsView();
        //Platform.runLater(() -> selectTabByText("Pods"));

    }

    private void initializeCommandsTabsView() {
        log.info("Iniciando todas las vistas tab");
        for (Tab tab : commandTabPane.getTabs()) {
            log.info("La tab '{}' inicia carga manualmente", tab.getText());
            handleTabSelection(tab); // <-- fuerza la carga
        }
    }

    private void handleTabSelection(Tab newTab) {
        switch (newTab.getText()) {
            case "Generar llave publica" -> loadCommandView("create-key.fxml", newTab.getText());
            case "Cifrar" -> loadCommandView("encrypted.fxml", newTab.getText());
            case "Descifrar" -> loadCommandView("decrypted.fxml", newTab.getText());
            case "Listar llaves" -> loadCommandView("keys-list.fxml", newTab.getText());
            default -> throw new RuntimeException("No existe vista para este Tab");
        }
    }

    private void loadCommandView(String fxmlPath, String tabTitle) {
        try {
            log.info("path: {}", fxmlPath);
            var fullPath = getClass().getResource("/ui/fxml/commands/gpg/" + fxmlPath);
            log.info("full path {}", fullPath);

            FXMLLoader loader = new FXMLLoader(fullPath);

            loader.setControllerFactory(type -> {

                AppBootstrap bootstrap =
                        AppBootstrap.getInstance();

                if (type == CreateKeyController.class) {
                    log.info("Creando instancia de CreateKeyController");
                    return new CreateKeyController(bootstrap.createKeyViewModel());
                } else if (type == EncryptController.class) {
                    return new EncryptController(bootstrap.encryptViewModel());
                } else if (type == DecryptController.class) {
                    return new DecryptController(bootstrap.decryptViewModel());
                } else if (type == ListKeysController.class) {
                    return new ListKeysController(bootstrap.listKeysViewModel());
                }

                try {
                    return type.getDeclaredConstructor()
                            .newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Node view = loader.load();

            var tabOptional = commandTabPane.getTabs().stream().filter(tab -> tab.getText().equals(tabTitle)).findFirst();
            if (tabOptional.isEmpty()) {
                throw new RuntimeException("No tab");
            }
            tabOptional.get().setContent(view);
            commandTabPane.getSelectionModel().select(tabOptional.get()); //esto no estaba
            
        } catch (IOException e) {
            log.error("Error cargando vistas de GPG", e);
            e.printStackTrace();
        }
    }
}
