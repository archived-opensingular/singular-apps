package org.opensingular.server.core.service;

import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.wicket.view.template.MenuService;
import org.springframework.context.annotation.Lazy;
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

    private Map<ProcessGroupEntity, List<BoxConfigurationData>> map;
    private Map<String, BoxConfigurationData>                   mapMenu;

    @PostConstruct
    public void initialize() {
        reset();
    }

    @Override
    public void reset() {
        map = new HashMap<>();
        mapMenu = null;
        singularServerSessionConfiguration.reload();
        for (Map.Entry<ProcessGroupEntity, List<BoxConfigurationData>> entry : singularServerSessionConfiguration.getProcessGroupBoxConfigurationMap().entrySet()) {
            addMenu(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public BoxConfigurationData getDefaultSelectedMenu(ProcessGroupEntity processGroupEntity) {
        final List<BoxConfigurationData> menusPorCategoria = getMenusByCategory(processGroupEntity);
        if (menusPorCategoria != null && !menusPorCategoria.isEmpty()) {
            return menusPorCategoria.get(0);
        }
        return null;
    }

    @Override
    public List<BoxConfigurationData> getMenusByCategory(ProcessGroupEntity categoria) {
        return map.get(categoria);
    }

    @Override
    public BoxConfigurationData getMenuByLabel(String label) {
        return getMapMenu().get(label);
    }

    @Override
    public List<ProcessGroupEntity> getCategories() {
        return new ArrayList<>(map.keySet());
    }

    private Map<String, BoxConfigurationData> getMapMenu() {
        if (mapMenu == null) {
            mapMenu = new HashMap<>();
        }

        for (Map.Entry<ProcessGroupEntity, List<BoxConfigurationData>> processGroupEntityListEntry : map.entrySet()) {
            for (BoxConfigurationData boxConfigurationMetadataDTO : processGroupEntityListEntry.getValue()) {
                mapMenu.put(boxConfigurationMetadataDTO.getLabel(), boxConfigurationMetadataDTO);
            }
        }

        return mapMenu;
    }

    private void addMenu(ProcessGroupEntity categoria, List<BoxConfigurationData> menusGroupDTO) {
        mapMenu = null;
        map.put(categoria, menusGroupDTO);
    }

    public Map<ProcessGroupEntity, List<BoxConfigurationData>> getMap() {
        return Collections.unmodifiableMap(map);
    }
}
