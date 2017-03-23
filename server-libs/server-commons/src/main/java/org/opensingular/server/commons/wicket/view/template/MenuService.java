package org.opensingular.server.commons.wicket.view.template;

import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.server.commons.service.dto.BoxConfigurationMetadata;

import java.util.List;
import java.util.Map;

public interface MenuService {
    Map<ProcessGroupEntity, List<BoxConfigurationMetadata>> getMap();

    List<BoxConfigurationMetadata> getMenusByCategory(ProcessGroupEntity categoria);

    BoxConfigurationMetadata getMenuByLabel(String label);

    List<ProcessGroupEntity> getCategories();

    void reset();

    BoxConfigurationMetadata getDefaultSelectedMenu(ProcessGroupEntity categoriaSelecionada);
}
