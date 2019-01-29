package org.opensingular.requirement.module.wicket.view.panel;

import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class ViewNotificationPage extends GenericWebPage<String> {
    public ViewNotificationPage(IModel<String> model) {
        super(model);
        add(new Label("content", model).setEscapeModelStrings(false));
    }
}