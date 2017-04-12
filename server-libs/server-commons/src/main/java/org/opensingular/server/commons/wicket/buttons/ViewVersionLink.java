package org.opensingular.server.commons.wicket.buttons;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

public class ViewVersionLink extends Panel {

    public ViewVersionLink(String id, IModel<String> labelModel, ActionContext context) {
        super(id);
        Link<String> link = new Link<String>("oldVersionLink") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.add($b.attr("target", String.format("version%s", context.getFormVersionId().get())));
                this.add($b.attr("href", DispatcherPageUtil.buildFullURL(context)));
                this.setBody(labelModel);
            }

            @Override
            public void onClick() {
            }
        };
        this.add(link);
    }
}
