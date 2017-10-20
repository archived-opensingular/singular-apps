/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.server.commons.wicket.view.panel;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ContentDisposition;
import org.opensingular.form.wicket.link.FileDownloadLink;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.lib.wicket.util.model.IReadOnlyModel;
import org.opensingular.server.commons.service.PetitionInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;


public class NotificationPanel<PI extends PetitionInstance> extends Panel {

    @Inject
    private HtmlToPdfConverter htmlToPdfConverter;

    protected static final Logger LOGGER = LoggerFactory.getLogger(NotificationPanel.class);

    public NotificationPanel(String id, IModel<Pair<String, String>> model, IModel<PI> modelOfpetition) {

        super(id);
        add(new Label("title", model.getObject().getKey()));
        add(new FileDownloadLink("export-to-pdf",
                getPdfModel(model, modelOfpetition),
                ContentDisposition.INLINE,
                model.getObject().getValue() + ".pdf")
        );
        add(new Label("content", model.getObject().getValue()).setEscapeModelStrings(false));
    }

    protected IModel<File> getPdfModel(IModel<Pair<String, String>> model, IModel<PI> modelOfpetition) {
        return new IReadOnlyModel<File>() {
            @Override
            public File getObject() {
                HtmlToPdfDTO dto = new HtmlToPdfDTO();
                dto.setBody(model.getObject().getValue());
                return htmlToPdfConverter.convert(dto).orElse(null);
            }
        };
    }

}