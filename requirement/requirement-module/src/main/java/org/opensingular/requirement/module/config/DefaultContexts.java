package org.opensingular.requirement.module.config;

import org.opensingular.requirement.module.admin.AdministrationApplication;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;
import org.opensingular.requirement.module.spring.security.config.SecurityConfigs;
import org.opensingular.requirement.module.wicket.application.RequirementWicketApplication;
import org.opensingular.requirement.module.wicket.application.WorklistWicketApplication;

public interface DefaultContexts {

    class WorklistContextWithCAS extends ServerContext {
        public static final String NAME = "WORKLIST_WITH_CAS";

        public WorklistContextWithCAS() {
            super(NAME, "/worklist/*", "singular.worklist");
        }

        @Override
        public Class<? extends WorklistWicketApplication> getWicketApplicationClass() {
            return WorklistWicketApplication.class;
        }

        @Override
        public Class<? extends AbstractSingularSpringSecurityAdapter> getSpringSecurityConfigClass() {
            return SecurityConfigs.CASAnalise.class;
        }
    }

    class RequirementContextWithCAS extends ServerContext {
        public static final String NAME = "REQUIREMENT_WITH_CAS";

        public RequirementContextWithCAS() {
            super(NAME, "/requirement/*", "singular.requirement");
        }

        @Override
        public Class<? extends RequirementWicketApplication> getWicketApplicationClass() {
            return RequirementWicketApplication.class;
        }

        @Override
        public Class<? extends AbstractSingularSpringSecurityAdapter> getSpringSecurityConfigClass() {
            return SecurityConfigs.CASPeticionamento.class;
        }

        @Override
        public boolean checkOwner() {
            return true;
        }
    }

    class WorklistContext extends ServerContext {
        public static final String NAME = "WORKLIST";

        public WorklistContext() {
            super(NAME, "/worklist/*", "singular.worklist");
        }

        @Override
        public Class<? extends WorklistWicketApplication> getWicketApplicationClass() {
            return WorklistWicketApplication.class;
        }

        @Override
        public Class<? extends AbstractSingularSpringSecurityAdapter> getSpringSecurityConfigClass() {
            return SecurityConfigs.WorklistSecurity.class;
        }
    }

    class RequirementContext extends ServerContext {
        public static final String NAME = "REQUIREMENT";

        public RequirementContext() {
            super(NAME, "/requirement/*", "singular.requirement");
        }

        @Override
        public Class<? extends RequirementWicketApplication> getWicketApplicationClass() {
            return RequirementWicketApplication.class;
        }

        @Override
        public Class<? extends AbstractSingularSpringSecurityAdapter> getSpringSecurityConfigClass() {
            return SecurityConfigs.RequirementSecurity.class;
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
        public Class<? extends AbstractSingularSpringSecurityAdapter> getSpringSecurityConfigClass() {
            return SecurityConfigs.AdministrationSecurity.class;
        }
    }
}
