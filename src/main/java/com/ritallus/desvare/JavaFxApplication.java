package com.ritallus.desvare;

import java.io.IOException;
import java.util.Objects;

import com.ritallus.desvare.bootstrap.AppBootstrap;
import com.ritallus.desvare.ui.controller.sidebar.SidebarController;
import com.ritallus.desvare.ui.controller.tabs.MainTabController;
import com.ritallus.desvare.ui.viewmodel.tabs.MainTabsViewModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaFxApplication extends Application {


    private static final double MIN_WIDTH = 1280.0;
    private static final double MIN_HEIGHT = 720.0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/fxml/main.fxml"));

        MainTabsViewModel mainTabViewModel = new MainTabsViewModel();

        AppBootstrap.getInstance();

        loader.setControllerFactory(type -> {
            if (type == SidebarController.class) {
                return new SidebarController(mainTabViewModel);
            } else if (type == MainTabController.class) {
                return new MainTabController(mainTabViewModel);
            } else {
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Scene scene = new Scene(loader.load(), MIN_WIDTH, MIN_HEIGHT);
        scene.getStylesheets()
                .add(Objects.requireNonNull(getClass().getResource("/ui/css/global.css")).toExternalForm());

        try {
            stage.getIcons().addAll(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/appicons/icon16.png"))),
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/appicons/icon32.png"))),
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/appicons/icon64.png"))),
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/appicons/icon256.png")))
            );
        } catch (Exception e) {
            log.error("No se pudieron cargar los iconos de la UI", e);
        }

        stage.setTitle("DesvarApp");
        stage.setScene(scene);
        stage.show();
    }
}
