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

package org.opensingular.requirement.module.config;

import org.opensingular.lib.commons.context.SingularContext;
import org.opensingular.lib.commons.context.SingularSingletonStrategy;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;


public class SingularServerInitializerProvider {

    private PSingularInitializer singularInitializer;

    private SingularServerInitializerProvider() {
    }

    static SingularServerInitializerProvider get() {
        return ((SingularSingletonStrategy) SingularContext.get()).singletonize(SingularServerInitializerProvider.class, SingularServerInitializerProvider::new);
    }

    public PSingularInitializer retrieve() {
        if (singularInitializer == null) {
            List<Class<? extends PSingularInitializer>> configs = findAllInstantiableConfigs();
            if (configs.isEmpty()) {
                throw new SingularServerInitializerProviderException("É obrigatorio implementar a classe " + PSingularInitializer.class);
            }
            if (configs.size() > 1) {
                throw new SingularServerInitializerProviderException("Não é permitido possuir mais de uma implementação de " + PSingularInitializer.class);
            }
            Class<? extends PSingularInitializer> configClass = configs.get(0);
            try {
                singularInitializer = configClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new SingularServerInitializerProviderException("Não foi possivel criar uma nova instancia de " + configClass.getName(), ex);
            }
        }
        return singularInitializer;
    }

    private List<Class<? extends PSingularInitializer>> findAllInstantiableConfigs() {
        return SingularClassPathScanner.get()
                        .findSubclassesOf(PSingularInitializer.class)
                        .stream()
                        .filter(config -> !(Modifier.isAbstract(config.getModifiers()) || config.isInterface() || config.isAnonymousClass()))
                        .collect(Collectors.toList());
    }

    private static class SingularServerInitializerProviderException extends RuntimeException {
        public SingularServerInitializerProviderException(String s) {
            super(s);
        }

        public SingularServerInitializerProviderException(String s, Throwable throwable) {
            super(s, throwable);
        }
    }

}