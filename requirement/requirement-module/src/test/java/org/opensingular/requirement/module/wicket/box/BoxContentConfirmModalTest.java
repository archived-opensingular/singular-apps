/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module.wicket.box;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;


public class BoxContentConfirmModalTest {
    WicketTester           tester;
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