package org.opensingular.server.module;

import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.box.factory.BoxItemActionList;
import org.opensingular.server.commons.box.filter.ItemBoxDataFilter;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Provider responsavel por fornecer dados e metadados para exibição das tabelas no server
 */
public interface ItemBoxDataProvider {

    /**
     * Pesquisa a lista de linhas a serem exibidas pelo servidor
     * @param filter o filtro do usuario
     * @return lista de mapa, contando chaves do {@link org.opensingular.server.commons.service.dto.DatatableField}
     * e valores.
     */
    List<Map<String, Serializable>> search(QuickFilter filter);

    /**
     * Conta todas as linhas a serem exibidas
     * @param filter o filtro do usuario
     * @return total de linhas
     */
    Long count(QuickFilter filter);

    /**
     * Configura as ações por linha
     * @param line a linha
     * @param filter o filtro do usuario
     * @return a lista de ações que a linha deve conter
     */
    BoxItemActionList getLineActions(ItemBoxData line, QuickFilter filter);

    /**
     * Retorna os filtros a serem executados antes de responder os dados para o servidor
     * @return a(s) implementação(oes) do filtro
     */
    default List<ItemBoxDataFilter> getFilters(){
        return Collections.emptyList();
    }

}