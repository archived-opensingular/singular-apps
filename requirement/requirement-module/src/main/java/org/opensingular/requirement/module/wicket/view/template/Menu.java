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
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.opensingular.lib.commons.lambda.ISupplier;
import org.opensingular.lib.commons.ui.Icon;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.menu.MetronicMenu;
import org.opensingular.lib.wicket.util.menu.MetronicMenuGroup;
import org.opensingular.lib.wicket.util.menu.MetronicMenuItem;
import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.requirement.module.WorkspaceConfigurationMetadata;
import org.opensingular.requirement.module.connector.ModuleService;
import org.opensingular.requirement.module.service.dto.BoxConfigurationData;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.service.dto.FlowDefinitionDTO;
import org.opensingular.requirement.module.spring.security.SingularRequirementUserDetails;
import org.opensingular.requirement.module.wicket.SingularSession;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.ITEM_PARAM_NAME;

public class Menu extends Panel implements Loggable {

    @Inject
    @SpringBean(required = false)
    private WorkspaceConfigurationMetadata workspaceConfigurationMetadata;

    @Inject
    private ModuleService moduleService;

    private Class<? extends WebPage> boxPageClass;
    private MetronicMenu menu;

    public Menu(String id, Class<? extends WebPage> boxPageClass) {
        super(id);
        this.boxPageClass = boxPageClass;
        add(buildMenu());
        buildMenuGroup();
    }

    protected MetronicMenu buildMenu() {
        this.menu = new MetronicMenu("menu");
        return this.menu;
    }


    protected void buildMenuGroup() {
        Optional.ofNullable(workspaceConfigurationMetadata)
                .map(WorkspaceConfigurationMetadata::getBoxesConfiguration)
                .map(Collection::stream)
                .orElse(Stream.empty())
                .forEach(boxConfigurationMetadata -> {
                    List<MenuItemConfig> subMenus;
                    if (boxConfigurationMetadata.getItemBoxes() == null) {
                        subMenus = buildDefaultSubMenus(boxConfigurationMetadata);
                    } else {
                        subMenus = buildSubMenus(boxConfigurationMetadata);
                    }
                    if (!subMenus.isEmpty()) {
                        buildMenus(menu, boxConfigurationMetadata, subMenus);
                    }
                });
    }

    protected List<MenuItemConfig> buildDefaultSubMenus(BoxConfigurationData boxConfigurationMetadata) {
        return Collections.emptyList();
    }

    protected void buildMenus(MetronicMenu menu, BoxConfigurationData boxConfigurationMetadata,
                              List<MenuItemConfig> subMenus) {
        MetronicMenuGroup group = new MetronicMenuGroup(DefaultIcons.LAYERS, boxConfigurationMetadata.getLabel());
        menu.addItem(group);
        final List<Pair<Component, ISupplier<String>>> itens = new ArrayList<>();

        for (MenuItemConfig t : subMenus) {
            PageParameters pageParameters = new PageParameters();
            pageParameters.add(ITEM_PARAM_NAME, t.name);

            MetronicMenuItem i = new ServerMenuItem(t.icon, t.name, t.pageClass, t.page, pageParameters);
            group.addItem(i);
            itens.add(Pair.of(i.getHelper(), t.counterSupplier));
        }
        menu.add(new AddContadoresBehaviour(itens));
        onBuildModuleGroup(group, menu);
    }

    protected void onBuildModuleGroup(MetronicMenuGroup group, MetronicMenu menu) {

    }

    protected List<MenuItemConfig> buildSubMenus(BoxConfigurationData boxConfigurationMetadata) {

        List<String> abbreviations = boxConfigurationMetadata.getProcesses().stream()
                .map(FlowDefinitionDTO::getAbbreviation)
                .collect(Collectors.toList());

        List<MenuItemConfig> configs = new ArrayList<>();

        for (ItemBox itemBoxDTO : boxConfigurationMetadata.getItemBoxes()) {
            final ISupplier<String> countSupplier = createCountSupplier(itemBoxDTO, abbreviations);
            configs.add(MenuItemConfig.of(getBoxPageClass(), itemBoxDTO.getName(), itemBoxDTO.getHelpText(), itemBoxDTO.getIcone(), countSupplier));

        }

        return configs;
    }

    protected ISupplier<String> createCountSupplier(ItemBox itemBoxDTO, List<String> abbreviations) {
        return () -> moduleService.countAll(itemBoxDTO, abbreviations, getIdCurrentUser());
    }

    protected String getIdPessoa() {
        return getIdCurrentUser();
    }

    protected String getIdCurrentUser() {
        SingularRequirementUserDetails singularUserDetails = SingularSession.get().getUserDetails();
        return Optional.ofNullable(singularUserDetails)
                .map(SingularRequirementUserDetails::getUsername)
                .orElse(null);
    }

    //TODO buggy, should be refactored
    public Class<? extends Page> getBoxPageClass() {
        Class<? extends Page> homePage = WebApplication.get().getHomePage();
        if (homePage != null) {
            return homePage;
        }
        return boxPageClass;
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
