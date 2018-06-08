package org.opensingular.requirement.sei30.features;

import java.util.Objects;
import javax.annotation.Nonnull;

import org.opensingular.form.view.richtext.SViewByRichTextNewTab;
import org.opensingular.lib.commons.lambda.IFunction;

public class SViewSEIRichText extends SViewByRichTextNewTab {

    private SViewSEIRichText() {
        getConfiguration().setDoubleClickDisabledForCssClasses("ancoraSei");
    }

    public static ModeloSEIActionBuilder configLinkSEIAction(@Nonnull IFunction<SILinkSEI, String> functionActionLink) {
        Objects.requireNonNull(functionActionLink, "Action Link Function must not be null!");
        SViewSEIRichText view = new SViewSEIRichText();
        view.configureLinkSEIAction(functionActionLink);
        return new ModeloSEIActionBuilder(view);
    }

    void configureLinkSEIAction(IFunction<SILinkSEI, String> functionActionLink) {
        this.addAction(new InsertLinkSEIButtonRichText(functionActionLink));
    }


    void configureModeloSeiAction(IFunction<SIModeloSEI, String> functionActionLink) {
        this.addAction(new InsertModeloSEIButtonRichText(functionActionLink));

    }
}
