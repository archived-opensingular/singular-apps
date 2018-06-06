package org.opensingular.requirement.sei30.features;

import java.util.Objects;
import javax.annotation.Nonnull;

import org.opensingular.lib.commons.lambda.IFunction;

public class ModeloSEIActionBuilder {

    private SViewSEIRichText view;

    ModeloSEIActionBuilder(SViewSEIRichText view) {
        this.view = view;
    }

    public SViewSEIRichText configureModeloSeiAction(@Nonnull IFunction<SIModeloSEI, String> functionActionLink) {
        Objects.requireNonNull(functionActionLink, "Action Link Function must not be null!");
        view.configureModeloSeiAction(functionActionLink);
        return view;
    }
}
