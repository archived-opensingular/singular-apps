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

package org.opensingular.server.commons.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.persistence.filter.FilterToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuickFilterBuilder {
    private List<FilterToken> tokens = new ArrayList<>();
    private Map<String, List<IFunction<String, BooleanExpression>>> fieldFilters = new HashMap<>();

    public void addTokens(List<FilterToken> tokens) {
        this.tokens.addAll(tokens);
    }

    public void registerFieldFilter(String alias, IFunction<String, BooleanExpression> iFunction) {
        fieldFilters
                .computeIfAbsent(alias, k -> new ArrayList<>())
                .add(iFunction);
    }

    public BooleanBuilder build(Set<String> enabledFieldFilters) {
        BooleanBuilder filterBooleanBuilder = new BooleanBuilder();
        for (FilterToken token : tokens) {
            validateEnabledFilters(enabledFieldFilters);
            BooleanBuilder tokenBooleanBuilder = new BooleanBuilder();
            for (String filter : token.getAllPossibleMatches()) {
                for (Map.Entry<String, List<IFunction<String, BooleanExpression>>> entry : fieldFilters.entrySet()) {
                    if (enabledFieldFilters.contains(entry.getKey())) {
                        List<IFunction<String, BooleanExpression>> value = entry.getValue();
                        for (IFunction<String, BooleanExpression> function : value) {
                            tokenBooleanBuilder.or(function.apply(filter));
                        }
                    }
                }
            }
            filterBooleanBuilder.and(tokenBooleanBuilder);
        }

        return filterBooleanBuilder;
    }

    private void validateEnabledFilters(Set<String> quickFilterNames) {
        Set<String> names = new HashSet<>(quickFilterNames);
        names.removeAll(fieldFilters.keySet());

        if (!names.isEmpty()) {
            throw new SingularServerException(String.format("Os seguintes filtros habilitados não foram encontrados: %s", names));
        }
    }
}
