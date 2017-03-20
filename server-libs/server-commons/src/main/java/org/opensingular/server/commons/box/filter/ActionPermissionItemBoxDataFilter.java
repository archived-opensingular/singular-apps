package org.opensingular.server.commons.box.filter;

import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.spring.security.AuthorizationService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class ActionPermissionItemBoxDataFilter implements ItemBoxDataFilter {

    private final AuthorizationService authorizationService;

    @Inject
    public ActionPermissionItemBoxDataFilter(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void doFilter(ItemBoxData itemBoxData, QuickFilter filter) {
        authorizationService.filterActions((String) itemBoxData.get("type"), (Long) itemBoxData.get("codPeticao"), itemBoxData.getBoxItemActions(), filter.getIdUsuarioLogado());
    }

}