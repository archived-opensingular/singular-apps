package org.opensingular.server.commons.persistence.filter;


import javax.annotation.Nonnull;
import java.util.Objects;

public class FilterToken {

    private String token;

    public FilterToken(@Nonnull String token) {
        this.token = token;
    }

    public String get() {
        return token;
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

    public String getOnlyNumersAndLetters() {
        return token.replaceAll("[^\\da-zA-Z]", "");
    }

}
