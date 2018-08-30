package org.opensingular.requirement.module.persistence.filter;

import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.service.RequirementDefinitionService;
import org.opensingular.requirement.module.service.dto.FormDTO;
import org.opensingular.requirement.module.spring.security.AuthorizationService;
import org.opensingular.requirement.module.spring.security.SingularRequirementUserDetails;
import org.opensingular.requirement.module.wicket.SingularSession;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Cria o BoxFilter populando com os dados basicos
 */
public class BoxFilterFactory {
    @Inject
    @Named("formConfigWithDatabase")
    private SFormConfig<String> singularFormConfig;

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private RequirementDefinitionService requirementDefinitionService;

    private List<FormDTO> mainForms;

    public BoxFilter create(BoxDefinition box) {
        return box.createBoxFilter()
                .idUsuarioLogado(getIdUsuario())
                .idPessoa(getIdPessoa())
                .processesAbbreviation(getProcessesNames())
                .typesNames(getFormNames());
    }

    private List<String> getFormNames() {
        return listMainFormsWithPermission().stream().map(FormDTO::getName).collect(Collectors.toList());
    }

    private List<String> getProcessesNames() {
        return Flow.getDefinitions()
                .stream()
                .map(FlowDefinition::getName)
                .collect(Collectors.toList());
    }

    protected String getIdUsuario() {
        SingularRequirementUserDetails userDetails = SingularSession.get().getUserDetails();
        return Optional.ofNullable(userDetails)
                .map(SingularRequirementUserDetails::getUsername)
                .orElse(null);
    }

    protected String getIdPessoa() {
        SingularRequirementUserDetails userDetails = SingularSession.get().getUserDetails();
        return Optional.ofNullable(userDetails)
                .map(SingularRequirementUserDetails::getApplicantId)
                .orElse(null);
    }

    private List<FormDTO> listMainFormsWithPermission() {
        if (mainForms == null) {
            mainForms = new ArrayList<>();
            List<RequirementDefinition<?>> requirements = requirementDefinitionService.getRequirements();
            for (RequirementDefinition<?> requirement : requirements) {
                String name = SFormUtil.getTypeName((Class<? extends SType<?>>) requirement.getMainForm());
                Optional<SType<?>> sTypeOptional = singularFormConfig.getTypeLoader().loadType(name);
                if (sTypeOptional.isPresent()) {
                    SType<?> sType = sTypeOptional.get();
                    String label = sType.asAtr().getLabel();
                    mainForms.add(new FormDTO(name, sType.getNameSimple(), label));
                }
            }
        }
        return mainForms.stream()
                .filter(formDTO -> authorizationService.hasPermissionToForm(formDTO.getAbbreviation(), getIdUsuario()))
                .collect(Collectors.toList());
    }
}