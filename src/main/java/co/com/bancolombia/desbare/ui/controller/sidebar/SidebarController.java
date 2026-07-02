package co.com.bancolombia.desbare.ui.controller.sidebar;

import co.com.bancolombia.desbare.core.domain.model.ToolTab;
import co.com.bancolombia.desbare.ui.viewmodel.tabs.MainTabsViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SidebarController {

    private final MainTabsViewModel mainTabsViewModel;

    @FXML
    private Button btnGpg;
    @FXML
    private Button btnBase64;

    public SidebarController(MainTabsViewModel mainTabsViewModel) {
        this.mainTabsViewModel = mainTabsViewModel;
    }

    @FXML
    public void initialize() {
        log.info("initialize sidebar");


        mainTabsViewModel.toolTabProperty().addListener((obs, old, selected) -> {
            log.info("Actualizando color sidebar");
            updateActiveButton(selected);
        });


    }

    private void updateActiveButton(ToolTab toolTab) {

        log.info("Actualizando botón activo: {}", toolTab.getName());

        btnGpg.getStyleClass().remove("tool-active");
        btnBase64.getStyleClass().remove("tool-active");

        switch (toolTab.getName()) {
            case "GPG" -> btnGpg.getStyleClass().add("tool-active");
            case "BASE64" -> btnBase64.getStyleClass().add("tool-active");
        }

        log.info("GPG classes: {}", btnGpg.getStyleClass());
        log.info("BASE64 classes: {}", btnBase64.getStyleClass());
    }

    // -- ON ACTIONS
    @FXML
    private void handleOpenGpg() {
        log.info("Abriendo gpg");
        mainTabsViewModel.setToolTab(new ToolTab("GPG"));
    }

    @FXML
    private void handleOpenBase64() {
        log.info("Abriendo BASE64");
        mainTabsViewModel.setToolTab(new ToolTab("BASE64"));
    }
}
