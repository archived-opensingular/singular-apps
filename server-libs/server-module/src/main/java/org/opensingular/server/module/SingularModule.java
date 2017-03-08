package org.opensingular.server.module;

/**
 * Main definition of a requirement Module.
 * Every singular server module must define exactly on {@link SingularModule}.
 */
public interface SingularModule {

    /**
     * @return
     * Textual key representing this module.
     * Should be a valid java identifier
     */
    String category();

    /**
     *
     * @return
     *  Textual descriptive name of this module.
     */
    String name();

    /**
     *
     * @param config
     *  All requirements managed by this module and its corresponding
     *  configuration must be declared in the {@link RequirementConfiguration} object
     */
    void requirements(RequirementConfiguration config);


    /**
     *
     * @param config
     *  All workspace definitions of the module like boxes, menus, listing, filters, custom actions and controllers
     *  must be declared in the {@link WorkspaceConfiguration} object properly
     */
    void workspace(WorkspaceConfiguration config);


}
