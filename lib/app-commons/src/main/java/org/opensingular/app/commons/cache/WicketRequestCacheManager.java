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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.Collection;

/**
 * Cache manager proxy para que o cache dure apenas a sessão http do usuário
 * Se o cache for utilizado fora de uma request http seus valores serão cacheados no cache default.
 */
@Named(WicketRequestCacheManager.WICKET_REQUEST_CACHE_MANAGER)
public class WicketRequestCacheManager implements CacheManager {

    public static final String WICKET_REQUEST_CACHE_MANAGER = "wicketRequestCacheManager";

    @Inject
    private CacheManager cacheManager;

    @Override
    public Cache getCache(String name) {
        if (cacheEnabled()) {
            return cacheManager.getCache(SingularRequestCache.SINGULAR_CACHE_REQUEST_CACHE);
        } else {
            return cacheManager.getCache(SingularCache.SINGULAR_CACHE_NAME);
        }
    }

    private boolean cacheEnabled() {
        return RequestCycle.get() != null;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Arrays.asList(new String[]{SingularRequestCache.SINGULAR_CACHE_REQUEST_CACHE});
    }

    public void clearCache() {
        Cache cache = cacheManager.getCache(SingularRequestCache.SINGULAR_CACHE_REQUEST_CACHE);
        if(cache != null) {
            cache.clear();
        }
    }
}
