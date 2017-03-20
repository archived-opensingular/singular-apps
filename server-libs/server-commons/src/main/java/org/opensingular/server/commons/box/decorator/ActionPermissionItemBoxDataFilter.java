package org.opensingular.server.commons.box.decorator;

import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.spring.security.AuthorizationService;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
public class ActionPermissionItemBoxDataFilter implements ItemBoxDataFilter {

    private final AuthorizationService authorizationService;

    @Inject
    public ActionPermissionItemBoxDataFilter(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void doFilter(Map<String, Serializable> line, QuickFilter filter) {
        authorizationService.filterActions((String) line.get("type"), (Long) line.get("codPeticao"), (List<BoxItemAction>) line.get("actions"), filter.getIdUsuarioLogado());
    }

}