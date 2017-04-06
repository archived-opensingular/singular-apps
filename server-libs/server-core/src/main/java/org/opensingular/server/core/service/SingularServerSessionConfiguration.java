package org.opensingular.server.core.service;

import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SingularServerConfiguration;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.opensingular.server.commons.RESTPaths.MENU_CONTEXT;
import static org.opensingular.server.commons.RESTPaths.USER;
import static org.opensingular.server.commons.RESTPaths.WORKSPACE_CONFIGURATION;

@Named
@Scope("session")
public class SingularServerSessionConfiguration implements Loggable {

    private Map<ProcessGroupEntity, WorkspaceConfigurationMetadata> configMaps = new HashMap<>();

    @Inject
    private PetitionService<?, ?> petitionService;

    @Inject
    private SingularServerConfiguration singularServerConfiguration;


    @PostConstruct
    public void init() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            for (ProcessGroupEntity processGroup : buscarCategorias()) {
                final String url = processGroup.getConnectionURL() + WORKSPACE_CONFIGURATION
                        + "?" + MENU_CONTEXT + "=" + getMenuContext().getName()
                        + "&" + USER + "=" + getUserDetails().getUserPermissionKey();

                configMaps.put(processGroup, restTemplate.getForObject(url, WorkspaceConfigurationMetadata.class));

            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
    }


    public void reload() {
        configMaps.clear();
        init();
    }

    public List<ProcessGroupEntity> buscarCategorias() {
        return petitionService.listAllProcessGroups();
    }

    public IServerContext getMenuContext() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest       req = sra.getRequest();
        return IServerContext.getContextFromRequest(req, singularServerConfiguration.getContexts());
    }

    @SuppressWarnings("unchecked")
    private <T extends SingularUserDetails> T getUserDetails() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SingularUserDetails) {
            return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    public LinkedHashMap<ProcessGroupEntity, List<BoxConfigurationData>> getProcessGroupBoxConfigurationMap() {
        LinkedHashMap<ProcessGroupEntity, List<BoxConfigurationData>> map        = new LinkedHashMap<>();
        List<ProcessGroupEntity>                                      categorias = buscarCategorias();
        for (ProcessGroupEntity categoria : categorias) {
            final List<BoxConfigurationData> boxConfigurationMetadataDTOs = listMenus(categoria);
            map.put(categoria, boxConfigurationMetadataDTOs);
        }
        return map;
    }

    private List<BoxConfigurationData> listMenus(ProcessGroupEntity processGroup) {
        if (configMaps.containsKey(processGroup)) {
            return configMaps.get(processGroup).getBoxesConfiguration();
        } else {
            return new ArrayList<>(0);
        }

    }

}
