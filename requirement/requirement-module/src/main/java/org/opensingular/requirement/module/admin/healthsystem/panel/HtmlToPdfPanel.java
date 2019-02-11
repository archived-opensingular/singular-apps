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

package org.opensingular.requirement.module.admin.healthsystem.panel;

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.admin.healthsystem.stypes.SIPdfHealth;
import org.opensingular.requirement.module.admin.healthsystem.stypes.STypePdfHealth;
import org.opensingular.requirement.module.wicket.view.SingularToastrHelper;
import org.opensingular.ws.wkhtmltopdf.client.RestfulHtmlToPdfConverter;

import javax.inject.Inject;
import java.io.File;
import java.util.Optional;


public class HtmlToPdfPanel extends Panel implements Loggable {

    @Inject
    private HtmlToPdfConverter htmlToPdfConverter;

    public HtmlToPdfPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Form form = new Form("formExport");
        form.setMultiPart(true);
        SingularFormPanel singularFormPanel = new SingularFormPanel("htmlExport", STypePdfHealth.class);
        form.add(new PDFDownloadLink("exportRest", form, (IModel<SIPdfHealth>) (IModel) singularFormPanel.getInstanceModel()) {
            @Override
            File getConvertedFile(SIPdfHealth instance) {
                final HtmlToPdfConverter converter    = new RestfulHtmlToPdfConverter(instance.getEndpoint());
                final HtmlToPdfDTO       htmlToPdfDTO = new HtmlToPdfDTO(instance.getHtmlToExport());
                htmlToPdfDTO.addParam("--title");
                htmlToPdfDTO.addParam("PDF de Teste");
                final Optional<File> convert = converter.convert(htmlToPdfDTO);
                return convert.orElse(null);
            }
        });
        form.add(new PDFDownloadLink("exportLocal", form, (IModel<SIPdfHealth>) (IModel) singularFormPanel.getInstanceModel()) {
            @Override
            File getConvertedFile(SIPdfHealth instance) {
                final Optional<File> convert = htmlToPdfConverter.convert(new HtmlToPdfDTO(instance.getHtmlToExport()));
                return convert.orElse(null);
            }
        });
        form.add(singularFormPanel);
        add(form);
    }

    private abstract class PDFDownloadLink extends SubmitLink {

        private IModel<SIPdfHealth> modelOfInstance;

        PDFDownloadLink(String id, Form<?> form, IModel<SIPdfHealth> model) {
            super(id, form);
            this.modelOfInstance = model;
        }

        @Override
        public void onSubmit() {
            super.onSubmit();
            final File file = getConvertedFile(modelOfInstance.getObject());
            if (file == null) {
                getLogger().info("Não foi possível converter o PDF");
                new SingularToastrHelper(this).
                        addToastrMessage(ToastrType.ERROR, "Não foi possível fazer o donwload do PDF");
                return;
            }
            final IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(file));
            getRequestCycle().scheduleRequestHandlerAfterCurrent(
                    new ResourceStreamRequestHandler(resourceStream) {
                        @Override
                        public void respond(IRequestCycle requestCycle) {
                            super.respond(requestCycle);
                            Files.remove(file);
                        }
                    }
                            .setFileName(file.getName())
                            .setCacheDuration(Duration.NONE)
                            .setContentDisposition(ContentDisposition.ATTACHMENT));
        }

        abstract File getConvertedFile(SIPdfHealth instance);

    }


}






