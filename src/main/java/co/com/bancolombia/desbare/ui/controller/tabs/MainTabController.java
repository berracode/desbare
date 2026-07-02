package co.com.bancolombia.desbare.ui.controller.tabs;

import java.io.IOException;

import co.com.bancolombia.desbare.core.model.ToolTab;
import co.com.bancolombia.desbare.ui.viewmodel.tabs.MainTabsViewModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainTabController {

    private final MainTabsViewModel mainTabsViewModel;

    @FXML
    private TabPane toolMainTabPane;

    public MainTabController(MainTabsViewModel mainTabsViewModel) {
        this.mainTabsViewModel = mainTabsViewModel;
    }

    @FXML
    public void initialize() {

        mainTabsViewModel.toolTabProperty()
                .addListener((obs, old, selected) -> {
                    log.info("selected deployment para deployment tab: {}", selected.getName());
                    if (selected != null) {
                        log.info("Abriendo el deploymentStateView model con el nuevo deployment seleccionado: " +
                                         "{}", selected.getName());

                        openDeploymentTab(selected);
                    }
                });

        toolMainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, old, selectedTab) -> {
            if (selectedTab != null) {
                Object userData = selectedTab.getUserData();
                if (userData instanceof ToolTab toolTab) {
                    mainTabsViewModel.setToolTab(toolTab);
                    log.info("Estado actualizado desde tab seleccionada: {}", toolTab.getName());
                }
            }
        });

    }

    private void openDeploymentTab(ToolTab toolTab) {
        var name = toolTab.getName();
        var existing = toolMainTabPane.getTabs().stream()
                .filter(t -> t.getText().equalsIgnoreCase(name))
                .filter(t -> ((ToolTab) t.getUserData()).getName().equals(toolTab.getName()))
                .findFirst();
        if (existing.isPresent()) {
            toolMainTabPane.getSelectionModel().select(existing.get());
            log.info("La pestaña [{}] ya esta abierta no se carga nuevamente desde cero.", existing.get().getText());
            return;
        }

        log.info("Instanciando DeploymentTabItemViewModel");
        var fxmlPath = resolveFxmlPath(toolTab.getName());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

//            loader.setControllerFactory(type -> {
//                if (type == DeploymentTabController.class) {
//                    log.info("Instanciando deploymentTabController");
//                    return new DeploymentTabController(deploymentTabItemViewModel, context);
//                }
//                try {
//                    return type.getDeclaredConstructor().newInstance();
//                } catch (Exception e) {
//                    throw new RuntimeException("No puedo instanciar " + type, e);
//                }
//            });

            AnchorPane content = loader.load();
            Tab tab = new Tab(toolTab.getName(), content);
            tab.setUserData(toolTab);

            toolMainTabPane.getTabs().add(tab);
            toolMainTabPane.getSelectionModel().select(tab);

        } catch (IOException e) {
            throw new RuntimeException("Error cargando /fxml/tabs/toolTab-tab-content.fxml", e);
        }
    }

    private String resolveFxmlPath(String name) {
        return switch (name) {
            case "GPG" -> "/ui/fxml/commands/gpg/gpg.fxml";
            case "BASE64" -> "/ui/fxml/commands/base64/base64.fxml";
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };

    }
}
