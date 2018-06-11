package org.opensingular.requirement.sei30.features;

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
            SILinkSEI instanceLinkSei = (SILinkSEI) instance;
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
