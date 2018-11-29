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

package org.opensingular.requirement.module.persistence.filter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

public class FilterTokenFactory {

    public static final String QUOTATION_MARK_FIND_REGEX = "\"[^\"].*?\"";

    private final String rawFilter;

    private String dynamicFilter;
    private List<FilterToken> tokens;

    public FilterTokenFactory(@Nonnull String rawFilter) {
        this.rawFilter = rawFilter;
    }

    public List<FilterToken> make() {
        dynamicFilter = rawFilter;
        tokens = new ArrayList<>();
        addExactTokens();
        addAnywhereTokens();
        return tokens;
    }

    private void addAnywhereTokens() {
        Arrays.stream(dynamicFilter.split(" ")).filter(StringUtils::isNotBlank).map(FilterToken::new).forEach(tokens::add);
    }

    private void addExactTokens() {
        Matcher matcher = Pattern.compile(QUOTATION_MARK_FIND_REGEX).matcher(rawFilter);
        while (matcher.find()) {
            String matched = rawFilter.substring(matcher.start(), matcher.end());
            tokens.add(new FilterToken(matched.replace("\"", "")));
            dynamicFilter = dynamicFilter.replaceFirst(matched, "");
        }
    }

}
