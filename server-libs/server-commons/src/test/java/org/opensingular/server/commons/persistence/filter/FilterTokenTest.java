package org.opensingular.server.commons.persistence.filter;

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

}