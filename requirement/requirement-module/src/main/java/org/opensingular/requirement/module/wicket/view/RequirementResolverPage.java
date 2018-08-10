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

package org.opensingular.requirement.module.wicket.view;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;
import org.opensingular.requirement.module.RequirementDefinition;
import org.opensingular.requirement.module.SingularRequirementResolver;
import org.opensingular.requirement.module.connector.ModuleService;
import org.opensingular.requirement.module.exception.SingularRequirementException;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.wicket.NewRequirementUrlBuilder;
import org.opensingular.requirement.module.wicket.view.form.AbstractFormPage;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

/**
 * For Singular Requirement Resolver use only.
 * See {@link SingularRequirementResolver}
 *
 * @param <RE>
 * @param <RI>
 */
public class RequirementResolverPage<RE extends RequirementEntity, RI extends RequirementInstance> extends AbstractFormPage<RI> {

    @Inject
    private ModuleService moduleService;

    public RequirementResolverPage(@Nullable ActionContext context) {
        super(context);
    }

    public RequirementResolverPage(@Nullable ActionContext context, @Nullable Class<? extends SType<?>> formType) {
        super(context, formType);
    }


    @Override
    protected void send(IModel<? extends SInstance> mi, AjaxRequestTarget ajxrt, BSModalBorder sm) {
        SingularRequirementResolver requirementResolver = (SingularRequirementResolver) getSingularRequirement(getConfig().copyOfInnerActionContext()).orElseThrow(() -> new SingularRequirementException("No requirement definition found!"));
        RequirementDefinition requirement = requirementResolver.resolve((SIComposite) mi.getObject());
        redirectToResolvedRequirement(requirement.getKey(), new HashMap<>(0));
    }

    @Override
    protected ServerSendButton makeServerSendButton(String id, IModel<? extends SInstance> formInstance, BSModalBorder enviarModal) {
        return new ServerSendButton(id, formInstance, null) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                RequirementResolverPage.this.send(formInstance, target, null);
            }
        };
    }

    /**
     * Override this method to send additional URL parameters to create a new Requirement of the given type (i.e: requirement definition with id {@param idRequirementDefinition})
     *
     * @param requirementDefinitionKey
     * @param additionalParameters
     */
    protected void redirectToResolvedRequirement(String requirementDefinitionKey, Map<String, String> additionalParameters) {
        throw new RedirectToUrlException(new NewRequirementUrlBuilder(moduleService.getBaseUrl(), requirementDefinitionKey).getURL(additionalParameters));
    }

    @Nonnull
    @Override
    protected Optional<String> getIdentifier() {
        return Optional.empty();
    }

    @Override
    protected IModel<String> getContentTitle() {
        return $m.get(() -> getSingularFormPanel().getRootTypeSubtitle());
    }

    @Override
    protected Component buildSaveButton(String id) {
        return new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return false;
            }
        };
    }
}
