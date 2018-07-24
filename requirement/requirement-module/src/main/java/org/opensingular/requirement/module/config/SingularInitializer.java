package org.opensingular.requirement.module.config;

import org.opensingular.requirement.module.WorkspaceInitializer;

public interface SingularInitializer {
    FlowInitializer flowConfiguration();

    SchedulerInitializer schedulerConfiguration();

    WebInitializer webConfiguration();

    Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfig();

    FormInitializer formConfiguration();

    SpringSecurityInitializer springSecurityConfiguration();

    SpringHibernateInitializer springHibernateConfiguration();

    WorkspaceInitializer workspaceConfiguration();
}