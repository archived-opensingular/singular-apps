package org.opensingular.server.commons.wicket.buttons;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

public class ViewVersionLink extends WebMarkupContainer {

    public ViewVersionLink(String id, IModel<String> labelModel, ActionContext context) {
        super(id);
        add(new Label("label", labelModel));
        this.add($b.attr("target", String.format("version%s", context.getFormVersionId().get())));
        this.add($b.attr("href", DispatcherPageUtil.buildFullURL(context)));
    }
}
