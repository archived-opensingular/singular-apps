package org.opensingular.server.commons.wicket.view.util;

import org.opensingular.server.commons.wicket.buttons.DiffLink;
import org.opensingular.server.commons.wicket.buttons.ViewVersionLink;

import java.util.HashMap;
import java.util.Map;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class ModuleButtonFactory {

    private ActionContext context = new ActionContext();

    public ModuleButtonFactory(ActionContext context, Map<String, String> param) {
        this.context = new ActionContext(context);
        for (Map.Entry<String, String> s : param.entrySet()) {
            this.context.setParam(s.getKey(), s.getValue());
        }
    }

    public ModuleButtonFactory(ActionContext context) {
        this(context, new HashMap<>());
    }

    public DiffLink getDiffButton(String id) {
        this.context.setDiffEnabled(true);
        return new DiffLink(id, $m.ofValue("Visualizar Diferenças"), context);
    }

    public ViewVersionLink getViewVersionButton(String id, Long formVersionId) {
        this.context.setFormVersionId(formVersionId);
        return new ViewVersionLink(id, $m.ofValue("Versão anterior do formulário"), context);
    }

    private String getDiffViewURL() {
        return null;
    }

    private String getDraftURL() {
        return null;
    }


}
