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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.opensingular.requirement.module.spring.security.SecurityAuthPaths;
import org.opensingular.requirement.module.spring.security.SecurityAuthPathsFactory;
import org.opensingular.requirement.module.wicket.SingularSession;

import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class TopMenu extends Panel {

    public TopMenu(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        buildContent();
    }

    protected void buildContent() {
        queue(new Label("nome", $m.ofValue(SingularSession.get().getName())));

        WebMarkupContainer avatar    = new WebMarkupContainer("codrh");
        Optional<String>   avatarSrc = Optional.ofNullable(null);
        avatarSrc.ifPresent(src -> avatar.add($b.attr("src", src)));
        queue(avatar);
        avatar.setVisible(avatarSrc.isPresent());

        Link logout = new Link("logout") {
            SecurityAuthPaths securityAuthPaths;

            @Override
            public void onClick() {
                if (securityAuthPaths == null) {
                    securityAuthPaths = new SecurityAuthPathsFactory().get();
                }
                throw new RedirectToUrlException(securityAuthPaths.getLogoutPath(RequestCycle.get()));
            }
        };
        queue(logout);
        this.setVisible(SingularSession.get().getUserDetails() != null);
    }

}
