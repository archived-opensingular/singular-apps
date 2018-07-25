/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.module.wicket.error;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Throwables;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.joda.time.DateTime;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.exception.SingularServerIntegrationException;
import org.opensingular.requirement.module.service.SendEmailToSupportService;
import org.opensingular.requirement.module.wicket.view.template.ServerTemplate;
import org.springframework.transaction.UnexpectedRollbackException;
import org.wicketstuff.annotation.mount.MountPath;

import static java.lang.String.format;

@MountPath("public/error/500")
public class Page500 extends ServerTemplate implements Loggable {
    private Exception exception;

    @Inject
    private SendEmailToSupportService singularSupportService;

    public Page500() {
        build();
    }

    public Page500(Exception exception) {
        this.exception = exception;
        build();
    }

    @Override
    protected boolean isWithMenu() {
        return false;
    }

    private final WebMarkupContainer detail = new WebMarkupContainer("detail");

    protected void build() {
        String errorCode = errorCode();
        if (exception != null) {
            getLogger().error(errorCode, this.exception);
            sendExceptionEmail(errorCode, getStackTraceString(), getUrlException());
        }
        add(new Label("codigo-erro", Model.of(errorCode)));
        pageHeader.setVisible(false);
        add(detail);
        detail.setVisible(false);
        detail.add(new WebMarkupContainer("message"));
        if (exception instanceof SingularServerIntegrationException) {
            detail.setVisible(true);
            detail.replace(new Label("message", Model.of(exception.getMessage())));
        }
    }

    private void sendExceptionEmail(String errorCode, String stackTrace, String urlException) {
        try{
            singularSupportService.sendEmailToSupport(errorCode, stackTrace, urlException);
        }catch (UnexpectedRollbackException e){
            getLogger().error(e.getMessage(), e);
            getLogger().warn("A Rollback happened because of a exception while sending an e-mail to support {}", e.getMessage());
        }
    }

    private String getStackTraceString() {
        return Throwables.getStackTraceAsString(exception);
    }

    private String getUrlException() {
        return ((HttpServletRequest)getRequest().getContainerRequest()).getRequestURL()+"?"
                +((HttpServletRequest)getRequest().getContainerRequest()).getQueryString();
    }

    @Override
    protected IModel<String> getContentTitle() {
        return Model.of("");
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return Model.of("");
    }

    private static String errorCode() {
        DateTime now = DateTime.now();
        return format("SER-%04d-%02d%02d%02d-%02d%02d-%04d ",
                get(now.year()), get(now.monthOfYear()), get(now.dayOfMonth()),
                get(now.hourOfDay()), get(now.minuteOfHour()), get(now.secondOfMinute()),
                get(now.millisOfSecond()));
    }

    private static int get(DateTime.Property prop) {
        return prop.get();
    }
}

