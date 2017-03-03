package org.opensingular.server.module;

import org.opensingular.server.module.workspace.SingularItemBox;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceConfiguration {

    private List<SingularItemBox> itemBoxes = new ArrayList<>();

    public WorkspaceConfiguration add(SingularItemBox itemBox) {
        itemBoxes.add(itemBox);
        return this;
    }

    List<SingularItemBox> getItemBoxes() {
        return itemBoxes;
    }
}
