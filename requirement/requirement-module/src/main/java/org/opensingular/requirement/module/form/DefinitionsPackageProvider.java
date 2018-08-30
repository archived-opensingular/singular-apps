package org.opensingular.requirement.module.form;

@FunctionalInterface
public interface DefinitionsPackageProvider {
    /**
     * Fornece os types que devem ser registados
     */
    String[] get();
}