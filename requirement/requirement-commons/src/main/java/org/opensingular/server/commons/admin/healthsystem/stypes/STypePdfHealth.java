package org.opensingular.server.commons.admin.healthsystem.stypes;

import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeHTML;
import org.opensingular.form.type.core.STypeString;
import org.opensingular.form.view.SViewByRichText;
import org.opensingular.ws.wkhtmltopdf.client.RestfulHtmlToPdfConverter;

import javax.annotation.Nonnull;

@SInfoType(spackage = SSystemHealthPackage.class, newable = true, name = "pdfhealth")
public class STypePdfHealth extends STypeComposite<SIPdfHealth> {

    public STypeString endpoint;
    public STypeHTML htmlToExport;

    public STypePdfHealth() {
        super(SIPdfHealth.class);
    }

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        endpoint = this.addFieldString("endpoint");
        htmlToExport = this.addField("htmlToExport", STypeHTML.class);

        endpoint.setInitialValue(RestfulHtmlToPdfConverter.getEndpointDefault());
        endpoint.asAtr()
                .label("Endpoint:")
                .maxLength(155)
                .enabled(true);

        htmlToExport
                .withInitListener(s -> s.setValue(" teste "))
                .withView(SViewByRichText::new)
                .asAtr()
                .label("HTML Exportação");

    }
}
