package org.opensingular.studio.core.panel.action;

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.lib.wicket.util.datatable.column.BSActionPanel;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.studio.core.definition.StudioDefinition;
import org.opensingular.studio.core.panel.CrudShellManager;

public class DeleteAction implements ListAction {

    private final StudioDefinition studioDefinition;
    private final CrudShellManager crudShellManager;

    public DeleteAction(StudioDefinition studioDefinition, CrudShellManager crudShellManager) {
        this.studioDefinition = studioDefinition;
        this.crudShellManager = crudShellManager;
    }

    @Override
    public void configure(BSActionPanel.ActionConfig<SInstance> config) {
        config.iconeModel(Model.of(DefaultIcons.TRASH));
        config.labelModel(Model.of("Remover"));
    }

    @Override
    public void onAction(AjaxRequestTarget target, IModel<SInstance> model, CrudShellManager crudShellManager) {
        this.crudShellManager.addConfirm("Tem certeza que deseja excluir?", target, (ajaxRequestTarget) -> {
            studioDefinition.getRepository().delete(FormKey.from(model.getObject()));
            this.crudShellManager.addToastrMessage(ToastrType.INFO, "Item excluído com sucesso.");
            this.crudShellManager.update(ajaxRequestTarget);
        });
    }

}
