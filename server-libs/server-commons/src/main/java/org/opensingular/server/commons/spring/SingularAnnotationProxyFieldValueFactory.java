package org.opensingular.server.commons.spring;

import org.apache.wicket.proxy.LazyInitProxyFactory;
import org.apache.wicket.spring.SpringBeanLocator;
import org.apache.wicket.spring.injection.annot.AnnotProxyFieldValueFactory;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Generics;
import org.apache.wicket.util.string.Strings;
import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.ResolvableType;

import javax.inject.Named;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public class SingularAnnotationProxyFieldValueFactory extends AnnotProxyFieldValueFactory {

    private final ConcurrentMap<SpringBeanLocator, Object>                           cache         = Generics.newConcurrentHashMap();
    private final ConcurrentMap<AbstractMap.SimpleEntry<Class<?>, Class<?>>, String> beanNameCache = Generics.newConcurrentHashMap();
    private final boolean wrapInProxies;

    public SingularAnnotationProxyFieldValueFactory(boolean wrapInProxies) {
        super(ApplicationContextProvider::get, wrapInProxies);
        this.wrapInProxies = wrapInProxies;
    }

    public SingularAnnotationProxyFieldValueFactory() {
        super(ApplicationContextProvider::get, true);
        this.wrapInProxies = true;
    }

    @Override
    public Object getFieldValue(final Field field, final Object fieldOwner) {
        return handleReturn(findFieldValue(extractFieldInfo(field, fieldOwner)), field);
    }

    private Object handleReturn(Object value, Field field) {
        if (isOptional(field.getType())) {
            return Optional.ofNullable(value);
        } else {
            return value;
        }
    }

    private boolean isOptional(Class<?> clazz) {
        return Optional.class == clazz;
    }

    private FieldInfo extractFieldInfo(final Field field, final Object fieldOwner) {

        if (supportsField(field)) {
            FieldInfo info = new FieldInfo();
            boolean isOptional = isOptional(field.getType());
            SpringBean annot = field.getAnnotation(SpringBean.class);

            info.fieldOwner = fieldOwner;
            info.field = field;
            if (annot != null) {
                info.name = annot.name();
                info.required = annot.required();
            } else {
                Named named = field.getAnnotation(Named.class);
                info.name = named != null ? named.value() : "";
                info.required = !isOptional;
            }
            if (isOptional) {
                info.type = ResolvableType.forField(field).resolveGeneric(0);
            } else {
                info.type = field.getType();
            }

            info.generic = ResolvableType.forClass(info.type).resolveGeneric(0);

            return info;
        }
        return null;
    }

    private Object findFieldValue(FieldInfo info) {
        if (info != null) {
            String beanName = getBeanName(info);

            SpringBeanLocator locator = new SpringBeanLocator(beanName, info.type, info.field, ApplicationContextProvider::get);

            // only check the cache if the bean is a singleton
            Object cachedValue = cache.get(locator);
            if (cachedValue != null) {
                return cachedValue;
            }

            Object target;
            try {
                // check whether there is a bean with the provided properties
                target = locator.locateProxyTarget();
            } catch (IllegalStateException isx) {
                if (info.required) {
                    throw isx;
                } else {
                    return null;
                }
            }

            if (wrapInProxies) {
                target = LazyInitProxyFactory.createProxy(info.type, locator);
            }

            // only put the proxy into the cache if the bean is a singleton
            if (locator.isSingletonBean()) {
                Object tmpTarget = cache.putIfAbsent(locator, target);
                if (tmpTarget != null) {
                    target = tmpTarget;
                }
            }
            return target;
        }
        return null;
    }

    /**
     * @param field
     * @return bean name
     */
    private String getBeanName(FieldInfo info) {
        String name = info.name;
        if (Strings.isEmpty(info.name)) {
            Class<?> fieldType = info.type;
            AbstractMap.SimpleEntry<Class<?>, Class<?>> keyPair =
                    new AbstractMap.SimpleEntry<Class<?>, Class<?>>(fieldType, info.generic);

            name = beanNameCache.get(fieldType);
            if (name == null) {
                name = getBeanNameOfClass(ApplicationContextProvider.get(), fieldType,
                        info.generic, info.field.getName());

                if (name != null) {

                    String tmpName = beanNameCache.putIfAbsent(keyPair, name);
                    if (tmpName != null) {
                        name = tmpName;
                    }
                }
            }
        }

        return name;
    }

    /**
     * Returns the name of the Bean as registered to Spring. Throws IllegalState exception if none
     * or more than one beans are found.
     *
     * @param ctx       spring application context
     * @param clazz     bean class
     * @param fieldName
     * @return spring name of the bean
     * @throws IllegalStateException
     */
    private String getBeanNameOfClass(final ApplicationContext ctx, final Class<?> clazz,
                                      final Class<?> generic, String fieldName) {
        // get the list of all possible matching beans
        List<String> names = new ArrayList<>(
                Arrays.asList(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(ctx, clazz)));

        // filter out beans that are not candidates for autowiring
        if (ctx instanceof AbstractApplicationContext) {
            Iterator<String> it = names.iterator();
            while (it.hasNext()) {
                final String possibility = it.next();
                BeanDefinition beanDef = getBeanDefinition(
                        ((AbstractApplicationContext) ctx).getBeanFactory(), possibility);
                if (BeanFactoryUtils.isFactoryDereference(possibility) ||
                        possibility.startsWith("scopedTarget.") ||
                        (beanDef != null && !beanDef.isAutowireCandidate())) {
                    it.remove();
                }
            }
        }

        if (names.size() > 1) {
            if (ctx instanceof AbstractApplicationContext) {
                List<String> primaries = new ArrayList<>();
                for (String name : names) {
                    BeanDefinition beanDef = getBeanDefinition(
                            ((AbstractApplicationContext) ctx).getBeanFactory(), name);
                    if (beanDef instanceof AbstractBeanDefinition) {
                        if (beanDef.isPrimary()) {
                            primaries.add(name);
                        }
                    }
                }
                if (primaries.size() == 1) {
                    return primaries.get(0);
                }
            }

            //use field name to find a match
            int nameIndex = names.indexOf(fieldName);

            if (nameIndex > -1) {
                return names.get(nameIndex);
            }

            if (generic != null) {
                return null;
            }

            StringBuilder msg = new StringBuilder();
            msg.append("More than one bean of type [");
            msg.append(clazz.getName());
            msg.append("] found, you have to specify the name of the bean ");
            msg.append("(@SpringBean(name=\"foo\")) or (@Named(\"foo\") if using @javax.inject classes) in order to resolve this conflict. ");
            msg.append("Matched beans: ");
            msg.append(Strings.join(",", names.toArray(new String[names.size()])));
            throw new IllegalStateException(msg.toString());
        } else if (!names.isEmpty()) {
            return names.get(0);
        }

        return null;
    }

    private Class<?> getType(Field field) {
        if (field.getType().isAssignableFrom(Optional.class)) {
            return ResolvableType.forField(field).resolveGeneric(0);
        } else {
            return field.getType();
        }
    }

    private static class FieldInfo {

        Class<?> type;
        String   name;
        boolean  required;
        Object   fieldOwner;
        Class<?> generic;
        Field    field;
    }

}
