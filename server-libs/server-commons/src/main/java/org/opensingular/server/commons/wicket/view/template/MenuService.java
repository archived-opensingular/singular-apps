package org.opensingular.server.commons.wicket.view.template;

import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;

import java.util.List;
import java.util.Map;

public interface MenuService {
    Map<ProcessGroupEntity, List<BoxConfigurationData>> getMap();

    List<BoxConfigurationData> getMenusByCategory(ProcessGroupEntity categoria);

    BoxConfigurationData getMenuByLabel(String label);

    List<ProcessGroupEntity> getCategories();

    void reset();

    BoxConfigurationData getDefaultSelectedMenu(ProcessGroupEntity categoriaSelecionada);
}
