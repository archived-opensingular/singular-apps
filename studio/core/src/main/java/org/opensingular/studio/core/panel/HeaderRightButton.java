package org.opensingular.studio.core.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;

public interface HeaderRightButton extends Serializable {

    void onAction(AjaxRequestTarget target);

    default void onAction() {
    }

    String getLabel();

    default String getTitle() {
        return getLabel();
    }

    String getIcon();

    default boolean isVisible() {
        return true;
    }

}
