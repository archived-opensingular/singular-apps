/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.opensingular.requirement.module.persistence.filter.BoxFilter;
import org.opensingular.requirement.module.service.dto.DatatableField;

/**
 * Provider responsavel por fornecer dados e metadados para exibição das tablesList no server
 */
public interface BoxItemDataProvider {

    /**
     * Pesquisa a lista de linhas a serem exibidas pelo servidor
     *
     * @param filter        o filtro do usuario
     * @return lista de mapa, contando chaves do {@link DatatableField}
     * e valores.
     */
    List<Map<String, Serializable>> search(BoxFilter filter);

    /**
     * Conta todas as linhas a serem exibidas
     *
     * @param filter        o filtro do usuario
     * @return total de linhas
     */
    Long count(BoxFilter filter);

    /**
     * Configura as ações por linha
     * @return a lista de ações que a linha deve conter
     */
    ActionProvider getActionProvider();

}