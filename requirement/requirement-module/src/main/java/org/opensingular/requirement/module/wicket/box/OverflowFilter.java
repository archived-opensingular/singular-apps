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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.opensingular.requirement.module.persistence.query.RequirementSearchAliases.DESCRIPTION;

/**
 * This filter has the responsibility to hide the Overflow description of the column.
 * The description of the column will be changed to have 3 period(...) and a action to expand the description.
 * <p>
 * Note: Only String columns can be used with this filter.
 */
public class OverflowFilter implements BoxItemDataFilter {

    private Set<String> alias = new HashSet<>();
    private int sizeOverflowCharacters = 100;

    @Override
    public void doFilter(List<Map<String, Serializable>> maps) {
        addDescriptionFormatters(maps);
    }

    public OverflowFilter() {
        addDefaultAlias();
    }

    /**
     * @param sizeOverflowCharacters The max size of the characters until hide the text.
     * @param alias                  The alias of the columns two include this rule.
     */
    public OverflowFilter(int sizeOverflowCharacters, String... alias) {
        this.sizeOverflowCharacters = sizeOverflowCharacters;
        if (alias == null) {
            this.alias.add(DESCRIPTION);
        } else {
            this.alias = Stream.of(alias)
                    .collect(Collectors.toSet());
        }
    }

    private void addDefaultAlias() {
        this.alias.add(DESCRIPTION);
    }

    /**
     * This method will include a formatter for description that will split the size to improve the view by user.
     * Will add a function to show all description when clicked.
     * <p>
     * Note: If the object is not String the formatter will not be included.
     *
     * @param maps The maps of requirements.
     */
    private void addDescriptionFormatters(List<Map<String, Serializable>> maps) {
        maps.forEach(m -> this.alias.forEach(alia -> {
            Object object = m.get(alia);
            if (object instanceof String) {
                String description = ((String) object);
                if (description.length() > sizeOverflowCharacters) {
                    String javascriptWithRestOfDescription = createScriptShowDescription(description);
                    description = ((String) object).substring(0, sizeOverflowCharacters - 3) + "<a onclick=\" " + javascriptWithRestOfDescription + " \">...</a>";
                    m.put(alia, description);
                }
            }
        }));
    }

    private String createScriptShowDescription(String description) {
        String restOfDescription = description.substring(sizeOverflowCharacters - 3, description.length());
        return "javascript:"
                + " var _this = $(this); "
                + " var parent = _this.parent(); "
                + " _this.remove(); "
                + " parent.text(parent.text()+'" + restOfDescription + "');";
    }
}
