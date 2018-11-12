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

package org.opensingular.studio.core.view;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.form.spring.SingularUserDetails;
import org.opensingular.form.spring.UserDetailsProvider;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.studio.core.util.StudioWicketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.inject.Inject;
import java.util.Optional;

public class StudioHeader extends Panel {

    private WebMarkupContainer rightNavbar = new WebMarkupContainer("rightNavbar");

    public StudioHeader(String id) {
        super(id);
        addHomeAnchor();
        addRightNavbar();
        addUsername();
    }

    private void addRightNavbar() {
        rightNavbar.add(new Behavior() {
            @Override
            public void onConfigure(Component component) {
                super.onConfigure(component);
                component.setVisible(getUserDetails().isPresent());
            }
        });
        add(rightNavbar);
    }

    private void addHomeAnchor() {
        WebMarkupContainer anchor = new WebMarkupContainer("homeAnchor");
        anchor.add(new Behavior() {
            @Override
            public void onComponentTag(Component component, ComponentTag tag) {
                super.onComponentTag(component, tag);
                String path = StudioWicketUtils.getServerContextPath();
                if (StringUtils.isBlank(path)) {
                    path = "/";
                }
                tag.put("href", path);

            }
        });
        add(anchor);
    }

    private void addUsername() {
        rightNavbar.add(new Label("username", getUserDetails().map(SingularUserDetails::getDisplayName).orElse("")));
    }

    private Optional<SingularUserDetails> getUserDetails() {
        UserDetails userDetails = ApplicationContextProvider.get().getBean(UserDetailsProvider.class).get();
        if (userDetails instanceof SingularUserDetails) {
            return Optional.of((SingularUserDetails) userDetails);
        } else {
            return Optional.empty();
        }
    }

}
