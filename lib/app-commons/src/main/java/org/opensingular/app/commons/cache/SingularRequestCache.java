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

/**
 * Caches data  per wicket http REQUEST as long as the REQUEST is active.
 * if there is no wicket REQUEST it defaults to @SingularCache to cache and lookup
 */
@Cacheable(cacheNames = SingularRequestCache.SINGULAR_CACHE_REQUEST_CACHE
        , unless = "#result == null"
        , keyGenerator = SingularRequestKeyGenerator.SINGULAR_REQUEST_KEY_GENERATOR)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SingularRequestCache {
    String SINGULAR_CACHE_REQUEST_CACHE = "httpRequestCache";
}