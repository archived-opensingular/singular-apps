package org.opensingular.server.commons.box.action.defaults;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.action.AbstractURLPopupBoxItemAction;
import org.opensingular.server.commons.form.FormAction;

public class AnalyseAction extends AbstractURLPopupBoxItemAction {


    public AnalyseAction(BoxItemData line) {
        super("analyse", "Analisar", DefaultIcons.PENCIL, FormAction.FORM_ANALYSIS, line);
    }
}
