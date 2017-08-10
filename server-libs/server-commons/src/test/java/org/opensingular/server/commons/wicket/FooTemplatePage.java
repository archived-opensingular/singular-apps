package org.opensingular.server.commons.wicket;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.server.commons.wicket.view.template.ServerTemplate;

public class FooTemplatePage extends ServerTemplate {
    @Override
    protected IModel<String> getContentTitle() {
        return null;
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return new Model<>();
    }

    @Override
    protected boolean isWithMenu() {
        return true;
    }
}
