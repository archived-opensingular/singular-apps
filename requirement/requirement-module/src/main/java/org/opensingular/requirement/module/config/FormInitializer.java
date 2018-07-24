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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;

import org.opensingular.form.SType;
import org.opensingular.form.spring.SpringSDocumentFactory;
import org.opensingular.form.spring.SpringTypeLoader;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.requirement.module.form.SingularServerDocumentFactory;
import org.opensingular.requirement.module.form.SingularServerFormConfigFactory;
import org.opensingular.requirement.module.form.SingularServerSpringTypeLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class FormInitializer {

    public static final Logger logger        = LoggerFactory.getLogger(FormInitializer.class);
    static final        String SINGULAR_FORM = "[SINGULAR][FORM] {}";
    private List<Class<? extends SType<?>>> types = null;

    protected Class<? extends SpringSDocumentFactory> documentFactory() {
        return SingularServerDocumentFactory.class;
    }

    protected Class<? extends SpringTypeLoader> typeLoader() {
        return SingularServerSpringTypeLoader.class;
    }

    protected Class<?> formConfigFactory() {
        return SingularServerFormConfigFactory.class;
    }

    @SuppressWarnings("unchecked")
    public synchronized List<Class<? extends SType<?>>> getTypes() {
        if (types == null){
            types = new ArrayList<>();
            SingularClassPathScanner.get()
                    .findSubclassesOf(SType.class)
                    .stream()
                    .filter(f -> !Modifier.isAbstract(f.getModifiers()))
                    .forEach(f -> types.add((Class<? extends SType<?>>) f));
        }
        return types;
    }

    public void init(ServletContext ctx, AnnotationConfigWebApplicationContext applicationContext) {
        Class<?> documentFactory = documentFactory();
        if (documentFactory != null) {
            applicationContext.register(documentFactory);
        } else {
            logger.info(SINGULAR_FORM, " Null Form Document Factory, skipping Form Document Factory configuration. ");
        }
        Class<?> typeLoader = typeLoader();
        if (typeLoader != null) {
            applicationContext.register(typeLoader);
        } else {
            logger.info(SINGULAR_FORM, " Null Form Type Loader, skipping Form Type Loader configuration. ");
        }
        Class<?> formConfigFactory = formConfigFactory();
        if (formConfigFactory != null) {
            applicationContext.register(formConfigFactory);
        } else {
            logger.info(SINGULAR_FORM, " Null Form Config Factory, skipping Form Config Factory configuration. ");
        }
    }


}
