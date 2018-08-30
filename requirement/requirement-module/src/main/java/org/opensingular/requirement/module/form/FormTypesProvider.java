package org.opensingular.requirement.module.form;

import org.opensingular.form.SType;

import java.util.List;

@FunctionalInterface
public interface FormTypesProvider  {
    /**
     * Fornece os types que devem ser registados
     */
    List<Class<? extends SType<?>>> get();
}
