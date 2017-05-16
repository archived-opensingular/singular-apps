package org.opensingular.server.module;

import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Provider responsavel por fornecer dados e metadados para exibição das tablesList no server
 */
public interface BoxItemDataProvider {

    /**
     * Pesquisa a lista de linhas a serem exibidas pelo servidor
     *
     * @param filter        o filtro do usuario
     * @param boxInfo
     * @return lista de mapa, contando chaves do {@link org.opensingular.server.commons.service.dto.DatatableField}
     * e valores.
     */
    List<Map<String, Serializable>> search(QuickFilter filter, BoxInfo boxInfo);

    /**
     * Conta todas as linhas a serem exibidas
     *
     * @param filter        o filtro do usuario
     * @param boxInfo
     * @return total de linhas
     */
    Long count(QuickFilter filter, BoxInfo boxInfo);

    /**
     * Configura as ações por linha
     * @return a lista de ações que a linha deve conter
     */
    ActionProvider getActionProvider();

}