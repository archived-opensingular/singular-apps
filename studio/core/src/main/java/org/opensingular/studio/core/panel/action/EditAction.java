package org.opensingular.studio.core.panel.action;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SInstance;
import org.opensingular.lib.wicket.util.datatable.column.BSActionPanel;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.studio.core.panel.CrudEditContent;
import org.opensingular.studio.core.panel.CrudShellManager;

public class EditAction implements ListAction {

    private final CrudShellManager crudShellManager;

    public EditAction(CrudShellManager crudShellManager) {
        this.crudShellManager = crudShellManager;
    }

    @Override
    public void configure(BSActionPanel.ActionConfig<SInstance> config) {
        config.iconeModel(Model.of(DefaultIcons.PENCIL));
        config.labelModel(Model.of("Editar"));
    }

    @Override
    public void onAction(AjaxRequestTarget target, IModel<SInstance> model, CrudShellManager crudShellManager) {
        CrudEditContent crudEditContent = this.crudShellManager.makeEditContent(this.crudShellManager.getCrudShellContent(), model);
        this.crudShellManager.replaceContent(target, crudEditContent);
    }
}
