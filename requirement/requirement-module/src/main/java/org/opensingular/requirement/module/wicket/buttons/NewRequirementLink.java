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

package org.opensingular.requirement.module.wicket.buttons;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.opensingular.lib.commons.lambda.ISupplier;
import org.opensingular.lib.wicket.util.metronic.menu.DropdownMenu;
import org.opensingular.requirement.module.SingularModuleConfiguration;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.SingularRequirement;
import org.opensingular.requirement.module.service.RequirementDefinitionService;
import org.opensingular.requirement.module.wicket.NewRequirementUrlBuilder;

import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class NewRequirementLink extends Panel {

    private final String url;
    private final Map<String, String> params;
    private IModel<LinkedHashSet<Class<? extends RequirementDefinition>>> requirements;
    private IModel<String> labelModel = new StringResourceModel("label.button.insert", this, null);

    @Inject
    private RequirementDefinitionService requirementDefinitionService;

    public NewRequirementLink(String id, String url, Map<String, String> params,
                              IModel<LinkedHashSet<Class<? extends RequirementDefinition>>> requirements) {
        this(id, null, url, params, requirements);
    }

    public NewRequirementLink(String id, IModel<String> labelModel, String url,
                              Map<String, String> params, IModel<LinkedHashSet<Class<? extends RequirementDefinition>>> requirements) {
        super(id);
        this.url = url;
        this.labelModel = labelModel == null ? this.labelModel : labelModel;
        this.params = params;
        this.requirements = requirements;
        buildButtons();
    }

    protected void buildButtons() {
        addSingleButton(() -> Optional.ofNullable(requirements.getObject()).map(Set::size).orElse(0) == 1);
        addDropdownButton(() -> Optional.ofNullable(requirements.getObject()).map(Set::size).orElse(0) > 1);
    }

    protected void addSingleButton(ISupplier<Boolean> visibleSupplier) {
        Optional<RequirementDefinition> findFirst = getRequirementsStream().findFirst();
        if (findFirst.isPresent()) {
            Link<Void> newButton = buildLink("_botao", labelModel, findFirst.get());
            newButton.add($b.visibleIf(visibleSupplier));
            this.add(newButton);
        }
    }

    private Link<Void> buildLink(String id, IModel<String> labelModel, RequirementDefinition requirement) {
        String url = NewRequirementLink.this.buildURL(requirement);
        return new Link<Void>(id) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.add($b.attr("href", url));
                this.add($b.attr("target", "_blank"));
                this.setBody(labelModel);
            }

            @Override
            public void onClick() {
                //DO NOTHING
            }
        };
    }

    protected void addDropdownButton(ISupplier<Boolean> visibleSupplier) {
        DropdownMenu dropdownMenu = new DropdownMenu("_novos");
        dropdownMenu.add($b.visibleIf(visibleSupplier));
        dropdownMenu.add($b.onConfigure(c -> {
            if (visibleSupplier.get()) {
                for (RequirementDefinition r : getRequirementsStream().collect(Collectors.toList())) {
                    String name = r.getName();
                    dropdownMenu.adicionarMenu(id -> buildLink(id, $m.ofValue(name), r));
                }
            }
        }));
        this.add(dropdownMenu);
    }

    private Stream<RequirementDefinition> getRequirementsStream() {
        return requirementDefinitionService.getRequirements().stream()
                .filter(req -> requirements.getObject().stream().anyMatch(reqClass -> reqClass.isAssignableFrom(req.getClass())));
    }

    protected String buildURL(RequirementDefinition requirement) {
        return new NewRequirementUrlBuilder(url, requirement.getKey()).getURL(params);
    }

    @Override
    protected boolean getStatelessHint() {
        return true;
    }
}