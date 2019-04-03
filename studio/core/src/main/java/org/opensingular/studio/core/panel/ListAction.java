package org.opensingular.studio.core.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.lib.wicket.util.datatable.column.BSActionPanel;

import java.io.Serializable;

public interface ListAction extends Serializable {

    void configure(BSActionPanel.ActionConfig<SInstance> config);

    void onAction(AjaxRequestTarget target, IModel<SInstance> model, CrudShellManager crudShellManager);

}
