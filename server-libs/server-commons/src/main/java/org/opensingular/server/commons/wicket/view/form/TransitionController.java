package org.opensingular.server.commons.wicket.view.form;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.lib.wicket.util.bootstrap.layout.BSContainer;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;

import java.io.Serializable;
import java.util.Map;

public interface TransitionController<T extends SType<?>> extends Serializable {
    Class<T> getType();

    boolean isValidatePageForm();

    default Map<String, String> getFlowParameters(SIComposite pageInstance, SInstance transitionInstance) {
        return null;
    }

    default void onCreateInstance(SIComposite pageInstance, SInstance transitionInstance) {}

    default void onTransition(SIComposite pageIserienstance, SInstance transitionInstance) {}

    default boolean onShow(SIComposite pageInstance, SInstance transitionInstance, BSModalBorder modal, AjaxRequestTarget ajaxRequestTarget) {
        return true;
    }

    default void appendExtraContent(BSContainer extraContainer) {}
}
