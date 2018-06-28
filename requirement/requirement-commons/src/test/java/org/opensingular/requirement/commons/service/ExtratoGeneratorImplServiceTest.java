package org.opensingular.requirement.commons.service;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.opensingular.form.SDictionary;
import org.opensingular.form.SIComposite;
import org.opensingular.form.type.core.SIString;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.requirement.commons.extrato.ExtratoExtratoGeneratorImpl;
import org.opensingular.requirement.commons.extrato.ExtratoGenerator;
import org.opensingular.requirement.commons.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.commons.service.dto.STypeMock;
import org.opensingular.ws.wkhtmltopdf.client.MockHtmlToPdfConverter;
import org.springframework.web.util.HtmlUtils;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class ExtratoGeneratorImplServiceTest {

    public static final String JOAQUIM = "Joaquim";

    @InjectMocks
    private ExtratoGeneratorService extratoGeneratorService = new ExtratoGeneratorService();

    @Spy
    private ExtratoGenerator extratoGenerator = new ExtratoExtratoGeneratorImpl();

    @Mock
    private RequirementService<RequirementEntity, RequirementInstance> requirementService;

    @Spy
    private HtmlToPdfConverter htmlToPdfConverter = new MockHtmlToPdfConverter();

    @Mock
    private RequirementInstance requirement;

    @Before
    public void configure() {
        SIComposite siComposite = SDictionary.create().newInstance(STypeMock.class);


        Optional<SIString> nome =
                siComposite.findField(STypeMock.class, i -> i.nome);
        nome.ifPresent(field -> field.setValue(JOAQUIM));

        when(requirement.getMainForm()).thenReturn(siComposite);
        when(requirementService.getRequirement(1L))
                .thenReturn(requirement);
    }

    @Test
    public void testExtratoPdfGenerator2() {
        extratoGeneratorService.generatePdfFile(1L);
        ArgumentCaptor<HtmlToPdfDTO> value = ArgumentCaptor.forClass(HtmlToPdfDTO.class);
        verify(htmlToPdfConverter).convert(value.capture());
        assertTrue("Esperado que haja 'Joaquim' no corpo do pdf gerado", value.getValue().getBody().contains(HtmlUtils.htmlEscape(JOAQUIM)));
    }


    @Test
    public void testExtratoPdfGenerator() {
        String htmlGenerated = extratoGeneratorService.generateHtml(1L);
        assertTrue("Esperado que haja 'Joaquim' no corpo do pdf gerado", htmlGenerated.contains(HtmlUtils.htmlEscape(JOAQUIM)));

    }
}
