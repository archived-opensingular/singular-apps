package org.opensingular.server.commons.wicket.view.template;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;

import java.util.List;
import java.util.Map;

public interface MenuService {

    Map<ModuleEntity, List<BoxConfigurationData>> getMap();

    List<BoxConfigurationData> getMenusByCategory(ModuleEntity categoria);

    BoxConfigurationData getMenuByLabel(String label);

    List<ModuleEntity> getCategories();

    void reset();

    BoxConfigurationData getDefaultSelectedMenu(ModuleEntity categoriaSelecionada);
}
