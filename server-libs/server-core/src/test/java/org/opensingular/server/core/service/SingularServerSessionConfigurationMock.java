package org.opensingular.server.core.service;

import org.opensingular.server.commons.test.WorkspaceMetadataMockBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;

@Primary
@Named
@DependsOn(value = "workspaceMetadataMockBean")
@Scope("session")
public class SingularServerSessionConfigurationMock extends SingularServerSessionConfiguration {

    @Inject
    private WorkspaceMetadataMockBean workspaceMetadataMockBean;


    @Override
    public void init() {
        setConfigMaps(workspaceMetadataMockBean.getMap());
    }

}
