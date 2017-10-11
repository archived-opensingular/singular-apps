package org.opensingular.server.commons.persistence.filter;


import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FilterToken {

    private String token;
    private boolean exact;

    public FilterToken(@Nonnull String token) {
        this(token, false);
    }

    public FilterToken(@Nonnull String token, boolean exact) {
        this.token = token;
        this.exact = exact;
    }

    public List<String> getAllPossibleMatches() {
        if (exact) {
            return Collections.singletonList(token);
        } else {
            List<String> matches = new ArrayList<>();
            matches.add(get());
            matches.add(getOnlyNumbersAndLetters());
            return matches;
        }
    }

    /**
     * Returns raw token value without like wildcards characters.
     * @return
     */
    public String getRaw(){
        return token;
    }

    private String anywhereOrExact(String str) {
        return exact ? str : "%" + str + "%";
    }

    public String get() {
        return anywhereOrExact(token);
    }

    public String getOnlyNumbersAndLetters() {
        return anywhereOrExact(token.replaceAll("[^\\da-zA-Z]", ""));
    }

    public boolean isExact() {
        return exact;
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
