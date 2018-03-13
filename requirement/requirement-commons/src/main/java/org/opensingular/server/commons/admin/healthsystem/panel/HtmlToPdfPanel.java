package org.opensingular.server.commons.admin.healthsystem.panel;

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.ContentDisposition;
import org.opensingular.form.wicket.link.FileDownloadLink;
import org.opensingular.form.wicket.panel.SingularFormPanel;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.admin.healthsystem.stypes.SIPdfHealth;
import org.opensingular.server.commons.admin.healthsystem.stypes.STypePdfHealth;
import org.opensingular.server.commons.wicket.view.SingularToastrHelper;
import org.opensingular.ws.wkhtmltopdf.client.RestfulHtmlToPdfConverter;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public class HtmlToPdfPanel extends Panel implements Loggable {

    public HtmlToPdfPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Form form = new Form("formExport");
        SingularFormPanel singularFormPanel = new SingularFormPanel("htmlExport", STypePdfHealth.class);
        FileDownloadLink link = new FileDownloadLink("exportHtml", ContentDisposition.ATTACHMENT, String.format("temp-%s.pdf", UUID.randomUUID())) {
            @Override
            public void onClick() {
                final File file = getConvertedFile((SIPdfHealth) singularFormPanel.getInstance());
                if (file != null) {
                    super.downloadFile(file);
                } else {
                    getLogger().info("Não foi possível converter o PDF");
                    new SingularToastrHelper(this).
                            addToastrMessage(ToastrType.ERROR, "Não foi possível fazer o donwload do PDF");
                }
            }
        };
        form.add(singularFormPanel);
        form.add(link);
        add(form);
    }

    private File getConvertedFile(SIPdfHealth instance) {
        final HtmlToPdfConverter converter = new RestfulHtmlToPdfConverter(instance.getEndpoint());
        final HtmlToPdfDTO htmlToPdfDTO = new HtmlToPdfDTO(instance.getHtmlToExport());
        htmlToPdfDTO.addParam("--title");
        htmlToPdfDTO.addParam("PDF de Teste");
        htmlToPdfDTO.addParam("--print-media-type");
        htmlToPdfDTO.addParam("--load-error-handling");
        htmlToPdfDTO.addParam("ignore");
        final Optional<File> convert = converter.convert(htmlToPdfDTO);
        return convert.orElse(null);
    }
}






