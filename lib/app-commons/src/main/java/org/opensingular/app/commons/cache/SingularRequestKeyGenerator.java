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

import org.opensingular.lib.commons.util.Loggable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SingularRequestKeyGenerator extends SingularKeyGenerator implements Loggable {
    public static final  String SINGULAR_REQUEST_KEY_GENERATOR = "singularRequestKeyGenerator";
    private static final String CACHE_KEYS_ATTRIBUTE_NAME      = "#SINGULAR#CACHE#KEYS";

    public static Optional<Set<String>> extractCacheKeysFrom(HttpServletRequest request) {
        return Optional.ofNullable((Set<String>) request.getAttribute(SingularRequestKeyGenerator.CACHE_KEYS_ATTRIBUTE_NAME));
    }

    @Override
    protected String internalGenerateKey(String methodName, String methodReturnType, String[] parameters, Object[] params) {
        String                   generatedKey       = super.internalGenerateKey(methodName, methodReturnType, parameters, params);
        final HttpServletRequest httpServletRequest = lookupRequest();
        if (httpServletRequest != null) {
            generatedKey = getRequestCachePrefix(httpServletRequest) + generatedKey;
            final HashSet<String> cacheKeys = new HashSet<>();
            extractCacheKeysFrom(httpServletRequest).ifPresent(cacheKeys::addAll);
            cacheKeys.add(generatedKey);
            httpServletRequest.setAttribute(CACHE_KEYS_ATTRIBUTE_NAME, cacheKeys);
        }
        return generatedKey;
    }

    private HttpServletRequest lookupRequest() {
        try {
            return Optional.of(RequestContextHolder.currentRequestAttributes())
                    .map(ServletRequestAttributes.class::cast)
                    .map(ServletRequestAttributes::getRequest)
                    .orElse(null);
        } catch (IllegalStateException e) {
            getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    private static String getRequestCachePrefix(HttpServletRequest reqId) {
        return "request#" + Thread.currentThread().getId() + "#";
    }
}