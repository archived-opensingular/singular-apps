package org.opensingular.requirement.sei30.features;

import javax.annotation.Nonnull;

import org.opensingular.lib.commons.lambda.IFunction;

public class ModeloSEIActionBuilder {

    private SViewSeiRichText view;

    ModeloSEIActionBuilder(SViewSeiRichText view) {
        this.view = view;
    }

    public SViewSeiRichText configureModeloSeiAction(@Nonnull IFunction<SILinkSei, String> functionActionLink){
        view.configureModeloSeiAction(functionActionLink);
        return view;
    }
}
