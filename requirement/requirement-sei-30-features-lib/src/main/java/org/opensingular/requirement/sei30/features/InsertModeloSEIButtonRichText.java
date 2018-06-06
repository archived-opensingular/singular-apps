package org.opensingular.requirement.sei30.features;

import java.util.Optional;
import javax.annotation.Nonnull;

import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.view.richtext.RichTextAction;
import org.opensingular.form.view.richtext.RichTextContentContext;
import org.opensingular.lib.commons.lambda.IFunction;

public class InsertModeloSEIButtonRichText implements RichTextAction<RichTextContentContext> {

    private IFunction<SIModeloSEI, String> functionActionModelo;

    public InsertModeloSEIButtonRichText(@Nonnull IFunction<SIModeloSEI, String> functionActionLink) {
        this.functionActionModelo = functionActionLink;
    }

    @Override
    public String getLabel() {
        return "Inserir um Modelo do SEI!";
    }

    @Override
    public String getIconUrl() {
        //TODO ALTERAR ISSO, DEVERÁ SER COLOCADA NA LIB.
        return "https://cdn1.iconfinder.com/data/icons/file-format-set/64/2342-256.png";
    }

    @Override
    public Optional<Class<? extends SType<?>>> getForm() {
        return Optional.of(STypeModeloSEI.class);
    }

    @Override
    public Boolean getLabelInline() {
        return Boolean.FALSE;
    }

    @Override
    public Class<? extends RichTextContentContext> getType() {
        return RichTextContentContext.class;
    }

    @Override
    public void onAction(RichTextContentContext richTextActionContext, Optional<SInstance> optional) {
        optional.ifPresent(instance -> {
            SIModeloSEI instanceLinkSei = (SIModeloSEI) instance;
            richTextActionContext.setReturnValue(functionActionModelo.apply(instanceLinkSei));
        });
    }
}
