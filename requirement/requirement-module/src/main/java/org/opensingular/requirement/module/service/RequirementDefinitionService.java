package org.opensingular.requirement.module.service;

import org.opensingular.requirement.module.RequirementDefinition;


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
    private List<RequirementDefinition<?>> requirements;

    /**
     * Lista os requerimentos carregados
     */
    public List<RequirementDefinition<?>> getRequirements() {
        return requirements;
    }

    /**
     * Recupera o requerimento pelo id informado
     */
    public RequirementDefinition<?> getRequirementByKey(String key) {
        return getRequirements()
                .stream()
                .filter(r -> Objects.equals(r.getKey(), key))
                .findFirst()
                .orElse(null);
    }
}