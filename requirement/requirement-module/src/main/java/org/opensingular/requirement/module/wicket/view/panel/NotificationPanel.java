/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.wicket.view.panel;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.opensingular.lib.wicket.util.util.Shortcuts;

import java.util.UUID;


public class NotificationPanel extends GenericPanel<Pair<String, String>> {
    private WebMarkupContainer  contentWrapper;
    private HiddenField<String> content;

    public NotificationPanel(String id, IModel<Pair<String, String>> model) {
        super(id, model);
        add(createLabel());
        add(createViewButton());
        add(createContentHiddenField());
        add(createContentWrapper());
    }

    private Component createViewButton() {
        return new Link<String>("viewContent", getContentModel()) {
            @Override
            public void onClick() {
                throw new RestartResponseException(new ViewNotificationPage(getContentModel()));
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("target", "_blank");
            }
        };
    }

    private Label createLabel() {
        return new Label("title", getModelObject().getKey());
    }

    private HiddenField<String> createContentHiddenField() {
        content = new HiddenField<>("content", getContentModel());
        return content;
    }

    private IModel<String> getContentModel() {
        return Shortcuts.$m.map(getModel(), Pair::getValue);
    }

    private WebMarkupContainer createContentWrapper() {
        contentWrapper = new WebMarkupContainer("contentWrapper");
        contentWrapper.add(Shortcuts.$b.onReadyScript(this::loadSanboxedContentScript));
        return contentWrapper;
    }

    private String loadSanboxedContentScript() {
        UUID   frameId = UUID.randomUUID();
        String script  = "(function(){";
        script += "$('#" + contentWrapper.getMarkupId(true) + "').html(\"<iframe ";
        script += " id=\\\"" + frameId + "\\\" style=\\\"height: 100%; width: 100%;\\\" frameborder=\\\"0\\\"></iframe>\");";
        script += " var frame = $('#" + frameId + "')[0].contentWindow";
        script += " || $('#" + frameId + "')[0].contentDocument.document";
        script += " || $('#" + frameId + "')[0].contentDocument;";
        script += " frame.document.open();";
        script += " frame.document.write( $(\"#" + content.getMarkupId(true) + "\").val());";
        script += " frame.document.close();";
        script += "}());";
        return script;
    }
}