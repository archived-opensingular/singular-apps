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

package org.opensingular.requirement.module.config;

import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.ObjectUtils;
import org.opensingular.requirement.module.exception.SingularRequirementException;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Classe que faz o looup da implementação de {@link SingularInitializer}
 *
 * @see SingularInitializer
 * @see AbstractSingularInitializer
 * @see SingularWebAppInitializer
 */
class SingularInitializerProvider {
    /**
     * Busca no classpath a implementação de {@link SingularInitializer}, só é permitido uma implementação por classpath
     *
     * @return a implementação encontrada
     */
    static SingularInitializer retrieve() {
        List<Class<? extends SingularInitializer>> configs = findAllInstantiableConfigs();
        if (configs.isEmpty()) {
            throw new SingularServerInitializerProviderException("É obrigatorio implementar a classe " + SingularInitializer.class);
        }
        if (configs.size() > 1) {
            throw new SingularServerInitializerProviderException("Não é permitido possuir mais de uma implementação de " + SingularInitializer.class);
        }
        Class<? extends SingularInitializer> configClass = configs.get(0);
        return ObjectUtils.newInstance(configClass);
    }

    private static List<Class<? extends SingularInitializer>> findAllInstantiableConfigs() {
        return SingularClassPathScanner.get()
                .findSubclassesOf(SingularInitializer.class)
                .stream()
                .filter(config -> !(Modifier.isAbstract(config.getModifiers()) || config.isInterface() || config.isAnonymousClass()))
                .collect(Collectors.toList());
    }

    private static class SingularServerInitializerProviderException extends SingularRequirementException {
        SingularServerInitializerProviderException(String s) {
            super(s);
        }

        SingularServerInitializerProviderException(String s, Throwable throwable) {
            super(s, throwable);
        }
    }
}