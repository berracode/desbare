package co.com.bancolombia.desbare.ui.controller.commands.base64;

import co.com.bancolombia.desbare.bootstrap.AppBootstrap;
import co.com.bancolombia.desbare.ui.viewmodel.base64.Base64ToolViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Base64Controller {

    @FXML
    private TabPane commandTabPane;

    @FXML
    public void initialize() {
        log.info("INITIALIZE BASE64 TAB CONTROLLER");

        commandTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                log.info("Base64 Cambió a tab: {}", newTab.getText());
            }
        });

        initializeCommandsTabsView();
    }

    private void initializeCommandsTabsView() {
        log.info("Iniciando vistas de Base64");
        for (Tab tab : commandTabPane.getTabs()) {
            handleTabSelection(tab);
        }
    }

    private void handleTabSelection(Tab newTab) {
        switch (newTab.getText()) {
            case "Codificar" -> loadCommandView("encode.fxml", newTab.getText());
            case "Decodificar" -> loadCommandView("decode.fxml", newTab.getText());
        }
    }

    private void loadCommandView(String fxmlPath, String tabTitle) {
        try {
            var fullPath = getClass().getResource("/ui/fxml/commands/base64/" + fxmlPath);
            log.info("Cargando vista Base64: {}", fullPath);

            FXMLLoader loader = new FXMLLoader(fullPath);

            loader.setControllerFactory(type -> {
                AppBootstrap bootstrap = AppBootstrap.getInstance();
                Base64ToolViewModel base64Vm = bootstrap.base64ToolViewModel();
                if (type == EncodeController.class) {
                    return new EncodeController(base64Vm);
                } else if (type == DecodeController.class) {
                    return new DecodeController(base64Vm);
                }

                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Error en ControllerFactory de Base64 para: " + type.getName(), e);
                }
            });

            Node view = loader.load();

            var tabOptional = commandTabPane.getTabs().stream()
                    .filter(tab -> tab.getText().equals(tabTitle))
                    .findFirst();

            if (tabOptional.isEmpty()) {
                throw new RuntimeException("No se encontró la tab: " + tabTitle);
            }

            tabOptional.get().setContent(view);

        } catch (IOException e) {
            log.error("Error crítico cargando FXML de Base64", e);
            e.printStackTrace();
        }
    }
}