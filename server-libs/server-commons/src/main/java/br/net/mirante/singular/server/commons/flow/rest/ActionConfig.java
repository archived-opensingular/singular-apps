/*
 * Copyright (c) 2016, Mirante and/or its affiliates. All rights reserved.
 * Mirante PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package br.net.mirante.singular.server.commons.flow.rest;

import br.net.mirante.singular.commons.util.Loggable;
import br.net.mirante.singular.flow.core.property.MetaDataRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.net.mirante.singular.server.commons.flow.action.DefaultActions.ACTION_RELOCATE;

/**
 * Classe responsável por guardar a configuração de ações
 * disponível para o módulo
 */
public class ActionConfig implements Loggable {

    public static final MetaDataRef<ActionConfig> KEY = new MetaDataRef<>(ActionConfig.class.getName(), ActionConfig.class);

    private static final Map<ActionDefinition, Class<? extends IController>> MAP_DEFAULT_ACTIONS;

    static {
        MAP_DEFAULT_ACTIONS = new HashMap<>();
        MAP_DEFAULT_ACTIONS.put(ACTION_RELOCATE, AtribuirController.class);
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
            getLogger().debug(String.format("Action '%s' foi removido pois não está definida para esse fluxo.", name));
        }
        return contains;
    }
}
