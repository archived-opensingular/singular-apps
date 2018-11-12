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

package org.opensingular.requirement.module.service;

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
import org.opensingular.form.SType;
import org.opensingular.form.type.core.SIString;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.requirement.commons.service.dto.STypeMock;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.extrato.ExtratoGenerator;
import org.opensingular.requirement.module.extrato.ExtratoGeneratorImpl;
import org.opensingular.ws.wkhtmltopdf.client.MockHtmlToPdfConverter;
import org.springframework.web.util.HtmlUtils;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class ExtratoGeneratorImplServiceTest {

    public static final String JOAQUIM = "Joaquim";

    @InjectMocks
    private ExtratoGeneratorService extratoGeneratorService = new ExtratoGeneratorService();

    @Spy
    private ExtratoGenerator extratoGenerator = new ExtratoGeneratorImpl();

    @Mock
    private RequirementService requirementService;

    @Spy
    private HtmlToPdfConverter htmlToPdfConverter = new MockHtmlToPdfConverter();

    @Mock
    private RequirementInstance requirement;

    @Mock
    private RequirementDefinition requirementDefinition;

    @Before
    public void configure() {
        SIComposite siComposite = SDictionary.create().newInstance(STypeMock.class);


        Optional<SIString> nome =
                siComposite.findField(STypeMock.class, i -> i.nome);
        nome.ifPresent(field -> field.setValue(JOAQUIM));

        when(requirement.getRequirementDefinition()).thenReturn(requirementDefinition);
        when(requirementDefinition.getMainForm()).thenReturn((Class) siComposite.getType().getClass());
        when(requirement.getForm()).thenReturn(Optional.of(siComposite));
        when(requirementService.loadRequirementInstance(1L))
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
