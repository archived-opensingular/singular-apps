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

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.opensingular.app.commons.cache.SingularKeyGenerator.SINGULAR_KEY_GENERATOR;

/**
 * Caches data  per wicket http session as long as the session is active.
 * if there is no wicket session it defaults to @SingularCache to cache and lookup
 */
@Cacheable(cacheNames = SingularRequestCache.SINGULAR_CACHE_REQUEST_CACHE, unless = "#result == null", keyGenerator = SINGULAR_KEY_GENERATOR, cacheManager = WicketRequestCacheManager.WICKET_REQUEST_CACHE_MANAGER)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SingularRequestCache {
    public static final String SINGULAR_CACHE_REQUEST_CACHE = "wicketRequest";
}
