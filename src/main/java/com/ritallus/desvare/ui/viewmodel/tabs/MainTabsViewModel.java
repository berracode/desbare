package com.ritallus.desvare.ui.viewmodel.tabs;

import com.ritallus.desvare.core.domain.model.ToolTab;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainTabsViewModel {
    private final ObjectProperty<ToolTab> toolTab = new SimpleObjectProperty<>();

    public ToolTab getToolTab() {
        return toolTab.get();
    }

    public void setToolTab(ToolTab toolTab) {
        log.info("Asignando el ToolTab {}", toolTab.getName());
        this.toolTab.set(toolTab);
    }

    public ObjectProperty<ToolTab> toolTabProperty() {
        log.info("Escuchando el ToolTab {}", toolTab.getName());
        return toolTab;
    }
}
