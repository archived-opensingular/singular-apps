package org.opensingular.server.commons.box.decorator;

import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChain;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class TaskActionPermissionItemBoxDataDecorator implements ItemBoxDataDecorator {

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionResolverService permissionResolverService;

    @Override
    public void decorate(Map<String, Serializable> line, QuickFilter filter, ItemBoxDataDecoratorChain chain) {
        authorizationService.filterActions((String) line.get("type"), (Long) line.get("codPeticao"),
                (List<BoxItemAction>) line.get("actions"), filter.getIdUsuarioLogado(),
                permissionResolverService.searchPermissions(filter.getIdUsuarioLogado()));
        chain.decorate(line, filter);

    }
}
