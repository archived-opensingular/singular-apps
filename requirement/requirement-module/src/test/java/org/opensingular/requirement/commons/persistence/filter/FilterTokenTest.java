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

package org.opensingular.requirement.commons.persistence.filter;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.opensingular.requirement.module.persistence.filter.FilterToken;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;


public class FilterTokenTest {

    @Test
    public void testCreation() throws Exception {
        FilterToken filterToken = new FilterToken("filter");
        assertNotNull(filterToken);
    }

    @Test
    public void testCreationExact() throws Exception {
        FilterToken filterToken = new FilterToken("filter");
    }

    @Test
    public void testTokensWithSameFilter() throws Exception {
        assertEquals(new FilterToken("one"), new FilterToken("one"));
    }

    @Test
    public void testIfTokensWithDifferentFilter() throws Exception {
        assertNotEquals(new FilterToken("one"), new FilterToken("two"));
    }

    @Test
    public void testGetAnywhere() throws Exception {
        FilterToken filter = new FilterToken("filter");
        assertThat(filter.get(), Matchers.equalTo("%filter%"));
    }

    @Test
    public void testGetOnlyNumbersAndLettersAnywhere() throws Exception {
        FilterToken filter = new FilterToken("###danilo-$123");
        assertThat(filter.getOnlyNumbersAndLetters(), Matchers.equalTo("%danilo123%"));
    }


    @Test
    public void testGetAllPossibleMatches() throws Exception {
        FilterToken filterToken = new FilterToken("teste");
        assertThat(filterToken.getAllPossibleMatches(), containsInAnyOrder(filterToken.get()));
    }

}