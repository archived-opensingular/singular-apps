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

package org.opensingular.requirement.module.wicket.view.form;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;
import org.wicketstuff.annotation.mount.MountPath;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

@SuppressWarnings("serial")
@MountPath("/view")
public class FormPage extends AbstractFormPage<RequirementEntity, RequirementInstance> {

    public FormPage() {
        this(null);
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return $m.get(() -> {
            if (getIdentifier().isPresent()) {
                return "";
            }
            return new ResourceModel("label.form.content.title", "Nova Solicitação").getObject();
        });
    }

    @Override
    protected IModel<String> getContentTitle() {
        return $m.get(() -> getSingularFormPanel().getRootTypeSubtitle());
    }

    public FormPage(@Nullable ActionContext context) {
        super(context);
    }

    @Nonnull
    @Override
    protected Optional<String> getIdentifier() {
        return getRequirementOptional()
                .map(RequirementInstance::getCod)
                .map(Object::toString);
    }
}
