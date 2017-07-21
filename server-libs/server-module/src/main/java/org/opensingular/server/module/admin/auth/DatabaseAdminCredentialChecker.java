package org.opensingular.server.module.admin.auth;


import org.apache.commons.lang3.StringUtils;
import org.opensingular.server.commons.auth.AdminCredentialChecker;
import org.opensingular.server.commons.persistence.entity.parameter.ParameterEntity;
import org.opensingular.server.commons.service.ParameterService;
import org.opensingular.server.module.SingularModuleConfiguration;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class DatabaseAdminCredentialChecker implements AdminCredentialChecker {

    public static final String PARAM_PASSHASHADMIN = "ADMIN_HASH_PASSWORD";
    public static final String PARAM_ADMINUSERNAME = "ADMIN_USERNAME";


    @Inject
    private ParameterService parameterService;

    @Inject
    private SingularModuleConfiguration singularModuleConfiguration;

    @Override
    public boolean check(String username, String password) {
        return isPasswordAndUsernameValid(username, password)
                && username.equalsIgnoreCase(retrieveUsername())
                && getSHA1(password).equals(retrievePasswordHash());
    }

    private boolean isPasswordAndUsernameValid(String username, String password) {
        return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
    }

    private String retrievePasswordHash() {
        return retrieveParameter(PARAM_PASSHASHADMIN);
    }

    private String retrieveUsername() {
        return retrieveParameter(PARAM_ADMINUSERNAME);
    }

    private String retrieveParameter(String parameterName) {
        if (singularModuleConfiguration.getModule().abbreviation() != null) {
            return parameterService.findByNameAndModule(parameterName, singularModuleConfiguration.getModule().abbreviation())
                    .map(ParameterEntity::getValue)
                    .orElse(null);
        }
        return null;
    }

}