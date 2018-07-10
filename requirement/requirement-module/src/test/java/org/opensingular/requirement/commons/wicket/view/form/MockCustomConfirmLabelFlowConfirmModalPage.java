package org.opensingular.requirement.commons.wicket.view.form;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;
import org.opensingular.requirement.module.wicket.view.form.MockCustomConfirmLabelFlowConfirmModal;

public class MockCustomConfirmLabelFlowConfirmModalPage extends WebPage {
    public MockCustomConfirmLabelFlowConfirmModal confirmModal;

    public MockCustomConfirmLabelFlowConfirmModalPage(String transitioName, AbstractFormPage mockAbstractFormPage) {
        Form<Void> form = new Form<>("form");
        add(form);
        confirmModal = new MockCustomConfirmLabelFlowConfirmModal("confirmModal", transitioName, mockAbstractFormPage);
        form.add(confirmModal);
    }
}
