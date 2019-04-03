package org.opensingular.studio.core.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

public class HeadRightButtonPanel extends Panel {

    private final CrudListContent.HeaderRightButton headerRightButton;

    HeadRightButtonPanel(CrudListContent.HeaderRightButton headerRightButton) {
        super("headerRightButtonPanel");
        this.headerRightButton = headerRightButton;
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.put("title", headerRightButton.getTitle());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        AbstractLink link = null;
        if (headerRightButton instanceof HeaderRightDownloadLink) {
            link = new Link<Void>("headerRightAction") {
                @Override
                public void onClick() {
                    ((HeaderRightDownloadLink)headerRightButton).onClick();
                }
            };
        } else if (headerRightButton instanceof HeaderRightAjaxLink) {
            link = new AjaxLink<Void>("headerRightAction") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    ((HeaderRightAjaxLink)headerRightButton).onAction(target);
                }
            };
        }

        if (link != null) {
            Label btnLabel = new Label("headerRightActionLabel", headerRightButton.getLabel());

            WebMarkupContainer btnIcon = new WebMarkupContainer("headerRigthActionIcon") {
                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.put("class", headerRightButton.getIcon());
                }
            };
            link.add(btnLabel);
            link.add(btnIcon);
            link.setVisible(headerRightButton.isVisible());
            this.add(link);
        }

    }
}
