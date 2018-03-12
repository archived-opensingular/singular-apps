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

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.wicket.util.template.admin.SingularAdminTemplate;
import org.opensingular.server.commons.wicket.view.SingularToastrHelper;

import javax.annotation.Nonnull;

public abstract class ServerTemplate extends SingularAdminTemplate {

    public ServerTemplate() {
    }


    public ServerTemplate(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected @Nonnull
    WebMarkupContainer buildPageMenu(String id) {
        return new Menu(id, (Class<? extends WebPage>) this.getPageClass());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forUrl("/singular-static/resources/singular/fonts/google/open-sans.css"));
        response.render(JavaScriptReferenceHeaderItem.forReference(new PackageResourceReference(ServerTemplate.class, "singular.js")));
        response.render(CssHeaderItem.forReference(new PackageResourceReference(ServerTemplate.class, "ServerTemplate.css")));
        if (SingularProperties.get().isTrue(SingularProperties.ANALYTICS_ENABLED)) {
            response.render(JavaScriptHeaderItem.forUrl(skinnableResource("/layout4/scripts/analytics.js")));
        }
    }

    protected StringResourceModel getMessage(String prop) {
        return new StringResourceModel(prop.trim(), this, null);
    }

    public void addToastrSuccessMessage(String messageKey, String... args) {
        new SingularToastrHelper(this).
                addToastrMessage(ToastrType.SUCCESS, messageKey, args);
    }

    public void addToastrErrorMessage(String messageKey, String... args) {
        new SingularToastrHelper(this).
                addToastrMessage(ToastrType.ERROR, messageKey, args);
    }

    public void addToastrWarningMessage(String messageKey, String... args) {
        new SingularToastrHelper(this).
                addToastrMessage(ToastrType.WARNING, messageKey, args);
    }

    public void addToastrInfoMessage(String messageKey, String... args) {
        new SingularToastrHelper(this).
                addToastrMessage(ToastrType.INFO, messageKey, args);
    }

    public void addToastrSuccessMessageWorklist(String messageKey, String... args) {
        new SingularToastrHelper(this).
                addToastrMessageWorklist(ToastrType.SUCCESS, messageKey, args);
    }

    public void addToastrErrorMessageWorklist(String messageKey, String... args) {
        new SingularToastrHelper(this).
                addToastrMessageWorklist(ToastrType.ERROR, messageKey, args);
    }

    public void addToastrWarningMessageWorklist(String messageKey, String... args) {
        new SingularToastrHelper(this).
                addToastrMessageWorklist(ToastrType.WARNING, messageKey, args);
    }

    protected void addToastrInfoMessageWorklist(String messageKey, String... args) {
        new SingularToastrHelper(this).
                addToastrMessageWorklist(ToastrType.INFO, messageKey, args);
    }

}
