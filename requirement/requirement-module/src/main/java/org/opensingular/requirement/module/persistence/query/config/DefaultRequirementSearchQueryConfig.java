package org.opensingular.requirement.module.persistence.query.config;

public class DefaultRequirementSearchQueryConfig implements RequirementSearchQueryConfig {

    String STRING_REPLACEMENT_TEMPLATE = "{0}";

    /**
     * Permite definir uma expressão, nos mesmos moldes da classe StringTemplate para que as consultas
     * apliquem este template nas comparações de strings, tanto na palavra digitada quando na palavra buscada.
     *
     * Por padrão, não realiza nenhuma transformação
     *
     * @see com.querydsl.core.types.dsl.StringTemplate
     */
    public String stringReplacementTemplate() {
        return STRING_REPLACEMENT_TEMPLATE;
    }

}
