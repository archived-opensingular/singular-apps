package org.opensingular.server.studio.spring;

import org.opensingular.server.single.config.SingleAppBeanFactory;
import org.opensingular.studio.core.config.StudioConfig;
import org.opensingular.studio.core.config.StudioConfigProvider;
import org.opensingular.studio.core.menu.StudioMenu;
import org.springframework.context.annotation.Bean;

public class ServerStudioRequirementBeanFactory extends SingleAppBeanFactory {
    private StudioConfig studioConfig;

    public ServerStudioRequirementBeanFactory() {
        this.studioConfig = StudioConfigProvider.get().retrieve();
    }

    @Bean
    public StudioMenu studioMenu() {
        return studioConfig.getAppMenu();
    }
}