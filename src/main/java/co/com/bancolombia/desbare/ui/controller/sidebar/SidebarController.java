package co.com.bancolombia.desbare.ui.controller.sidebar;

import co.com.bancolombia.desbare.core.model.ToolTab;
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

    }

    // -- ON ACTIONS
    @FXML
    private void handleOpenGpg() {
        mainTabsViewModel.setToolTab(new ToolTab("GPG"));
    }

    @FXML
    private void handleOpenBase64() {
        mainTabsViewModel.setToolTab(new ToolTab("BASE64"));


    }
}
