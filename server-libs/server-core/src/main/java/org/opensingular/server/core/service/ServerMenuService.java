package org.opensingular.server.core.service;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.view.template.MenuService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServerMenuService implements MenuService, Loggable {

    @Inject
    private SingularServerSessionConfiguration singularServerSessionConfiguration;

    private Map<IServerContext, Map<ModuleEntity, List<BoxConfigurationData>>> contextMap = new HashMap<>();
    private Map<IServerContext, Map<String, BoxConfigurationData>> mapMenu = new HashMap<>();

    private void loadEntries() {
        singularServerSessionConfiguration.reload();
        for (Map.Entry<ModuleEntity, List<BoxConfigurationData>> entry : singularServerSessionConfiguration.getModuleBoxConfigurationMap().entrySet()) {
            addMenu(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public BoxConfigurationData getDefaultSelectedMenu(ModuleEntity moduleEntity) {
        final List<BoxConfigurationData> menusPorCategoria = getMenusByCategory(moduleEntity);
        if (menusPorCategoria != null && !menusPorCategoria.isEmpty()) {
            return menusPorCategoria.get(0);
        }
        return null;
    }

    @Override
    public List<BoxConfigurationData> getMenusByCategory(ModuleEntity categoria) {
        return getMap().get(categoria);
    }

    @Override
    public BoxConfigurationData getMenuByLabel(String label) {
        return getMapMenu().get(label);
    }

    @Override
    public List<ModuleEntity> getCategories() {
        return new ArrayList<>(getModifiableMap().keySet());
    }

    @Override
    public void reset() {

    }

    private Map<String, BoxConfigurationData> getMapMenu() {
        Map<String, BoxConfigurationData> modifiableMapMenu = getModifiableMapMenu();
        for (Map.Entry<ModuleEntity, List<BoxConfigurationData>> moduleEntityListEntry : getModifiableMap().entrySet()) {
            for (BoxConfigurationData boxConfigurationMetadataDTO : moduleEntityListEntry.getValue()) {
                modifiableMapMenu.put(boxConfigurationMetadataDTO.getLabel(), boxConfigurationMetadataDTO);
            }
        }
        return modifiableMapMenu;
    }

    private void addMenu(ModuleEntity categoria, List<BoxConfigurationData> menusGroupDTO) {
        getModifiableMap().put(categoria, menusGroupDTO);
    }

    public Map<ModuleEntity, List<BoxConfigurationData>> getMap() {
        return Collections.unmodifiableMap(getModifiableMap());
    }

    private Map<ModuleEntity, List<BoxConfigurationData>> getModifiableMap() {
        IServerContext ctx = getCurrentContext();
        if (!contextMap.containsKey(ctx)) {
            contextMap.put(ctx, new HashMap<>());
            loadEntries();
        }
        return contextMap.get(ctx);
    }

    private Map<String, BoxConfigurationData> getModifiableMapMenu() {
        IServerContext ctx = getCurrentContext();
        if (!mapMenu.containsKey(ctx)) {
            mapMenu.put(ctx, new HashMap<>());
        }
        return mapMenu.get(ctx);
    }

    public IServerContext getCurrentContext() {
        return SingularSession.get().getServerContext();
    }
}
