package org.opensingular.server.commons.persistence.filter;


import java.util.Objects;

public class FilterToken {

    private String token;

    public FilterToken(String token) {
        this.token = token;
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
