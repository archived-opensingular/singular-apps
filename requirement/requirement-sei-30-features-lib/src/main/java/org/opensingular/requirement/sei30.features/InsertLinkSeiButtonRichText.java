package org.opensingular.requirement.sei30.features;

import java.util.Optional;
import javax.annotation.Nonnull;

import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.view.richtext.RichTextAction;
import org.opensingular.form.view.richtext.RichTextInsertContext;
import org.opensingular.lib.commons.lambda.IFunction;

public class InsertLinkSeiButtonRichText implements RichTextAction<RichTextInsertContext> {

    private IFunction<SILinkSei, String> functionActionLink;

    public InsertLinkSeiButtonRichText(@Nonnull IFunction<SILinkSei, String> functionActionLink) {
        this.functionActionLink = functionActionLink;
    }

    @Override
    public String getLabel() {
        return "Inserir um Link para processo ou documento do SEI!";
    }

    @Override
    public String getIconUrl() {
        return "http://treinamentosei3singular.antaq.gov.br/sei/editor/ck/plugins/linksei/images/sei.png?t=G2FW";
    }

    @Override
    public Optional<Class<? extends SType<?>>> getForm() {
        return Optional.of(STypeLinkSei.class);
    }

    @Override
    public Boolean getLabelInline() {
        return Boolean.FALSE;
    }

    @Override
    public Class<? extends RichTextInsertContext> getType() {
        return RichTextInsertContext.class;
    }

    @Override
    public void onAction(RichTextInsertContext richTextActionContext, Optional<SInstance> optional) {
        optional.ifPresent(instance -> {
            SILinkSei instanceLinkSei = (SILinkSei) instance;
            String protocolo = instanceLinkSei.getProtocolo();
            String idProtocolo = functionActionLink.apply(instanceLinkSei);
            String retornoFormatado = "<span contenteditable=\"false\" style=\"text-indent:0px;\">"
                    + "<a id=lnkSei" + idProtocolo + " class=\"ancoraSei\" style=\"text-indent:0px;\">" + protocolo + "</a>"
                    + "</span>";
            richTextActionContext.setReturnValue(retornoFormatado);
        });
    }
}
