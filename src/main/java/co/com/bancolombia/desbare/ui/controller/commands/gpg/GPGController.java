package co.com.bancolombia.desbare.ui.controller.commands.gpg;

import java.io.IOException;

import co.com.bancolombia.desbare.bootstrap.AppBootstrap;

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
                }

                try {
                    return type.getDeclaredConstructor()
                            .newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });


            // Ejemplo de controller factory manual:
//            loader.setControllerFactory(type -> {
//                if (podsViewModel == null) {
//                    podsViewModel = new PodsViewModel(AppBootstrap.getInstance().getPodService());
//                }
//                if (type == PodsContentController.class) {
//                    return new PodsContentController(podsViewModel);
//                } else if (type == LogsContentController.class) {
//                    var logViewModel = new LogsViewModel(AppBootstrap.getInstance().getLogsService());
//                    return new LogsContentController(podsViewModel, logViewModel);
//                }
//
//                try {
//                    return type.getDeclaredConstructor().newInstance();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });

            Node view = loader.load();

            // Object controller = loader.getController();
//            if (controller instanceof DeploymentTab) {
//                ((DeploymentTab) controller).setDeploymentName(deploymentTabItemViewModel.deploymentName());
//            }
//
//
//            DeploymentContext ctx =
//                    buildContextFromAppState(deploymentTabItemViewModel);
//
//            if (controller instanceof PodsContentController podsCtrl) {
//                podsCtrl.setDeploymentContext(ctx);
//            } else if (controller instanceof LogsContentController logsContentController) {
//                logsContentController.setDeploymentContext(ctx);
//
//            }

            var tabOptional =
                    commandTabPane.getTabs().stream().filter(tab -> tab.getText().equals(tabTitle))
                            .findFirst();//esto no estaba
            if (tabOptional.isEmpty()) {//esto no estaba
                throw new RuntimeException("No tab");
            }
            tabOptional.get().setContent(view);
            commandTabPane.getSelectionModel().select(tabOptional.get()); //esto no estaba


//            commandContent.getChildren().setAll(view);
//            AnchorPane.setTopAnchor(view, 0.0);
//            AnchorPane.setBottomAnchor(view, 0.0);
//            AnchorPane.setLeftAnchor(view, 0.0);
//            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            log.error("Error cargando vistas de GPG", e);
            e.printStackTrace();
        }
    }
}
