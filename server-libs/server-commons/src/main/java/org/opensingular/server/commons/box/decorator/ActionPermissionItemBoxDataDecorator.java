package org.opensingular.server.commons.box.decorator;

import org.opensingular.server.commons.box.chain.ItemBoxDataDecoratorChain;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.spring.security.AuthorizationService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ActionPermissionItemBoxDataDecorator implements ItemBoxDataDecorator {

    private final AuthorizationService authorizationService;

    @Inject
    public ActionPermissionItemBoxDataDecorator(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void decorate(Map<String, Serializable> line, QuickFilter filter, ItemBoxDataDecoratorChain chain) {
        authorizationService.filterActions((String) line.get("type"), (Long) line.get("codPeticao"), (List<BoxItemAction>) line.get("actions"), filter.getIdUsuarioLogado());
        chain.decorate(line, filter);

    }
}
