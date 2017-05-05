package org.opensingular.server.commons.wicket.view.template;

import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.lib.commons.lambda.ISupplier;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MenuService {

    Map<ProcessGroupEntity, List<BoxConfigurationData>> getMap();

    List<BoxConfigurationData> getMenusByCategory(ProcessGroupEntity categoria);

    BoxConfigurationData getMenuByLabel(String label);

    List<ProcessGroupEntity> getCategories();

    void reset();

    BoxConfigurationData getDefaultSelectedMenu(ProcessGroupEntity categoriaSelecionada);

    @Deprecated //vinicius.nunes
    class MenuServiceSupplier implements ISupplier<Optional<MenuService>>, Loggable {
        @Override
        public Optional<MenuService> get() {
            try {
                return Optional.ofNullable(ApplicationContextProvider.get().getBean(MenuService.class));
            } catch (NoSuchBeanDefinitionException ex) {
                getLogger().debug(null, ex);
                return Optional.empty();
            }
        }
    }

}
