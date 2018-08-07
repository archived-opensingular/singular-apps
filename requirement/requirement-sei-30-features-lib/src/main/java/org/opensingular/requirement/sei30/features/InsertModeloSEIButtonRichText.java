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

package org.opensingular.requirement.sei30.features;

import java.util.Optional;
import javax.annotation.Nonnull;

import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.view.richtext.RichTextAction;
import org.opensingular.form.view.richtext.RichTextContentContext;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.lib.commons.ui.Icon;

public class InsertModeloSEIButtonRichText implements RichTextAction<RichTextContentContext> {

    private IFunction<SIModeloSEI, String> functionActionModelo;

    public InsertModeloSEIButtonRichText(@Nonnull IFunction<SIModeloSEI, String> functionActionLink) {
        this.functionActionModelo = functionActionLink;
    }

    @Override
    public String getLabel() {
        return "Inserir um Modelo do SEI!";
    }

    @Override
    public Icon getIcon() {
        return (Icon) () -> "document-model-icon";
    }

    @Override
    public Optional<Class<? extends SType<?>>> getForm() {
        return Optional.of(STypeModeloSEI.class);
    }

    @Override
    public boolean getLabelInline() {
        return false;
    }

    @Override
    public Class<? extends RichTextContentContext> getType() {
        return RichTextContentContext.class;
    }

    @Override
    public void onAction(RichTextContentContext richTextActionContext, Optional<SInstance> optional) {
        optional.ifPresent(instance -> {
            SIModeloSEI instanceLinkSei = (SIModeloSEI) instance;  //NOSONAR
            richTextActionContext.setReturnValue(functionActionModelo.apply(instanceLinkSei));
        });
    }
}
