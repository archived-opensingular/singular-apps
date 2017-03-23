package org.opensingular.server.core.service;

import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.service.dto.BoxConfigurationMetadata;
import org.opensingular.server.commons.wicket.view.template.MenuService;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@Scope("session")
public class ServerMenuService implements MenuService, Loggable {

    @Inject
    private SingularServerSessionConfiguration singularServerSessionConfiguration;

    private Map<ProcessGroupEntity, List<BoxConfigurationMetadata>> map;
    private Map<String, BoxConfigurationMetadata>                   mapMenu;

    @PostConstruct
    public void initialize() {
        reset();
    }

    @Override
    public void reset() {
        map = new HashMap<>();
        mapMenu = null;
        singularServerSessionConfiguration.reload();
        for (Map.Entry<ProcessGroupEntity, List<BoxConfigurationMetadata>> entry : singularServerSessionConfiguration.getProcessGroupBoxConfigurationMap().entrySet()) {
            addMenu(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public BoxConfigurationMetadata getDefaultSelectedMenu(ProcessGroupEntity processGroupEntity) {
        final List<BoxConfigurationMetadata> menusPorCategoria = getMenusByCategory(processGroupEntity);
        if (menusPorCategoria != null && !menusPorCategoria.isEmpty()) {
            return menusPorCategoria.get(0);
        }
        return null;
    }

    @Override
    public List<BoxConfigurationMetadata> getMenusByCategory(ProcessGroupEntity categoria) {
        return map.get(categoria);
    }

    @Override
    public BoxConfigurationMetadata getMenuByLabel(String label) {
        return getMapMenu().get(label);
    }

    @Override
    public List<ProcessGroupEntity> getCategories() {
        return new ArrayList<>(map.keySet());
    }

    private Map<String, BoxConfigurationMetadata> getMapMenu() {
        if (mapMenu == null) {
            mapMenu = new HashMap<>();
        }

        for (Map.Entry<ProcessGroupEntity, List<BoxConfigurationMetadata>> processGroupEntityListEntry : map.entrySet()) {
            for (BoxConfigurationMetadata boxConfigurationMetadataDTO : processGroupEntityListEntry.getValue()) {
                mapMenu.put(boxConfigurationMetadataDTO.getLabel(), boxConfigurationMetadataDTO);
            }
        }

        return mapMenu;
    }

    private void addMenu(ProcessGroupEntity categoria, List<BoxConfigurationMetadata> menusGroupDTO) {
        mapMenu = null;
        map.put(categoria, menusGroupDTO);
    }

    public Map<ProcessGroupEntity, List<BoxConfigurationMetadata>> getMap() {
        return Collections.unmodifiableMap(map);
    }
}
