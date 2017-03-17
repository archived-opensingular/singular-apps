package org.opensingular.server.commons.wicket.view.util;

import org.apache.wicket.model.IModel;
import org.opensingular.server.commons.wicket.buttons.DiffLink;

import java.util.Map;

public class ModuleButtonFactory {

    private ActionContext context = new ActionContext();

    public ModuleButtonFactory(ActionContext context, Map<String, String> param) {
        this.context = context.clone();
    }

    public DiffLink getDiffButton(String id, IModel<String> label) {
        this.context.setDiffEnabled(true);
        return new DiffLink(id, label, context);

    }

    private String getDiffViewURL() {
        return null;
    }

    private String getDraftURL() {
        return null;
    }


}
