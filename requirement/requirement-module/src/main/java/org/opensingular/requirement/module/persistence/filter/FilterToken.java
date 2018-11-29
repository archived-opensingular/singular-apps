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


import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterToken {

    private String token;

    public FilterToken(@Nonnull String token) {
        this.token = token;
    }

    public List<String> getAllPossibleMatches() {

        List<String> matches = new ArrayList<>();
        matches.add(get());
        matches.add(getOnlyNumbersAndLetters());
        return matches;

    }

    /**
     * Returns raw token value without like wildcards characters.
     *
     * @return
     */
    public String getRaw() {
        return token;
    }

    private String anywhereOrExact(String str) {
        return "%" + str + "%";
    }

    public String get() {
        return anywhereOrExact(token);
    }

    public String getOnlyNumbersAndLetters() {
        return anywhereOrExact(token.replaceAll("[^\\da-zA-Z]", ""));
    }

    @Override
    public String toString() {
        return "FilterToken{" +
                "token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterToken that = (FilterToken) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

}
