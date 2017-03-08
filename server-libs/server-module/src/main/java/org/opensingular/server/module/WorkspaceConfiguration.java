package org.opensingular.server.module;

import org.opensingular.server.module.workspace.ItemBoxFactory;

import java.util.ArrayList;
import java.util.List;



/**
 * Configuration object for module {@link ItemBoxFactory} registrations.
 *
 */
public class WorkspaceConfiguration {

    private List<ItemBoxFactory> itemBoxes = new ArrayList<>();

    /**
     * Register a singule {@link ItemBoxFactory}
     * @param itemBox     the
     * @return
     */
    public WorkspaceConfiguration add(ItemBoxFactory itemBox) {
        itemBoxes.add(itemBox);
        return this;
    }

    List<ItemBoxFactory> getItemBoxes() {
        return itemBoxes;
    }
}
