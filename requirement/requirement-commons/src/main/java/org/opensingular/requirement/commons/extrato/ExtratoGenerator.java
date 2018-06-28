package org.opensingular.requirement.commons.extrato;

import org.opensingular.form.SInstance;
import org.opensingular.form.flatview.FlatViewContext;
import org.opensingular.lib.commons.canvas.HtmlCanvas;
import org.opensingular.lib.commons.canvas.bootstrap.BootstrapHtmlCanvas;

import static org.opensingular.form.flatview.FlatViewGenerator.ASPECT_FLAT_VIEW_GENERATOR;

public class ExtratoGenerator implements Generator {

    public String generate(SInstance root) {
        HtmlCanvas htmlCanvas = new BootstrapHtmlCanvas(true);
        root.getAspect(ASPECT_FLAT_VIEW_GENERATOR).ifPresent(i -> i.writeOnCanvas(htmlCanvas, new FlatViewContext(root)));
        return htmlCanvas.build();
    }
}
