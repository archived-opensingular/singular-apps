package org.opensingular.server.module;

import org.opensingular.server.module.workspace.ItemBoxFactory;

import java.util.*;


/**
 * Configuration object for module {@link ItemBoxFactory} registrations.
 *
 */
public class WorkspaceConfiguration {

    private Map<String, ItemBoxFactory> itemBoxes = new LinkedHashMap<>();

    /**
     * Register a single {@link ItemBoxFactory}
     * @param itemBox     the
     * @return
     */
    public WorkspaceConfiguration addBox(ItemBoxFactory itemBox) {
        itemBoxes.put(UUID.randomUUID().toString(), itemBox);
        return this;
    }


    Map<String, ItemBoxFactory> getItemBoxes() {
        return itemBoxes;
    }
}
