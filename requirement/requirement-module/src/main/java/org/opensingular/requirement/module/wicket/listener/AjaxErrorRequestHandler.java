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

package org.opensingular.requirement.module.wicket.listener;

import de.alpharogroup.wicket.js.addon.toastr.ToastrType;
import org.apache.wicket.Application;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.http.WebResponse;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.wicket.util.toastr.ToastrHelper;

public class AjaxErrorRequestHandler implements IRequestHandler, Loggable {

    private SingularException singularException;

    public AjaxErrorRequestHandler(SingularException singularException) {
        this.singularException = singularException;
    }

    @Override
    public void respond(IRequestCycle requestCycle) {
        String script = ToastrHelper.generateJs(singularException, ToastrType.ERROR, false);

        WebResponse response = (WebResponse)requestCycle.getResponse();
        final String encoding = Application.get()
                .getRequestCycleSettings()
                .getResponseRequestEncoding();

        // Set content type based on markup type for page
        response.setContentType("text/xml; charset=" + encoding);

        // Make sure it is not cached by a client
        response.disableCaching();

        response.write("<?xml version=\"1.0\" encoding=\"");
        response.write(encoding);
        response.write("\"?>");
        response.write("<ajax-response>");
        response.write("<evaluate><![CDATA[" + script + "]]></evaluate>");
        response.write("</ajax-response>");

        getLogger().error(singularException.getMessage(), singularException);
    }

    @Override
    public void detach(IRequestCycle requestCycle) {
    }

}
