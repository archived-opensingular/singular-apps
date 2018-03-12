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

package org.opensingular.server.commons.persistence.filter;

import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


public class FilterTokenFactoryTest {

    @Test
    public void testGetFilterTokensWithThreeValues() throws Exception {
        FilterTokenFactory filterTokenFactory = new FilterTokenFactory("one two three");
        assertThat(filterTokenFactory.make(),
                containsInAnyOrder(new FilterToken("one"), new FilterToken("two"), new FilterToken("three")));
    }

    @Test
    public void testGetFilterTokensWithOneValues() throws Exception {
        FilterTokenFactory filterTokenFactory = new FilterTokenFactory("one");
        assertThat(filterTokenFactory.make(), containsInAnyOrder(new FilterToken("one")));
    }


    @Test
    public void testGetFilterTokensWithQuotationMarks() throws Exception {
        FilterTokenFactory filterTokenFactory = new FilterTokenFactory("\"one\"");
        assertThat(filterTokenFactory.make().get(0), equalTo(new FilterToken("one")));
    }

    @Test
    public void testGetFilterTokensWithQuotationMarksAndSpaces() throws Exception {
        FilterTokenFactory filterTokenFactory = new FilterTokenFactory("\"one\" two \"three four\"");
        assertThat(filterTokenFactory.make(), containsInAnyOrder(new FilterToken("one"), new FilterToken("two"), new FilterToken("three four")));
    }

    @Test
    public void testGetFilterTokensWithSingleQuotationMark() throws Exception {
        FilterTokenFactory filterTokenFactory = new FilterTokenFactory("\" one");
        assertThat(filterTokenFactory.make(), containsInAnyOrder(new FilterToken("\""), new FilterToken("one")));
    }

    @Test
    public void testGetFilterTokensWithSingleQuotationMarkAndQuotationMarks() throws Exception {
        FilterTokenFactory filterTokenFactory = new FilterTokenFactory("\"one\" \"");
        assertThat(filterTokenFactory.make(), containsInAnyOrder(new FilterToken("\""), new FilterToken("one")));
    }

}