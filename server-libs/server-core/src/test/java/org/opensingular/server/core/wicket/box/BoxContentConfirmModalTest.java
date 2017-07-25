package org.opensingular.server.core.wicket.box;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;


public class BoxContentConfirmModalTest {
    WicketTester tester;
    BoxContentConfirmModal boxContentConfirmModal;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();
        boxContentConfirmModal = new SerializableBoxContentConfirmModal();
    }

    @Test
    public void testRender() throws Exception {
        tester.startComponentInPage(boxContentConfirmModal);
    }

    @Test
    public void testShow() throws Exception {
        tester.startComponentInPage(boxContentConfirmModal);
        boxContentConfirmModal.show(null);
    }

    private static class SerializableBoxContentConfirmModal extends BoxContentConfirmModal<Serializable> {
        public SerializableBoxContentConfirmModal() {
            super(null, null);
        }
        @Override
        protected void onConfirm(AjaxRequestTarget target) {

        }
    }
}