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

package org.opensingular.server.commons.wicket.view.template;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.opensingular.lib.wicket.util.template.SkinOptions;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

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
        add(new Link<Void>("baseurlAnchor") {
            @Override
            public void onClick() {
                throw new RedirectToUrlException(RequestCycle.get().getRequest().getFilterPath());
            }
        });
    }

    protected TopMenu configureTopMenu(String id) {
        return new TopMenu(id, option);
    }
}
