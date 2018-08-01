package org.opensingular.requirement.module.persistence.filter;

import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.requirement.module.form.FormTypesProvider;
import org.opensingular.requirement.module.service.dto.FormDTO;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.spring.security.AuthorizationService;
import org.opensingular.requirement.module.spring.security.SingularRequirementUserDetails;
import org.opensingular.requirement.module.wicket.SingularSession;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.springframework.beans.factory.BeanFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Cria o BoxFilter populando com os dados basicos
 */
public class BoxFilterFactory {
    @Inject
    private BeanFactory beanFactory;

    @Inject
    @Named("formConfigWithDatabase")
    private SFormConfig<String> singularFormConfig;

    @Inject
    private FormTypesProvider formTypesProvider;

    @Inject
    private AuthorizationService authorizationService;

    public BoxFilter create(ItemBox itemBox) {
        BoxDefinition boxDefinition = beanFactory.getBean(itemBox.getBoxDefinitionClass());
        return boxDefinition.createBoxFilter()
                .withIdUsuarioLogado(getIdUsuario())
                .withIdPessoa(getIdPessoa())
                .withProcessesAbbreviation(getProcessesNames())
                .withTypesNames(getFormNames());
    }

    private List<String> getFormNames() {
        return getForms().stream().map(FormDTO::getName).collect(Collectors.toList());
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

    private List<FormDTO> getForms() {
        Stream.Builder<FormDTO> formDTOs = Stream.builder();
        for (Class<? extends SType<?>> formClass : formTypesProvider.get()) {
            String name = SFormUtil.getTypeName(formClass);
            Optional<SType<?>> sTypeOptional = singularFormConfig.getTypeLoader().loadType(name);
            if (sTypeOptional.isPresent()) {
                SType<?> sType = sTypeOptional.get();
                String label = sType.asAtr().getLabel();
                formDTOs.accept(new FormDTO(name, sType.getNameSimple(), label));
            }
        }

        return formDTOs.build()
                .filter(formDTO -> authorizationService.hasPermissionToForm(formDTO.getAbbreviation(), getIdUsuario()))
                .collect(Collectors.toList());
    }
}