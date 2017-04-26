package org.opensingular.server.commons.test;

import org.opensingular.lib.support.spring.util.AutoScanDisabled;
import org.opensingular.server.commons.admin.healthsystem.validation.database.IValidatorDatabase;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.wicket.view.template.MenuService;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.spy;

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
public class CommonsConfigurationMock {


    @Bean
    public MenuService menuService() {
        return new MenuServiceMock();
    }

    @Primary
    @Bean
    public AuthorizationService authorizationService() {
        return spy(AuthorizationService.class);
    }

    @Primary
    @Bean
    public IValidatorDatabase validatorDatabase() {
        return spy(ValidatorOracleMock.class);
    }
}
