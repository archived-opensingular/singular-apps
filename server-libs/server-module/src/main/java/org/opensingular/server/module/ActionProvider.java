package org.opensingular.server.module;

import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.box.factory.BoxItemActionList;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

/**
 * Componente responsável por criar as ações em cada linha do resultado de dados do provider
 */
public interface ActionProvider {

    /**
     * Configura as ações por linha
     *
     * @param line   a linha
     * @param filter o filtro do usuario
     * @return a lista de ações que a linha deve conter
     */
    BoxItemActionList getLineActions(BoxInfo boxInfo, BoxItemData line, QuickFilter filter);
}
