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

package org.opensingular.requirement.sei31.features;

import java.util.Optional;
import javax.annotation.Nonnull;

import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.view.richtext.RichTextAction;
import org.opensingular.form.view.richtext.RichTextInsertContext;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.lib.commons.ui.Icon;

public class InsertLinkSEIButtonRichText implements RichTextAction<RichTextInsertContext> {

    private IFunction<SILinkSEI, String> functionActionLink;

    public InsertLinkSEIButtonRichText(@Nonnull IFunction<SILinkSEI, String> functionActionLink) {
        this.functionActionLink = functionActionLink;
    }

    @Override
    public String getLabel() {
        return "Inserir um Link para processo ou documento do SEI!";
    }

    @Override
    public Icon getIcon() {
        return (Icon) () -> "sei-icon";
    }

    @Override
    public Optional<Class<? extends SType<?>>> getForm() {
        return Optional.of(STypeLinkSEI.class);
    }

    @Override
    public boolean getLabelInline() {
        return false;
    }

    @Override
    public Class<? extends RichTextInsertContext> getType() {
        return RichTextInsertContext.class;
    }

    @Override
    public void onAction(RichTextInsertContext richTextActionContext, Optional<SInstance> optional) {
        optional.ifPresent(instance -> {
            SILinkSEI instanceLinkSei = (SILinkSEI) instance; //NOSONAR
            String protocolo = instanceLinkSei.getProtocolo();
            String idProtocolo = functionActionLink.apply(instanceLinkSei);
            if(protocolo != null && idProtocolo != null) {
                //This insert is exactly the same of used in the SEI service.
                String retornoFormatado = "<span contenteditable=\"false\" style=\"text-indent:0px;\">"
                        + "<a id=lnkSei" + idProtocolo + " class=\"ancoraSei\" style=\"text-indent:0px;\">" + protocolo + "</a>"
                        + "</span>";
                richTextActionContext.setReturnValue(retornoFormatado);
            }
        });
    }
}
