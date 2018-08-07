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

package org.opensingular.requirement.module.wicket.view.template;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.lib.commons.lambda.ISupplier;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.menu.MetronicMenu;
import org.opensingular.lib.wicket.util.menu.MetronicMenuGroup;
import org.opensingular.lib.wicket.util.menu.MetronicMenuItem;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.config.workspace.WorkspaceMenu;
import org.opensingular.requirement.module.config.workspace.WorkspaceMenuBoxItem;
import org.opensingular.requirement.module.config.workspace.WorkspaceMenuCategory;
import org.opensingular.requirement.module.config.workspace.WorkspaceMenuItem;
import org.opensingular.requirement.module.connector.ModuleService;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.workspace.BoxDefinition;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.CATEGORY_PARAM_NAME;
import static org.opensingular.requirement.module.wicket.view.util.ActionContext.ITEM_PARAM_NAME;

public class Menu extends Panel implements Loggable {
    @Inject
    private ModuleService moduleService;

    private IModel<IServerContext> serverContext;

    private Class<? extends WebPage> boxPageClass;

    private MetronicMenu menu;

    public Menu(String id, Class<? extends WebPage> boxPageClass, IModel<IServerContext> serverContext) {
        super(id);
        this.boxPageClass = boxPageClass;
        this.serverContext = serverContext;
        add(buildMenu());
        buildMenuGroup();
    }

    protected MetronicMenu buildMenu() {
        this.menu = new MetronicMenu("menu");
        return this.menu;
    }


    protected void buildMenuGroup() {
        Optional.ofNullable(serverContext.getObject())
                .map(IServerContext::getWorkspace)
                .map(Workspace::menu)
                .ifPresent(menu -> {
                    if (!menu.listAllBoxInfos().isEmpty()) {
                        buildMenus(menu);
                    }
                });
    }


    protected void buildMenus(WorkspaceMenu workspaceMenu) {
        final List<Pair<Component, ISupplier<String>>> itens = new ArrayList<>();
        for (WorkspaceMenuCategory category : workspaceMenu.getCategories()) {
            MetronicMenuGroup group = new MetronicMenuGroup(category.getIcon(), category.getName());
            menu.addItem(group);
            for (WorkspaceMenuItem workspaceMenuItem : category.getWorkspaceMenuItens()) {
                if(!workspaceMenuItem.isVisible()){
                    continue;
                }
                MenuItemConfig t = buildMenuItemConfig(workspaceMenuItem);
                PageParameters pageParameters = new PageParameters();
                pageParameters.add(CATEGORY_PARAM_NAME, category.getName());
                pageParameters.add(ITEM_PARAM_NAME, t.name);
                MetronicMenuItem i = new ServerMenuItem(t.icon, t.name, t.pageClass, t.page, pageParameters);
                if (t.counterSupplier != null) {
                    itens.add(Pair.of(i.getHelper(), t.counterSupplier));
                }
                group.addItem(i);
            }
        }
        menu.add(new AddContadoresBehaviour(itens));
        onBuildModuleGroup(menu);
    }

    protected void onBuildModuleGroup(MetronicMenu menu) {

    }

    protected MenuItemConfig buildMenuItemConfig(WorkspaceMenuItem workspaceMenuItem) {
        ISupplier<String> countSupplier = null;
        if (workspaceMenuItem instanceof WorkspaceMenuBoxItem ) {
            BoxDefinition boxDefinition = ((WorkspaceMenuBoxItem) workspaceMenuItem).getBoxDefinition();
            if(boxDefinition.getItemBox().isDisplayCounters()) {
                countSupplier = createCountSupplier(boxDefinition);
            }
        }
        return MenuItemConfig.of(boxPageClass, workspaceMenuItem.getName(), workspaceMenuItem.getHelpText(), workspaceMenuItem.getIcon(), countSupplier);
    }

    protected ISupplier<String> createCountSupplier(BoxDefinition boxDefinition) {
        return () -> moduleService.countAll(boxDefinition);
    }

    protected static class AddContadoresBehaviour extends AbstractDefaultAjaxBehavior {

        private final List<Pair<Component, ISupplier<String>>> itens;

        public AddContadoresBehaviour(List<Pair<Component, ISupplier<String>>> itens) {
            this.itens = itens;
        }

        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            super.renderHead(component, response);
            StringBuilder js = new StringBuilder();
            js.append(" window.Singular = window.Singular || {};");
            js.append(" window.Singular.contadores =  window.Singular.contadores || []; ");
            js.append(" (function() {");
            js.append("     var novoContador = function(){ ");
            js.append("         $(document).ready(function(){ ");
            js.append("             $(document).ready(function(){");
            js.append("                 $.getJSON('").append(getCallbackUrl()).append("', function(json) { ");
            for (int i = 0; i < itens.size(); i++) {
                final String markupId = itens.get(i).getLeft().getMarkupId();
                final String currentItem = "item" + i;
                js.append("var ").append(currentItem).append(" = ").append(" $('#").append(markupId).append("');");
                js.append(currentItem).append(".hide(); ");
                js.append(currentItem).append(".addClass('badge badge-danger'); ");
                js.append(currentItem).append(".html(json.").append(currentItem).append(");");
                js.append(currentItem).append(".fadeIn('slow'); ");
            }
            js.append("                 });");
            js.append("             });");
            js.append("         });");
            js.append("     };");
            js.append("     novoContador(); ");
            js.append("     window.Singular.contadores.push(novoContador); ");
            js.append(" }());");
            js.append(" window.Singular.atualizarContadores  = function(){$(window.Singular.contadores).each(function(){this();});}; ");
            response.render(OnDomReadyHeaderItem.forScript(js));
        }

        @Override
        protected void respond(AjaxRequestTarget target) {
            final String type = "application/json";
            final String encoding = StandardCharsets.UTF_8.name();
            final StringBuilder json = new StringBuilder();
            json.append('{');
            for (int i = 0; i < itens.size(); i++) {
                json.append("\"item").append(i).append('\"').append(':').append(itens.get(i).getRight().get());
                if (i + 1 != itens.size()) {
                    json.append(',');
                }
            }
            json.append('}');
            RequestCycle.get().scheduleRequestHandlerAfterCurrent(new TextRequestHandler(type, encoding, json.toString()));
        }
    }

    protected static class MenuItemConfig {
        public IRequestablePage page;
        public String name;
        public String helpText;
        public Class<? extends IRequestablePage> pageClass;
        public Icon icon;
        public ISupplier<String> counterSupplier;

        public static MenuItemConfig of(Class<? extends IRequestablePage> pageClass, String name, String helpText, Icon icon, ISupplier<String> counterSupplier) {
            MenuItemConfig mic = new MenuItemConfig();
            mic.pageClass = pageClass;
            mic.name = name;
            mic.helpText = helpText;
            mic.icon = icon;
            mic.counterSupplier = counterSupplier;
            return mic;
        }

        static MenuItemConfig of(IRequestablePage page, String name, String helpText, Icon icon, ISupplier<String> counterSupplier) {
            MenuItemConfig mic = new MenuItemConfig();
            mic.page = page;
            mic.name = name;
            mic.helpText = helpText;
            mic.icon = icon;
            mic.counterSupplier = counterSupplier;
            return mic;
        }
    }

}
