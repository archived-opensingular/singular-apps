package org.opensingular.requirement.module.config;

import org.opensingular.requirement.module.admin.AdministrationApplication;
import org.opensingular.requirement.module.spring.security.config.SecurityConfigs;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public interface DefaultContexts {

    class WorklistContext extends ServerContext {
        public static final String NAME = "WORKLIST";

        public WorklistContext() {
            super(NAME, "/worklist/*", "singular.worklist");
        }

        @Override
        public Class<? extends AbstractSingularInitializer.AnalysisApplication> getWicketApplicationClass() {
            return AbstractSingularInitializer.AnalysisApplication.class;
        }

        @Override
        public boolean applyCasFilter() {
            return true;
        }

        @Override
        public Class<? extends WebSecurityConfigurerAdapter> getSpringSecurityConfigClass() {
            return SecurityConfigs.CASAnalise.class;
        }
    }

    class RequirementContext extends ServerContext {
        public static final String NAME = "REQUIREMENT";

        public RequirementContext() {
            super(NAME, "/requirement/*", "singular.requirement");
        }

        @Override
        public Class<? extends AbstractSingularInitializer.RequirementApplication> getWicketApplicationClass() {
            return AbstractSingularInitializer.RequirementApplication.class;
        }

        @Override
        public boolean applyCasFilter() {
            return true;
        }

        @Override
        public Class<? extends WebSecurityConfigurerAdapter> getSpringSecurityConfigClass() {
            return SecurityConfigs.CASPeticionamento.class;
        }

        @Override
        public boolean checkOwner() {
            return true;
        }
    }

    class AdministrationContext extends ServerContext {
        public static final String NAME = "ADMINISTRATION";

        public AdministrationContext() {
            super(NAME, "/administration/*", "singular.administration");
        }

        @Override
        public Class<? extends AdministrationApplication> getWicketApplicationClass() {
            return AdministrationApplication.class;
        }

        @Override
        public Class<? extends WebSecurityConfigurerAdapter> getSpringSecurityConfigClass() {
            return SecurityConfigs.AdministrationSecurity.class;
        }
    }
}
