package org.opensingular.server.module.box.filter;

import org.opensingular.flow.core.Flow;
import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.box.factory.ItemBoxActionList;
import org.opensingular.server.commons.box.filter.ItemBoxDataFilter;
import org.opensingular.server.commons.flow.actions.ActionConfig;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.SingularModuleConfiguration;
import org.opensingular.server.module.workspace.ItemBoxFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class ActionAppenderItemBoxDataFilter implements ItemBoxDataFilter, Loggable {

    @Inject
    private SingularModuleConfiguration singularModuleConfiguration;

    @Override
    public void doFilter(String boxId, ItemBoxData itemBoxData, QuickFilter filter) {
        singularModuleConfiguration.getItemBoxFactory(boxId)
                .map(ItemBoxFactory::getDataProvider)
                .map(provider -> getBoxActions(itemBoxData, provider, filter))
                .ifPresent(itemBoxData::setBoxItemActions);
    }

    private List<BoxItemAction> getBoxActions(ItemBoxData itemBoxData, ItemBoxDataProvider dataProvider, QuickFilter filter) {
        return findActionConfigAndFilter(itemBoxData, dataProvider.getLineActions(itemBoxData, filter));
    }

    private List<BoxItemAction> findActionConfigAndFilter(ItemBoxData itemBoxData, ItemBoxActionList itemBoxActionList) {
        String processKey = (String) itemBoxData.getProcessType();

        if (processKey != null) {
            ProcessDefinition<?> processDefinition = Flow.getProcessDefinition(processKey);
            ActionConfig actionConfig = processDefinition.getMetaDataValue(ActionConfig.KEY);
            if (actionConfig != null) {
                return filterByActionConfig(itemBoxActionList, actionConfig);
            }
        }

        return itemBoxActionList.getBoxItemActions();
    }

    private List<BoxItemAction> filterByActionConfig(ItemBoxActionList itemBoxActionList, ActionConfig actionConfig) {
        return itemBoxActionList.getBoxItemActions().stream()
                .filter(itemAction -> actionConfig.containsAction(itemAction.getName()))
                .collect(Collectors.toList());
    }

}