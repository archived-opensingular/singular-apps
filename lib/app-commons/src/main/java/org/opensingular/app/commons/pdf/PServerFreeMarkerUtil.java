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

package org.opensingular.app.commons.pdf;


import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.commons.lang3.StringUtils;
import org.opensingular.form.SInstance;
import org.opensingular.form.SingularFormException;
import org.opensingular.form.internal.freemarker.FormObjectWrapper;
import org.opensingular.lib.commons.context.SingularContext;
import org.opensingular.lib.commons.context.SingularSingletonStrategy;
import org.opensingular.lib.commons.util.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static freemarker.template.Configuration.VERSION_2_3_22;

public class PServerFreeMarkerUtil implements Loggable {

    private static final Version VERSION = VERSION_2_3_22;

    private final Configuration cfg = new Configuration(VERSION);
    private final Logger LOGGER = LoggerFactory.getLogger(PServerFreeMarkerUtil.class);
    private boolean escapeStr = true;

    public void buildConfiguration(Consumer<Configuration> configurationConsumer) {
        cfg.setTemplateLoader(new ClassTemplateLoader(Thread.currentThread().getContextClassLoader(), "templates"));//NOSONAR
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        if (configurationConsumer != null) {
            configurationConsumer.accept(cfg);
        }
    }

    private PServerFreeMarkerUtil() {
        buildConfiguration(null);
    }

    private PServerFreeMarkerUtil(Consumer<Configuration> configurationConsumer) {
        buildConfiguration(configurationConsumer);
    }

    public static PServerFreeMarkerUtil getInstance() {
        return ((SingularSingletonStrategy) SingularContext.get()).singletonize(PServerFreeMarkerUtil.class, PServerFreeMarkerUtil::new);
    }

    public static PServerFreeMarkerUtil getNewInstance(Consumer<Configuration> configurationConsumer) {
        return new PServerFreeMarkerUtil(configurationConsumer);
    }

    public static String mergeWithFreemarker(String templateName, Map<String, Object> model) {
        return mergeWithFreemarker(templateName, model, true);
    }

    public static String mergeWithFreemarker(String templateName, Map<String, Object> model, boolean escapeStr) {
        PServerFreeMarkerUtil i = getInstance();
        i.setEscapeStr(escapeStr);
        return i.doMergeWithFreemarker(templateName, model);
    }

    public String doMergeWithFreemarker(String templateName, Map<String, Object> model) {
        if (model == null || templateName == null) {
            return StringUtils.EMPTY;
        }
        try {
            return merge(cfg.getTemplate(templateName), model);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new SingularFormException("Não foi possivel fazer o merge do template " + templateName, ex);
        }
    }

    public String doMergeWithStringTemplate(String templateCode, Map<String, Object> model) {
        Objects.requireNonNull(templateCode, "Template não pode ser null");
        Objects.requireNonNull(model, "Model Não pode ser null");
        try {
            return merge(new Template(UUID.randomUUID().toString(), templateCode, cfg), model);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new SingularFormException("Não foi possivel fazer o merge com o template infomado", ex);
        }
    }

    private String merge(Template template, Map<String, Object> model) throws IOException, TemplateException {
        final StringWriter sw = new StringWriter();

        Predicate<Map.Entry<String, Object>> isInstance = (entry) -> Optional.ofNullable(entry)
                .map(Map.Entry::getValue).map(Object::getClass).map(SInstance.class::isAssignableFrom)
                .orElse(Boolean.FALSE);

        Map<String, Object> instances = model.entrySet()
                .stream().filter(isInstance)
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);

        Map<String, Object> pojos = model.entrySet()
                .stream().filter(isInstance.negate())
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);

        final Map map = new ObjectMapper().convertValue(pojos, Map.class);

        map.putAll(instances);
        template.process(escapeStr? encode(map): map, sw, new FormObjectWrapper(false));
        return sw.toString();
    }

    private Object encode(Object o) {
        final Map m = new HashMap();
        if (o instanceof Map) {
            ((Map) o).forEach((k, v) -> m.put(k, encode(v)));
        } else if (o instanceof String) {
            return HtmlUtils.htmlEscape((String) o);
        } else if (o instanceof Collection) {
            List<Object> list = new ArrayList<>();
            ((Collection) o).forEach(x -> list.add(encode(x)));
            return list;
        } else {
            return o;
        }
        return m;
    }

    public void setEscapeStr(boolean escapeStr) {
        this.escapeStr = escapeStr;
    }


}