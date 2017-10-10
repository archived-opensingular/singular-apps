package org.opensingular.server.commons.config;

import org.opensingular.lib.commons.context.SingularContext;
import org.opensingular.lib.commons.context.SingularSingletonStrategy;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.server.p.commons.config.PSingularInitializer;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;


public class SingularServerInitializerProvider {

    private PSingularInitializer singularInitializer;

    private SingularServerInitializerProvider() {
    }

    public static SingularServerInitializerProvider get() {
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