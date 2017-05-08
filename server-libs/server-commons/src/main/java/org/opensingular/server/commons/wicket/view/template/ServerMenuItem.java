package org.opensingular.server.commons.wicket.view.template;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.lib.wicket.util.menu.MetronicMenuItem;
import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.wicket.view.util.ActionContext;

public class ServerMenuItem extends MetronicMenuItem {


    public ServerMenuItem(Icone icon, String title, Class<? extends IRequestablePage> responsePageClass, IRequestablePage page, PageParameters parameters) {
        super(icon, title, responsePageClass, page, parameters);
    }


    @Override
    protected boolean configureActiveItem() {
        return RequestCycle.get().getRequest().getRequestParameters().getParameterValue(ActionContext.ITEM_PARAM_NAME).toString("").equals(this.title);
    }
}
