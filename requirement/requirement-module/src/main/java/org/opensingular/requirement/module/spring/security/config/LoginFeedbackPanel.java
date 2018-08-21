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

package org.opensingular.requirement.module.spring.security.config;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FencedFeedbackPanel;

public class LoginFeedbackPanel extends FencedFeedbackPanel {
    public LoginFeedbackPanel(String id, Component fence) {
        super(id, fence);
    }

    @Override
    protected String getCSSClass(FeedbackMessage message) {
        String css;
        switch (message.getLevel()) {
            case FeedbackMessage.SUCCESS:
                css = "list-unstyled alert alert-success";
                break;
            case FeedbackMessage.INFO:
                css = "list-unstyled alert alert-info";
                break;
            case FeedbackMessage.ERROR:
                css = "list-unstyled alert alert-danger ";
                break;
            default:
                css = "list-unstyled alert";
        }

        return css;
    }
}