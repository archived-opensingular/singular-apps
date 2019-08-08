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

package org.opensingular.app.commons.cache;

import org.apache.wicket.request.cycle.RequestCycle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SingularSessionKeyGenerator extends SingularKeyGenerator {
    public static final  String SINGULAR_SESSION_KEY_GENERATOR = "singularSessionKeyGenerator";
    private static final String CACHE_KEYS_ATTRIBUTE_NAME      = "#SINGULAR#CACHE#KEYS";

    public static Optional<Set<String>> extractCacheKeysFrom(HttpSession session) {
        return Optional.ofNullable((Set<String>) session.getAttribute(SingularSessionKeyGenerator.CACHE_KEYS_ATTRIBUTE_NAME));
    }

    @Override
    protected String internalGenerateKey(String methodName, String methodReturnType, String[] parameters, Object[] params) {
        String generatedKey = super.internalGenerateKey(methodName, methodReturnType, parameters, params);
        if (RequestCycle.get() != null) {
            final HttpSession httpSession = lookupSession();
            generatedKey = getSessionCachePrefix(httpSession.getId()) + generatedKey;
            final HashSet<String> cacheKeys = new HashSet<>();
            extractCacheKeysFrom(httpSession).ifPresent(cacheKeys::addAll);
            cacheKeys.add(generatedKey);
            httpSession.setAttribute(CACHE_KEYS_ATTRIBUTE_NAME, cacheKeys);
        }
        return generatedKey;
    }

    private HttpSession lookupSession() {
        return ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession();
    }

    public static String getSessionCachePrefix(String sessionId) {
        return "session#" + sessionId + "#";
    }
}