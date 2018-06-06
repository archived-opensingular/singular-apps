package org.opensingular.requirement.commons.wicket.view.form;

import org.opensingular.lib.wicket.util.modal.BSModalBorder;

public class MockCustomConfirmLabelFlowConfirmModal extends AbstractFlowConfirmModal {

    public MockCustomConfirmLabelFlowConfirmModal(String id, String transition, AbstractFormPage formPage) {
        super(id, transition, formPage);
    }

    @Override
    void addComponentsToModalBorder(BSModalBorder modalBorder) {
        addDefaultConfirmButton(modalBorder);
    }

}