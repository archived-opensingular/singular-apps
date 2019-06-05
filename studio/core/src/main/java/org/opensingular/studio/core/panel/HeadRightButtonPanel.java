package org.opensingular.studio.core.panel;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.studio.core.panel.button.IHeaderRightButton;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

public class HeadRightButtonPanel extends Panel {

    private final IHeaderRightButton headerRightButton;

    HeadRightButtonPanel(IHeaderRightButton headerRightButton) {
        super("headerRightButtonPanel");
        this.headerRightButton = headerRightButton;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        AbstractLink link = headerRightButton.createButton("headerRightAction");
        Label btnLabel = new Label("headerRightActionLabel", headerRightButton.getLabel());

        WebMarkupContainer btnIcon = new WebMarkupContainer("headerRigthActionIcon") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("class", headerRightButton.getIcon());
            }
        };

        link.add($b.onComponentTag((c, tag) -> tag.put("title", headerRightButton.getTitle())));
        link.add(btnLabel);
        link.add(btnIcon);
        link.setVisible(headerRightButton.isVisible());
        this.add(link);

    }
}
