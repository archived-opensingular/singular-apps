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

package org.opensingular.requirement.module.persistence.query;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.persistence.context.RequirementSearchContext;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SearchExtender que possibilita filtrar a busca passando uma lista de
 * classes do tipo {@link RequirementDefinition} a serem utilizados.
 */
public class RequirementDefinitionSearchExtender implements RequirementSearchExtender {
    private final List<String> requirementDefinitionKeys;

    private RequirementDefinitionSearchExtender(@Nonnull List<Class<? extends RequirementDefinition<?>>> requirements) {
        requirementDefinitionKeys = requirements
                .stream()
                .map(ApplicationContextProvider.get()::getBean)
                .map(RequirementDefinition::getKey)
                .collect(Collectors.toList());
    }

    @SafeVarargs
    public static RequirementDefinitionSearchExtender filteringByDefinitions(@Nonnull Class<? extends RequirementDefinition<?>>... requirements) {
        return filteringByDefinitions(Arrays.asList(requirements));
    }

    public static RequirementDefinitionSearchExtender filteringByDefinitions(@Nonnull List<Class<? extends RequirementDefinition<?>>> requirements) {
        return new RequirementDefinitionSearchExtender(requirements);
    }

    @Override
    public void extend(RequirementSearchContext ctx) {
        ctx.getQuery()
                .where(ctx.getAliases()
                        .requirementDefinition
                        .key
                        .in(requirementDefinitionKeys));
    }
}