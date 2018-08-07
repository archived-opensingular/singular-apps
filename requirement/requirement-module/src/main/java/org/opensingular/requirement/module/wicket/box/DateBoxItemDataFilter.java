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

package org.opensingular.requirement.module.wicket.box;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.opensingular.requirement.module.persistence.query.RequirementSearchAliases.PROCESS_BEGIN_DATE;
import static org.opensingular.requirement.module.persistence.query.RequirementSearchAliases.SITUATION_BEGIN_DATE;

public class DateBoxItemDataFilter implements BoxItemDataFilter {

    private String dateFormatter;
    private Set<String> alias = new HashSet<>();

    /**
     * Default constructor.
     * Will use the default configured for date.
     */
    public DateBoxItemDataFilter() {
        this(null);
    }

    @Override
    public void doFilter(List<Map<String, Serializable>> maps) {
        addDateFormatters(maps);
    }

    /**
     * Constructor responsible for configure one or more date filter,
     * and configure the mask.
     *
     * @param dateFormatter String with the date Formatter.
     * @param alias         The alias of the date columns that the formatter must changed.
     */
    public DateBoxItemDataFilter(String dateFormatter, String... alias) {
        this(dateFormatter);
        this.alias.addAll(Arrays.asList(alias));
    }

    /**
     * Constructor responsible for configure the Date Box Filter.
     *
     * @param dateFormatter The String formmater.
     *                      If it's null, it will use a default.
     */
    private DateBoxItemDataFilter(String dateFormatter) {
        this.dateFormatter = Optional.ofNullable(dateFormatter).orElse("dd/MM/yy HH:mm");
        addDefaultAlias();
    }

    private void addDefaultAlias() {
        this.alias.add(PROCESS_BEGIN_DATE);
        this.alias.add(SITUATION_BEGIN_DATE);
    }

    /**
     * This method will include the defaul's formatters for the date element's.
     * The mask will be used in the all alias of date.
     * <p>
     * Note: If the object is not Date the alias will not be included.
     *
     * @param maps The maps of requirements.
     */
    private void addDateFormatters(List<Map<String, Serializable>> maps) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormatter);
        maps.forEach(m -> this.alias.forEach(alia -> {
            Object object = m.get(alia);
            if (object instanceof Date) {
                m.put(alia, fmt.format((Date) object));
            }
        }));
    }
}
