package org.opensingular.server.commons.persistence.filter;


import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            tokens.add(new FilterToken(matched.replace("\"", ""), true));
            dynamicFilter = dynamicFilter.replaceFirst(matched, "");
        }
    }

}
