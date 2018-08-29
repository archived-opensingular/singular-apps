/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
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

package org.opensingular.requirement.commons.spring;


import java.util.Set;
import javax.inject.Named;

import com.google.common.collect.Sets;
import net.vidageek.mirror.dsl.Mirror;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.opensingular.lib.commons.context.ServiceRegistryLocator;
import org.opensingular.lib.commons.context.SingularContextSetup;
import org.opensingular.lib.commons.context.spring.SpringServiceRegistry;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.spring.test.ApplicationContextMock;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.requirement.module.spring.SingularDefaultBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

public class SingularServerSpringMockitoTestConfig implements Loggable {

    private TestScan testClasspathScanner = new TestScan();
    private Object myTestClass;

    public SingularServerSpringMockitoTestConfig(Object myTestClass) {
        if (myTestClass.getClass().isAnnotationPresent(RunWith.class)) {
            Class<? extends Runner> runnerClass = myTestClass.getClass().getDeclaredAnnotation(RunWith.class).value();
            if (runnerClass.isAssignableFrom(MockitoJUnitRunner.class)) {
                this.myTestClass = myTestClass;
                return;
            }
        }
        throw new IllegalArgumentException(this.getClass().getName() + " somente funciona com classes de teste cujo runner seja do tipo: " + MockitoJUnitRunner.class.getName());
    }

    public void resetAndReconfigure(boolean debug) {
        SingularContextSetup.reset();
        ApplicationContextMock applicationContext = new ApplicationContextMock();
        ServiceRegistryLocator.setup(new SpringServiceRegistry());
        new ApplicationContextProvider().setApplicationContext(applicationContext);
        registerBeanFactories(applicationContext);
        registerAnnotated(applicationContext, Named.class);
        registerAnnotated(applicationContext, Service.class);
        registerAnnotated(applicationContext, Component.class);
        registerAnnotated(applicationContext, Repository.class);
        registerMockitoTestClassMocksAndSpies(applicationContext);
        getLogger().info("Contexto configurado com os beans: ");
        if (debug) {
            applicationContext.listAllBeans().forEach(
                    b -> getLogger().info(b)
            );
        }
    }

    public void resetAndReconfigure() {
        resetAndReconfigure(false);
    }

    private void registerMockitoTestClassMocksAndSpies(ApplicationContextMock applicationContext) {
        new Mirror().on(myTestClass.getClass()).reflectAll().fields().matching(f -> f.isAnnotationPresent(Mock.class) || f.isAnnotationPresent(Spy.class)).forEach(
                f -> {
                    try {
                        applicationContext.putOrReplaceBean(f.get(myTestClass));
                    } catch (IllegalAccessException e) {
                        getLogger().trace(e.getMessage(), e);
                    }
                }
        );
    }

    private void registerAnnotated(ApplicationContextMock applicationContext, Class annotation) {
        testClasspathScanner.findClassesAnnotatedWith(annotation).forEach(f -> applicationContext.putOrReplaceBean(Mockito.mock(f)));
    }

    private void registerBeanFactories(ApplicationContextMock applicationContext) {
        Set<Class<?>> beanFactoriesClasses = Sets.union(testClasspathScanner.findSubclassesOf(SingularDefaultBeanFactory.class),
                testClasspathScanner.findClassesAnnotatedWith(Configuration.class));
        beanFactoriesClasses.forEach(c -> registerMockBean(applicationContext, c));
    }

    private void registerMockBean(ApplicationContextMock applicationContext, Class<?> targetClass) {
        new Mirror().on(targetClass).reflectAll().methods().matching(element -> element.isAnnotationPresent(Bean.class)).forEach(m -> {
            if (m.getParameterCount() == 0) {
                try {
                    applicationContext.putOrReplaceBean(m.invoke(Mockito.spy(targetClass)));
                } catch (Exception e) {
                    getLogger().trace(e.getMessage(), e);
                }
            }
        });
    }

    private static class TestScan extends SingularClassPathScanner {
        public TestScan() {
            super();
        }
    }
}
