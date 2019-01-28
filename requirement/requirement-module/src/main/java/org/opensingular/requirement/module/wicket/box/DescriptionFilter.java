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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.opensingular.requirement.module.persistence.query.RequirementSearchAliases.DESCRIPTION;

public class DescriptionFilter implements BoxItemDataFilter {

    private Set<String> alias = new HashSet<>();

    @Override
    public void doFilter(List<Map<String, Serializable>> maps) {
        addDescriptionFormatters(maps);
    }

    public DescriptionFilter() {
        addDefaultAlias();
    }

    private void addDefaultAlias() {
        this.alias.add(DESCRIPTION);
    }

    /**
     * This method will include a formatter for description that will split the size
     * to improve the view by user.
     *
     * Note: If the object is not String the formatter will not be included.
     *
     * @param maps The maps of requirements.
     */
    private void addDescriptionFormatters(List<Map<String, Serializable>> maps) {
        maps.forEach(m -> this.alias.forEach(alia -> {
            Object object = m.get(alia);
            if (object instanceof String) {
                String description = ((String) object);
                if(description.length() > 100) {
                    description = ((String) object).substring(0, 97) + "...";
                    m.put(alia, description);
                }
            }
        }));
    }
}
