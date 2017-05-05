package org.opensingular.server.commons.wicket.buttons;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.wicket.view.util.ActionContext;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

public class ViewVersionLink extends Panel {

    private ActionContext context;

    public ViewVersionLink(String id, IModel<String> labelModel, ActionContext context) {
        super(id);
        this.context = new ActionContext(context);
        this.context.setDiffEnabled(false);
        this.context.setFormAction(FormAction.FORM_ANALYSIS_VIEW);
        Link<String> link = new Link<String>("oldVersionLink") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.add($b.attr("target", String.format("version%s", ViewVersionLink.this.context.getFormVersionId().get())));
                this.add($b.attr("href", DispatcherPageUtil.buildFullURL(ViewVersionLink.this.context)));
                this.setBody(labelModel);
            }

            @Override
            public void onClick() {
            }
        };
        this.add(link);
    }
}
