package org.opensingular.requirement.commons.wicket.view.form;

import org.apache.wicket.util.tester.WicketTestCase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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