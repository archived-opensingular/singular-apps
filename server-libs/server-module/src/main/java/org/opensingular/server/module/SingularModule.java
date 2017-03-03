package org.opensingular.server.module;

public interface SingularModule {

    String category();

    String name();

    void requirements(RequirementConfiguration config);


    void workspace(WorkspaceConfiguration config);


}
