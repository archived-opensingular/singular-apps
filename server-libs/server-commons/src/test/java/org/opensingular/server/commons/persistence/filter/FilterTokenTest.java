package org.opensingular.server.commons.persistence.filter;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.*;


public class FilterTokenTest {

    @Test
    public void testCreation() throws Exception {
        assertNotNull(new FilterToken("filter"));
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
    public void testGet() throws Exception {
        FilterToken filter = new FilterToken("filter");
        assertThat(filter.get(), Matchers.equalTo("filter"));
    }

    @Test
    public void testGetOnlyNumbersAndLetters() throws Exception {
        FilterToken filter = new FilterToken("###danilo-$123");
        assertThat(filter.getOnlyNumersAndLetters(), Matchers.equalTo("danilo123"));
    }




}