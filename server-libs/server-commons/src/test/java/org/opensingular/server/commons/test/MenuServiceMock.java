package org.opensingular.server.commons.test;

import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.server.commons.WorkspaceConfigurationMetadata;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.wicket.view.template.MenuService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MenuServiceMock implements MenuService {

    @Inject
    WorkspaceMetadataMockBean workspaceMetadataMockBean;

    @Override
    public Map<ProcessGroupEntity, List<BoxConfigurationData>> getMap() {
        Map<ProcessGroupEntity, List<BoxConfigurationData>> map = new LinkedHashMap<>();
        for (Map.Entry<ProcessGroupEntity, WorkspaceConfigurationMetadata> config : workspaceMetadataMockBean.getMap().entrySet()) {
            map.put(config.getKey(), config.getValue().getBoxesConfiguration());
        }
        return map;
    }

    @Override
    public List<BoxConfigurationData> getMenusByCategory(ProcessGroupEntity categoria) {
        return workspaceMetadataMockBean.gimmeSomeMock().getBoxesConfiguration();
    }

    @Override
    public BoxConfigurationData getMenuByLabel(String label) {
        return null;
    }

    @Override
    public List<ProcessGroupEntity> getCategories() {
        List<ProcessGroupEntity> categories         = new ArrayList<>();
        ProcessGroupEntity       processGroupEntity = new ProcessGroupEntity();
        processGroupEntity.setConnectionURL("/nada/nada");
        processGroupEntity.setCod("CATEGORIAMOCK");
        processGroupEntity.setName("Categoria Mock");
        categories.add(processGroupEntity);
        return categories;
    }

    @Override
    public void reset() {

    }

    @Override
    public BoxConfigurationData getDefaultSelectedMenu(ProcessGroupEntity categoriaSelecionada) {
        return null;
    }
}
