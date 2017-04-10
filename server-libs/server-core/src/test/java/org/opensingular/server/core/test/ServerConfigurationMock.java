package org.opensingular.server.core.test;

import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.server.commons.test.CommonsConfigurationMock;
import org.opensingular.server.commons.wicket.view.template.MenuService;
import org.opensingular.server.core.service.ServerMenuService;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableTransactionManagement(proxyTargetClass = true)
@EnableCaching
@EnableWebMvc
@EnableWebSecurity
@Configuration
@ComponentScan(
        basePackages = {"org.opensingular"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION,
                        value = AutoScanDisabled.class)
        })
public class ServerConfigurationMock extends CommonsConfigurationMock {


    @Primary
    @Bean
    @Scope("session")
    public MenuService menuService() {
        return new ServerMenuService();
    }

}
