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

package org.opensingular.requirement.module.wicket.box;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.workspace.Workspace;
import org.opensingular.requirement.module.config.workspace.WorkspaceMenuCategory;
import org.opensingular.requirement.module.config.workspace.WorkspaceMenuItem;
import org.opensingular.requirement.module.spring.security.SingularRequirementUserDetails;
import org.opensingular.requirement.module.spring.security.UserDetailsProvider;
import org.opensingular.requirement.module.wicket.error.Page403;
import org.opensingular.requirement.module.wicket.template.ServerBoxTemplate;
import org.opensingular.requirement.module.wicket.view.template.Menu;
import org.wicketstuff.annotation.mount.MountPath;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.CATEGORY_PARAM_NAME;
import static org.opensingular.requirement.module.wicket.view.util.ActionContext.ITEM_PARAM_NAME;

@MountPath("/box")
public class BoxPage extends ServerBoxTemplate implements Loggable {

    @Inject
    private IServerContext serverContext;

    @Inject
    private UserDetailsProvider userDetailsProvider;

    private IModel<WorkspaceMenuItem> workspaceMenuItem;

    public BoxPage(PageParameters parameters) {
        super(parameters);
        addBox();
    }

    public void addBox() {
        String category = getPageParameters().get(CATEGORY_PARAM_NAME).toOptionalString();
        String item = getPageParameters().get(ITEM_PARAM_NAME).toOptionalString();

        if (serverContext == null) {
            getLogger().error("Não foi possivel determinal o contexto atual");
            throw new RestartResponseException(Page403.class);
        }

        Workspace workspace = serverContext.getWorkspace();

        if (item == null || category == null) {
            PageParameters pageParameters = new PageParameters();
            addItemParam(workspace, pageParameters);
            throw new RestartResponseException(getPageClass(), pageParameters);
        }

        workspaceMenuItem = workspace.menu()
                .getCategories()
                .stream()
                .filter(i -> i.getName().equalsIgnoreCase(category))
                .map(WorkspaceMenuCategory::getWorkspaceMenuItens)
                .flatMap(Collection::stream)
                .filter(i -> i.getName().equals(item)).findFirst()
                .map(Model::new)
                .orElse(null);

        if (workspaceMenuItem != null && workspaceMenuItem.getObject() != null) {
            add(new Form<Void>("form").add(newBoxContent()));
        } else {
            getLogger().error("As configurações de caixas não foram encontradas. Verfique se as permissões estão configuradas corretamente");
            throw new RestartResponseException(Page403.class);
        }
    }

    private void addItemParam(Workspace workspace, PageParameters pageParameters) {
        workspace.menu().getCategories()
                .stream()
                .filter(i -> !i.getWorkspaceMenuItens().isEmpty())
                .findFirst().ifPresent(category -> {
            pageParameters.add(CATEGORY_PARAM_NAME, category.getName());
            pageParameters.add(ITEM_PARAM_NAME, category.getWorkspaceMenuItens().stream().findFirst().map(WorkspaceMenuItem::getName).orElse(null));
        });
    }

    private Component newBoxContent() {
        return workspaceMenuItem.getObject().newContent("box");
    }

    protected Map<String, String> createLinkParams() {
        return new HashMap<>();
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return workspaceMenuItem.getObject().getDescription();
            }
        };
    }

    @Override
    protected IModel<String> getContentTitle() {
        return new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return workspaceMenuItem.getObject().getName();
            }
        };
    }

    @Override
    public IModel<String> getHelpText() {
        return new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return workspaceMenuItem.getObject().getHelpText();
            }
        };
    }

    protected String getIdUsuario() {
        return userDetailsProvider.getOptional(SingularRequirementUserDetails.class)
                .map(SingularRequirementUserDetails::getUsername)
                .orElse(null);
    }

    protected String getIdPessoa() {
        return userDetailsProvider.getOptional(SingularRequirementUserDetails.class)
                .map(SingularRequirementUserDetails::getApplicantId)
                .orElse(null);
    }

    @Nonnull
    @Override
    protected WebMarkupContainer buildPageMenu(String id) {
        return new Menu(id, getClass());
    }
}