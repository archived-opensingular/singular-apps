package org.opensingular.studio.core.panel.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;

import javax.annotation.Nonnull;

public abstract class HeaderRightModalButton implements IHeaderRightButton {

    public abstract void onAction(AjaxRequestTarget target);

    @Nonnull
    @Override
    public AbstractLink createButton(String id) {
        return new AjaxLink<Void>(id) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onAction(target);
            }
        };
    }

    @Override
    public abstract Panel modalComponent(String id);
}
