package org.opensingular.studio.core.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;

public abstract class HeaderRightAjaxLink implements CrudListContent.HeaderRightButton {

    public abstract void onAction(AjaxRequestTarget target);

}
