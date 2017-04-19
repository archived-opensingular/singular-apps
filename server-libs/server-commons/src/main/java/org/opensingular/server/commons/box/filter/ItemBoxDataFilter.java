package org.opensingular.server.commons.box.filter;

import org.opensingular.server.commons.box.BoxItemData;
import org.opensingular.server.commons.persistence.filter.QuickFilter;

/**
 * Filtro a ser processado por cada linha das tabelas do server
 */
public interface ItemBoxDataFilter {

    /**
     * Permite a configuração e processamento dos dados de cada linha da tabela, irá ser chamado para todas as lihas
     * @param boxId o id do box
     * @param boxItemData os dados retornados pelo provider da linha
     * @param filter o filtro utilizado
     */
    void doFilter(String boxId, BoxItemData boxItemData, QuickFilter filter);

}