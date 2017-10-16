package org.opensingular.server.commons.spring.security;


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
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.server.commons.spring.SingularDefaultBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.inject.Named;
import java.util.Set;

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
        for (Class<?> c : beanFactoriesClasses) {
            new Mirror().on(c).reflectAll().methods().matching(element -> element.isAnnotationPresent(Bean.class)).forEach(m -> {
                if (m.getParameterCount() == 0) {
                    try {
                        applicationContext.putOrReplaceBean(m.invoke(Mockito.spy(c)));
                    } catch (Exception e) {
                        getLogger().trace(e.getMessage(), e);
                    }
                }

            });
        }

    }

    private static class TestScan extends SingularClassPathScanner {
        public TestScan() {
            super();
        }
    }
}
