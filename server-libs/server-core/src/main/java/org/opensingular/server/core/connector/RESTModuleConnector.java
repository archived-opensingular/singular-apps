package org.opensingular.server.core.connector;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.inject.Named;

import static org.opensingular.server.commons.RESTPaths.MENU_CONTEXT;
import static org.opensingular.server.commons.RESTPaths.USER;
import static org.opensingular.server.commons.RESTPaths.WORKSPACE_CONFIGURATION;

@Named
public class RESTModuleConnector implements ModuleConnector {

    private <T extends SingularUserDetails> T getUserDetails() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SingularUserDetails) {
            return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    @Override
    public WorkspaceConfigurationMetadata loadWorkspaceConfiguration(ModuleEntity module, IServerContext serverContext) {
        RestTemplate restTemplate = new RestTemplate();
        String url = module.getConnectionURL() + WORKSPACE_CONFIGURATION + "?" + MENU_CONTEXT + "=" + serverContext.getName();
        SingularUserDetails userDetails = getUserDetails();
        if (userDetails != null) {
            url += "&" + USER + "=" + userDetails.getUserPermissionKey();
        }
        return restTemplate.getForObject(url, WorkspaceConfigurationMetadata.class);
    }

}