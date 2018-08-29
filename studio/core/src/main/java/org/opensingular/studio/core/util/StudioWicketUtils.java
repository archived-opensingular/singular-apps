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

package org.opensingular.studio.core.util;

import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class StudioWicketUtils {

    public static final String PLACEHOLDER = "REPLACE_ME";

    /**
     * Monta a URL completa a partir da pagina informada e do "path"
     * <p>
     * A pagina deve estar anotada com @MountPath e com o parametro ${path} em seu valor,
     * por exemplo, caso a entrada possua uma pagina que esteja anotada com @MountPath("/page/${path}")
     * e um path com valor de "foo/bar", o valor retornado será "/page/foo/bar"
     *
     * @param pathURI o path que será substituido na URL
     * @return a url completa
     */
    public static String getMergedPathIntoURL(String pathURI) {
        String[] paths                       = pathURI.split("/");
        String   moutedPathWithPathParameter = "";
        if (paths.length > 0 && WebApplication.exists()) {
            final WebApplication        webApplication    = WebApplication.get();
            final IRequestMapper        rootRequestMapper = webApplication.getRootRequestMapper();
            final Class<? extends Page> homePage          = webApplication.getHomePage();
            final PageParameters        pageParameters    = new PageParameters().add("path", PLACEHOLDER);
            final Url                   url               = rootRequestMapper.mapHandler(new BookmarkablePageRequestHandler(new PageProvider(homePage, pageParameters)));
            moutedPathWithPathParameter = "/" + url.getPath().replace(PLACEHOLDER, pathURI);
        }
        return getServerContextPath() + moutedPathWithPathParameter;
    }

    public static String getServerContextPath() {
        return WebApplication.get().getServletContext().getContextPath();
    }

}
