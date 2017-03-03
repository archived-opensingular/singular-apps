package org.opensingular.server.module.workspace;

import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.service.dto.ItemBox;

public interface SingularItemBox {

    boolean appliesTo(IServerContext context);

    ItemBox build(IServerContext context);
}
