package co.com.bancolombia.desbare.ui.viewmodel.tabs;

import co.com.bancolombia.desbare.core.model.ToolTab;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MainTabsViewModel {
    private final ObjectProperty<ToolTab> toolTab = new SimpleObjectProperty<>();

    public ToolTab getToolTab() {
        return toolTab.get();
    }

    public void setToolTab(ToolTab toolTab) {
        this.toolTab.set(toolTab);
    }

    public ObjectProperty<ToolTab> toolTabProperty() {
        return toolTab;
    }
}
