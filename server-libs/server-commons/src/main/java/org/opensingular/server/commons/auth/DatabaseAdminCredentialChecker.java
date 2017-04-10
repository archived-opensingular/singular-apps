package org.opensingular.server.commons.auth;


import org.apache.commons.lang3.StringUtils;
import org.opensingular.server.commons.persistence.entity.parameter.ParameterEntity;
import org.opensingular.server.commons.service.ParameterService;

import javax.inject.Inject;

public class DatabaseAdminCredentialChecker implements AdminCredentialChecker {

    public static final String PARAM_PASSHASHADMIN = "ADMIN_HASH_PASSWORD";
    public static final String PARAM_ADMINUSERNAME = "ADMIN_USERNAME";


    @Inject
    private ParameterService parameterService;

    private String           codProcessGroup;


    public DatabaseAdminCredentialChecker(String codProcessGroup) {
        this.codProcessGroup = codProcessGroup;
    }

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
        if (codProcessGroup != null) {
            return parameterService.findByNameAndProcessGroup(parameterName, codProcessGroup)
                    .map(ParameterEntity::getValue)
                    .orElse(null);
        }
        return null;
    }

}