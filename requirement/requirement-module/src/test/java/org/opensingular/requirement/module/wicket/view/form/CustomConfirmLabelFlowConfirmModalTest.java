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

package org.opensingular.requirement.module.wicket.view.form;

import org.apache.wicket.util.tester.WicketTestCase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.opensingular.requirement.commons.wicket.view.form.MockCustomConfirmLabelFlowConfirmModalPage;

@RunWith(MockitoJUnitRunner.class)
public class CustomConfirmLabelFlowConfirmModalTest extends WicketTestCase {

    @Mock
    private AbstractFormPage abstractFormPage;

    @Mock
    private FormPageExecutionContext config;

    @Test
    public void shouldUseResourceLabel() {
        Mockito.when(abstractFormPage.getConfig()).thenReturn(config);
        MockCustomConfirmLabelFlowConfirmModalPage page;
        page = new MockCustomConfirmLabelFlowConfirmModalPage( "Análisar Outorga", abstractFormPage);
        page.confirmModal.getModalBorder().setVisible(true);
        tester.startPage(page);
        Assertions.assertThat(tester.getLastResponseAsString()).contains("Concluir Análise");
    }
}