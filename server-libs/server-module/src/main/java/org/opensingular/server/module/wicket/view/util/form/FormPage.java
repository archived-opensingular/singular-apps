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

package org.opensingular.server.module.wicket.view.util.form;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.wicket.view.form.AbstractFormPage;
import org.opensingular.server.commons.wicket.view.form.FormPageConfig;
import org.wicketstuff.annotation.mount.MountPath;

import javax.annotation.Nullable;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

@SuppressWarnings("serial")
@MountPath("/view")
public class FormPage extends AbstractFormPage<PetitionEntity> {


    public FormPage() {
        this(null);
    }

    public FormPage(@Nullable FormPageConfig config) {
        super(PetitionEntity.class, config);
    }

    @Override
    protected IModel<?> getContentTitleModel() {
        return $m.get(() -> getSingularFormPanel().getRootTypeSubtitle());
    }

    @Override
    protected IModel<?> getContentSubtitleModel() {
        return $m.get(() -> {
            if (getIdentifier().isPresent()) {
//                return currentModel.getObject().getDescription();
                return "";
            }
            return new ResourceModel("label.form.content.title", "Nova Solicitação").getObject();
        });
    }

    @Override
    protected Optional<String> getIdentifier() {
        return getPetitionOptional()
                .map(PetitionEntity::getCod)
                .map(Object::toString);
    }
}
