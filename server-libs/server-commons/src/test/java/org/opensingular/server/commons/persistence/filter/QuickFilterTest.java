package org.opensingular.server.commons.persistence.filter;

import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;


public class QuickFilterTest {

    @Test
    public void testGetFilterTokensWithThreeValues() throws Exception {
        QuickFilter quickFilter = new QuickFilter().withFilter("one two three");
        assertThat(quickFilter.getFilterTokens(),
                contains(new FilterToken("one"), new FilterToken("two"), new FilterToken("three")));
    }

    @Test
    public void testGetFilterTokensWithOneValues() throws Exception {
        QuickFilter quickFilter = new QuickFilter().withFilter("one");
        assertThat(quickFilter.getFilterTokens(), contains(new FilterToken("one")));
    }

    @Test
    public void testGetFilterTokensWithoutFilter() throws Exception {
        QuickFilter quickFilter = new QuickFilter();
        assertThat(quickFilter.getFilterTokens(), empty());
    }

}