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
        SIComposite siComposite = requirementService.getRequirementEntity(codRequirement).getMainForm();
        return extratoGenerator.generate(siComposite);
    }

}
