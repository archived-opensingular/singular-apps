package org.opensingular.server.commons;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.inject.Named;
import java.util.List;

import static org.opensingular.server.commons.RESTPaths.*;

@Named
public class RESTModuleConnector implements ModuleConnector, Loggable {

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

    @Override
    public String count(ItemBox itemBoxDTO, List<String> siglas, String idUsuarioLogado, ModuleEntity module) {
        final String connectionURL = module.getConnectionURL();
        final String url = connectionURL + itemBoxDTO.getCountEndpoint();
        long qtd;
        try {
            QuickFilter filter = new QuickFilter()
                    .withProcessesAbbreviation(siglas)
                    .withRascunho(itemBoxDTO.isShowDraft())
                    .withEndedTasks(itemBoxDTO.getEndedTasks())
                    .withIdUsuarioLogado(idUsuarioLogado);
            qtd = new RestTemplate().postForObject(url, filter, Long.class);
        } catch (Exception e) {
            getLogger().error("Erro ao acessar serviço: " + url, e);
            qtd = 0;
        }
        return String.valueOf(qtd);
    }
}