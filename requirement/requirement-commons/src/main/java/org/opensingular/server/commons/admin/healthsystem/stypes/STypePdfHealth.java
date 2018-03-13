package org.opensingular.server.commons.admin.healthsystem.stypes;

import org.opensingular.form.SInfoType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.TypeBuilder;
import org.opensingular.form.type.core.STypeHTML;
import org.opensingular.form.type.core.STypeString;
import org.opensingular.form.validation.validator.StringValidators;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.ws.wkhtmltopdf.constains.HtmlToPdfConstants;

import javax.annotation.Nonnull;
import java.util.Optional;

@SInfoType(spackage = SSystemHealthPackage.class, newable = true, name = "pdfhealth")
public class STypePdfHealth extends STypeComposite<SIPdfHealth> {

    public STypeString endpoint;
    public STypeHTML htmlToExport;

    public STypePdfHealth() {
        super(SIPdfHealth.class);
    }

    @Override
    protected void onLoadType(@Nonnull TypeBuilder tb) {
        final Optional<String> endereco = SingularProperties.getOpt(HtmlToPdfConstants.ENDPOINT_WS_WKHTMLTOPDF);
        endpoint = this.addFieldString("endpoint");
        endereco.ifPresent(url -> endpoint.setInitialValue(url));
        endpoint.addInstanceValidator(StringValidators.isNotBlank("Endpoint obrigatório"));
        endpoint.asAtr()
                .label("Endpoint:")
                .maxLength(155)
                .enabled(true);
        htmlToExport = this.addField("htmlToExport", STypeHTML.class);
        htmlToExport.asAtr().label("HTML Exportação");
    }
}
