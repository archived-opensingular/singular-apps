package org.opensingular.server.commons.wicket.view.panel;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContentDisposition;
import org.opensingular.form.wicket.link.FileDownloadLink;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.p.commons.PServerFreeMarkerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


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
        return new Model<>();
    }

}