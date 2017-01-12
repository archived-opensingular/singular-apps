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

package org.opensingular.server.commons.flow.rest;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.flow.core.property.MetaDataRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.opensingular.server.commons.flow.action.DefaultActions.ACTION_ASSIGN;

/**
 * Classe responsável por guardar a configuração de ações
 * disponível para o módulo
 */
public class ActionConfig implements Loggable {

    public static final MetaDataRef<ActionConfig> KEY = new MetaDataRef<>(ActionConfig.class.getName(), ActionConfig.class);

    private static final Map<ActionDefinition, Class<? extends IController>> MAP_DEFAULT_ACTIONS;

    static {
        MAP_DEFAULT_ACTIONS = new HashMap<>();
        MAP_DEFAULT_ACTIONS.put(ACTION_ASSIGN, AtribuirController.class);
    }

    private List<ActionDefinition> defaultActions;
    private Map<ActionDefinition, Class<? extends IController>> customActions;

    public ActionConfig() {
        defaultActions = new ArrayList<>();
        customActions = new HashMap<>();
    }

    public Map<ActionDefinition, Class<? extends IController>> getCustomActions() {
        return Collections.unmodifiableMap(customActions);
    }

    public ActionConfig addAction(ActionDefinition definition, Class<? extends IController> controllerClass) {
        customActions.put(definition, controllerClass);
        return this;
    }

    public List<ActionDefinition> getDefaultActions() {
        return Collections.unmodifiableList(defaultActions);
    }

    public ActionConfig addDefaultAction(ActionDefinition action) {
        defaultActions.add(action);
        return this;
    }

    public Class<? extends IController> getAction(String name) {
        Class<? extends IController> controllerClass = getCustomAction(name);

        if (controllerClass == null) {
            controllerClass = MAP_DEFAULT_ACTIONS
                    .entrySet()
                    .stream()
                    .filter(e -> e.getKey().getName().equals(name))
                    .map(entry -> entry.getValue())
                    .findFirst()
                    .orElse(null);
        }

        return controllerClass;
    }

    private Class<? extends IController> getCustomAction(String name) {
        for (Map.Entry<ActionDefinition, Class<? extends IController>> entry : customActions.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public boolean containsAction(String name) {
        boolean contains = customActions.keySet().stream().anyMatch(actionDefinition -> actionDefinition.getName().equals(name)) || defaultActions.stream().anyMatch(actionDefinition -> actionDefinition.getName().equals(name));
        if (!contains) {
            getLogger().debug("Action '{}' foi removido pois não está definida para esse fluxo.", name);
        }
        return contains;
    }
}
