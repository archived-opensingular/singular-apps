package org.opensingular.requirement.sei30.features;

import java.util.Optional;
import javax.annotation.Nonnull;

import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.view.richtext.RichTextAction;
import org.opensingular.form.view.richtext.RichTextContentContext;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.lib.commons.ui.Icon;

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
    public Icon getIcon() {
        return (Icon) () -> "document-model-icon";
    }

    @Override
    public Optional<Class<? extends SType<?>>> getForm() {
        return Optional.of(STypeModeloSEI.class);
    }

    @Override
    public boolean getLabelInline() {
        return false;
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
