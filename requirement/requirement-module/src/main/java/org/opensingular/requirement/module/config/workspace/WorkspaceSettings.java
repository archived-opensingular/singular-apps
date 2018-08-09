package org.opensingular.requirement.module.config.workspace;

import org.apache.wicket.Application;
import org.opensingular.requirement.module.spring.security.AbstractSingularSpringSecurityAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceSettings implements Serializable {
    private String contextPath;
    private String propertiesBaseKey;
    private Class<? extends Application> wicketApplicationClass;
    private Class<? extends AbstractSingularSpringSecurityAdapter> springSecurityConfigClass;
    private boolean checkOwner;
    private List<String> publicUrls = new ArrayList<>();
    private boolean hideFromStudioMenu = false;

    public WorkspaceSettings addPublicUrl(String publicUrl) {
        this.publicUrls.add(publicUrl);
        return this;
    }

    public WorkspaceSettings contextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    public WorkspaceSettings propertiesBaseKey(String propertiesBaseKey) {
        this.propertiesBaseKey = propertiesBaseKey;
        return this;
    }

    public WorkspaceSettings wicketApplicationClass(Class<? extends Application> wicketApplicationClass) {
        this.wicketApplicationClass = wicketApplicationClass;
        return this;
    }

    public WorkspaceSettings springSecurityConfigClass(Class<? extends AbstractSingularSpringSecurityAdapter> springSecurityConfigClass) {
        this.springSecurityConfigClass = springSecurityConfigClass;
        return this;
    }

    public WorkspaceSettings checkOwner(boolean checkOwner) {
        this.checkOwner = checkOwner;
        return this;
    }

    public WorkspaceSettings hideFromStudioMenu(boolean hideFromStudioMenu) {
        this.hideFromStudioMenu = hideFromStudioMenu;
        return this;
    }

    /**
     * Conversao do formato aceito por servlets e filtros (contextPath) para java regex
     */
    public String getPathRegex() {
        return getContextPath().replaceAll("\\*", ".*");
    }

    /**
     * Conversao do formato aceito por servlets e filtros (contextPath) para um formato de url
     * sem a / ao final.
     */
    public String getUrlPath() {
        String path = getContextPath().replace("*", "").replace(".", "").trim();
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getPropertiesBaseKey() {
        return propertiesBaseKey;
    }

    public Class<? extends Application> getWicketApplicationClass() {
        return wicketApplicationClass;
    }

    public Class<? extends AbstractSingularSpringSecurityAdapter> getSpringSecurityConfigClass() {
        return springSecurityConfigClass;
    }

    public boolean isCheckOwner() {
        return checkOwner;
    }

    public List<String> getPublicUrls() {
        return publicUrls;
    }

    public boolean isHideFromStudioMenu() {
        return hideFromStudioMenu;
    }
}