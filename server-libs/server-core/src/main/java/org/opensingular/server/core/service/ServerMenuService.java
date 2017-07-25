package org.opensingular.server.core.service;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.wicket.view.template.MenuService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServerMenuService implements MenuService, Loggable {

    @Inject
    private SingularServerSessionConfiguration singularServerSessionConfiguration;

    private Map<ModuleEntity, List<BoxConfigurationData>> map;
    private Map<String, BoxConfigurationData>             mapMenu;

    @PostConstruct
    public void initialize() {
        reset();
    }

    @Override
    public void reset() {
        map = new HashMap<>();
        mapMenu = null;
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
        return map.get(categoria);
    }

    @Override
    public BoxConfigurationData getMenuByLabel(String label) {
        return getMapMenu().get(label);
    }

    @Override
    public List<ModuleEntity> getCategories() {
        return new ArrayList<>(map.keySet());
    }

    private Map<String, BoxConfigurationData> getMapMenu() {
        if (mapMenu == null) {
            mapMenu = new HashMap<>();
        }

        for (Map.Entry<ModuleEntity, List<BoxConfigurationData>> moduleEntityListEntry : map.entrySet()) {
            for (BoxConfigurationData boxConfigurationMetadataDTO : moduleEntityListEntry.getValue()) {
                mapMenu.put(boxConfigurationMetadataDTO.getLabel(), boxConfigurationMetadataDTO);
            }
        }

        return mapMenu;
    }

    private void addMenu(ModuleEntity categoria, List<BoxConfigurationData> menusGroupDTO) {
        mapMenu = null;
        map.put(categoria, menusGroupDTO);
    }

    public Map<ModuleEntity, List<BoxConfigurationData>> getMap() {
        return Collections.unmodifiableMap(map);
    }
}
