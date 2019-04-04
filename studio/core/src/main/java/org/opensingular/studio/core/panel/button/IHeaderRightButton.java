package org.opensingular.studio.core.panel.button;

import org.apache.wicket.markup.html.link.AbstractLink;

import javax.annotation.Nonnull;
import java.io.Serializable;

public interface IHeaderRightButton extends Serializable {

    String getLabel();

    default String getTitle() {
        return getLabel();
    }

    String getIcon();

    default boolean isVisible() {
        return true;
    }

    @Nonnull
    AbstractLink createButton(String id);
}
