package org.opensingular.server.commons.wicket;

import org.apache.wicket.model.IModel;
import org.opensingular.server.commons.wicket.view.template.Content;
import org.opensingular.server.commons.wicket.view.template.Template;
import static org.opensingular.lib.wicket.util.util.Shortcuts.*;

public class FooTemplatePage extends Template {
    @Override
    protected Content getContent(String id) {
        return new Content(id) {
            @Override
            protected IModel<?> getContentTitleModel() {
                return $m.ofValue("Test page");
            }

            @Override
            protected IModel<?> getContentSubtitleModel() {
                return $m.ofValue("Test page");
            }
        };
    }
}
