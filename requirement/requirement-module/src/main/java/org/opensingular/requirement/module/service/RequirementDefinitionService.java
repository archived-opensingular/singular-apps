package org.opensingular.requirement.module.service;

import org.opensingular.requirement.module.SingularRequirement;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

/**
 * Operações comuns entre as definições de requerimento
 */
public class RequirementDefinitionService {

    /**
     * Todas as definições de requerimento registradas no modulo
     */
    @Inject
    private List<SingularRequirement> requirements;

    /**
     * Lista os requerimentos carregados
     */
    public List<SingularRequirement> getRequirements() {
        return requirements;
    }

    /**
     * Recupera o requerimento pelo id informado
     */
    public SingularRequirement getRequirementById(Long id) {
        return getRequirements()
                .stream()
                .filter(r -> Objects.equals(r.getDefinitionCod(), id))
                .findFirst()
                .orElse(null);
    }
}