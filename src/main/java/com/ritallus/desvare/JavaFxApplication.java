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
import javafx.stage.Stage;

public class JavaFxApplication extends Application {


    private static final double MIN_WIDTH = 1280.0;
    private static final double MIN_HEIGHT = 720.0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/fxml/main.fxml"));

        MainTabsViewModel mainTabViewModel = new MainTabsViewModel();

        AppBootstrap bootstrap = AppBootstrap.getInstance();

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
        stage.setTitle("DesvarApp");
        stage.setScene(scene);
        stage.show();
    }
}
