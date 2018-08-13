/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.service;

import java.io.File;
import java.util.Optional;
import javax.inject.Inject;

import org.opensingular.form.SIComposite;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.requirement.module.extrato.ExtratoGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ExtratoGeneratorService {

    @Inject
    private HtmlToPdfConverter htmlToPdfConverter;

    @Inject
    private ExtratoGenerator extratoGenerator;

    @Inject
    private RequirementService<?, ?> requirementService;

    /**
     * Method responsible for generate the Pdf file of the requirement extrato.
     *
     * @param codRequirement The id of requirement.
     * @return Optinal containing file if exists.
     */
    public Optional<File> generatePdfFile(Long codRequirement) {
        return htmlToPdfConverter.convert(new HtmlToPdfDTO(generateHtml(codRequirement)));
    }

    /**
     * Method responsible for generate the HTML of the requirement extrato.
     *
     * @param codRequirement The id for requiriment.
     * @return String with html content.
     */
    public String generateHtml(Long codRequirement) {
        SIComposite siComposite = requirementService.getRequirement(codRequirement).getMainForm();
        return extratoGenerator.generate(siComposite);
    }

}
