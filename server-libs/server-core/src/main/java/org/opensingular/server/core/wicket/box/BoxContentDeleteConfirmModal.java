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
package org.opensingular.server.core.wicket.box;


import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import java.io.Serializable;

public abstract class BoxContentDeleteConfirmModal<T extends Serializable> extends BoxContentConfirmModal<T> {
    public BoxContentDeleteConfirmModal(IModel<T> dataModel) {
        super(null, dataModel);
    }

    @Override
    protected String getCancelButtonLabel() {
        return new StringResourceModel("label.button.cancel").getString();
    }

    @Override
    protected String getConfirmButtonLabel() {
        return new StringResourceModel("label.button.delete").getString();
    }

    @Override
    protected String getConfirmationMessage() {
        return new StringResourceModel("label.delete.message").getString();
    }

    @Override
    protected String getTitleText() {
        return new StringResourceModel("label.title.delete.draft").getString();
    }
}