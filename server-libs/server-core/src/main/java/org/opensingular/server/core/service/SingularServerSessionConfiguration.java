package org.opensingular.server.core.service;

import org.fest.util.VisibleForTesting;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SingularServerConfiguration;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.opensingular.server.commons.RESTModuleConnector;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Named
@Scope("session")
public class SingularServerSessionConfiguration implements Loggable {

    private Map<ModuleEntity, WorkspaceConfigurationMetadata> configMaps = new HashMap<>();

    @Inject
    private PetitionService<?, ?> petitionService;

    @Inject
    private SingularServerConfiguration singularServerConfiguration;

    @Inject
    private RESTModuleConnector restModuleConnector;

    @PostConstruct
    public void init() {
        try {
            IServerContext menuContext = getMenuContext();
            for (ModuleEntity module : buscarCategorias()) {
                configMaps.put(module, restModuleConnector.loadWorkspaceConfiguration(module, menuContext));
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
        }
    }

    public void reload() {
        configMaps.clear();
        init();
    }

    public List<ModuleEntity> buscarCategorias() {
        return petitionService.listAllModules();
    }

    public IServerContext getMenuContext() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        return IServerContext.getContextFromRequest(req, singularServerConfiguration.getContexts());
    }

    @SuppressWarnings("unchecked")
    private <T extends SingularUserDetails> T getUserDetails() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SingularUserDetails) {
            return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }

    public LinkedHashMap<ModuleEntity, List<BoxConfigurationData>> getModuleBoxConfigurationMap() {
        LinkedHashMap<ModuleEntity, List<BoxConfigurationData>> map = new LinkedHashMap<>();
        List<ModuleEntity> categorias = buscarCategorias();
        for (ModuleEntity categoria : categorias) {
            final List<BoxConfigurationData> boxConfigurationMetadataDTOs = listMenus(categoria);
            map.put(categoria, boxConfigurationMetadataDTOs);
        }
        return map;
    }

    private List<BoxConfigurationData> listMenus(ModuleEntity module) {
        if (configMaps.containsKey(module)) {
            return configMaps.get(module).getBoxesConfiguration();
        } else {
            return new ArrayList<>(0);
        }
    }

    /**
     * For testing purposes only. Must not be used in another scenario.
     *
     * @param configMaps
     */
    @VisibleForTesting
    void setConfigMaps(Map<ModuleEntity, WorkspaceConfigurationMetadata> configMaps) {
        this.configMaps = configMaps;
    }
}
