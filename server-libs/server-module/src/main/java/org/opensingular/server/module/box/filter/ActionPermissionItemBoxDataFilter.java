package org.opensingular.server.module.box.filter;

import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.box.filter.ItemBoxDataFilter;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.spring.security.AuthorizationService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ActionPermissionItemBoxDataFilter implements ItemBoxDataFilter {

    private final AuthorizationService authorizationService;

    @Inject
    public ActionPermissionItemBoxDataFilter(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void doFilter(String boxId, ItemBoxData itemBoxData, QuickFilter filter) {
        authorizationService.filterActions((String) itemBoxData.getType(), (Long) itemBoxData.getCodPeticao(), itemBoxData.getBoxItemActions(), filter.getIdUsuarioLogado());
    }

}