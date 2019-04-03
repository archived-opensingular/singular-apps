package org.opensingular.studio.core.panel.action;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.lib.wicket.util.datatable.column.BSActionPanel;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.studio.core.panel.CrudEditContent;
import org.opensingular.studio.core.panel.CrudShellManager;

public class ViewAction implements ListAction {

    private final CrudShellManager crudShellManager;

    public ViewAction(CrudShellManager crudShellManager) {
        this.crudShellManager = crudShellManager;
    }

    @Override
    public void configure(BSActionPanel.ActionConfig<SInstance> config) {
        config.iconeModel(Model.of(DefaultIcons.EYE));
        config.labelModel(Model.of("Visualizar"));
    }

    @Override
    public void onAction(AjaxRequestTarget target, IModel<SInstance> model, CrudShellManager crudShellManager) {
        CrudEditContent crudEditContent = this.crudShellManager
                .makeEditContent(this.crudShellManager.getCrudShellContent(), model);
        crudEditContent.setViewMode(ViewMode.READ_ONLY);
        this.crudShellManager.replaceContent(target, crudEditContent);
    }
}
