package org.opensingular.server.single.page;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.server.core.wicket.box.BoxPage;
import org.opensingular.server.module.wicket.view.util.dispatcher.DispatcherPage;

public class SingleAppPage extends WebPage {
    public SingleAppPage() {
        throw new RestartResponseException(BoxPage.class);
    }

    public SingleAppPage(PageParameters parameters) {
        super(parameters);
        if (parameters.get("dispatch").toBoolean(false)) {
            throw new RestartResponseException(DispatcherPage.class, parameters);
        } else {
            throw new RestartResponseException(BoxPage.class, parameters);
        }
    }
}