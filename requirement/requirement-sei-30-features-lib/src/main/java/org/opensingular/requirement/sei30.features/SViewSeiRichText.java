package org.opensingular.requirement.sei30.features;

import java.util.Objects;
import javax.annotation.Nonnull;

import org.opensingular.form.view.richtext.SViewByRichTextNewTab;
import org.opensingular.lib.commons.lambda.IFunction;

public class SViewSeiRichText extends SViewByRichTextNewTab {

    private SViewSeiRichText() {
        getConfiguration().setDoubleClickDisabledForCssClasses("ancoraSei");
    }

    public static ModeloSEIActionBuilder configLinkSeiAction(@Nonnull IFunction<SILinkSei, String> functionActionLink) {
        Objects.requireNonNull(functionActionLink, "Action Link Function must not be null!");
        SViewSeiRichText view = new SViewSeiRichText();
        view.configureLinkSeiAction(functionActionLink);
        return new ModeloSEIActionBuilder(view);
    }

    void configureLinkSeiAction(IFunction<SILinkSei, String> functionActionLink) {
        this.addAction(new InsertLinkSeiButtonRichText(functionActionLink));
    }


    void configureModeloSeiAction(IFunction<SILinkSei, String> functionActionLink) {
        //TODO REMOVER ISSO.
        this.addAction(new InsertLinkSeiButtonRichText(functionActionLink));

    }
}
