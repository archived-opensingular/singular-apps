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

package org.opensingular.requirement.module.wicket.view.panel;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.resource.ContentDisposition;
import org.opensingular.form.wicket.link.FileDownloadLink;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.lib.wicket.util.util.Shortcuts;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;


public class NotificationPanel extends GenericPanel<Pair<String, String>> {
    @Inject
    private HtmlToPdfConverter htmlToPdfConverter;

    private WebMarkupContainer  contentWrapper;
    private HiddenField<String> content;

    public NotificationPanel(String id, IModel<Pair<String, String>> model) {
        super(id, model);
        add(createLabel());
        add(createExportLink());
        add(createContentHiddenField());
        add(createContentWrapper());
    }

    private FileDownloadLink createExportLink() {
        return new FileDownloadLink("export-to-pdf", getPdfModel(), ContentDisposition.INLINE, getModelObject() + ".pdf");
    }

    private Label createLabel() {
        return new Label("title", getModelObject().getKey());
    }

    private HiddenField<String> createContentHiddenField() {
        content = new HiddenField<>("content", Shortcuts.$m.map(getModel(), Pair::getValue));
        return content;
    }

    private WebMarkupContainer createContentWrapper() {
        contentWrapper = new WebMarkupContainer("contentWrapper");
        contentWrapper.add(Shortcuts.$b.onReadyScript(this::loadContentSanboxedScript));
        return contentWrapper;
    }

    private String loadContentSanboxedScript() {
        UUID   frameId = UUID.randomUUID();
        String script  = "(function(){";
        script += "$('#" + contentWrapper.getMarkupId(true) + "').html(\"<iframe ";
        script += " id=\\\"" + frameId + "\\\" style=\\\"height: 100%; width: 100%;\\\" frameborder=\\\"0\\\"></iframe>\");";
        script += " var frame = $('#" + frameId + "')[0].contentWindow";
        script += " || $('#" + frameId + "')[0].contentDocument.document";
        script += " || $('#" + frameId + "')[0].contentDocument;";
        script += " frame.document.open();";
        script += " frame.document.write( $(\"#" + content.getMarkupId(true) + "\").val());";
        script += " frame.document.close();";
        script += "}());";
        return script;
    }

    private IModel<File> getPdfModel() {
        return new LoadableDetachableModel<File>() {
            @Override
            protected File load() {
                HtmlToPdfDTO dto = new HtmlToPdfDTO();
                dto.setBody(getModelObject().getValue());
                return htmlToPdfConverter.convert(dto).orElse(null);
            }
        };
    }

}