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

package org.opensingular.requirement.module.wicket.view.template;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.opensingular.lib.wicket.util.template.SkinOptions;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;

public class Header extends Panel {

    protected boolean withTogglerButton;
    protected SkinOptions option;

    public Header(String id) {
        this(id, false, null);
    }

    public Header(String id, boolean withTogglerButton, SkinOptions option) {
        super(id);
        this.withTogglerButton = withTogglerButton;
        this.option = option;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new WebMarkupContainer("togglerButton")
                .add($b.classAppender("hide", Model.of(!withTogglerButton))));
        add(new WebMarkupContainer("_TopAction")
                .add($b.classAppender("hide")));
        add(configureTopMenu("_TopMenu"));
        addBaseurlAnchor();
    }


    private void addBaseurlAnchor() {
        WebMarkupContainer anchor = new WebMarkupContainer("baseurlAnchor");
        anchor.add(new Behavior() {
            @Override
            public void onComponentTag(Component component, ComponentTag tag) {
                super.onComponentTag(component, tag);
                String path = WebApplication.get().getServletContext().getContextPath();
                if (StringUtils.isBlank(path)) {
                    path = "/";
                }
                tag.put("href", path);
            }
        });
        add(anchor);
    }


    protected TopMenu configureTopMenu(String id) {
        return new TopMenu(id, option);
    }
}
