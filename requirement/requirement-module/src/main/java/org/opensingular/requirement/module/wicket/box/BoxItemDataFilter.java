package org.opensingular.requirement.module.wicket.box;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Interface used to create the filters for the requeriment box.
 * Note:This interface work's like Consumer.
 *
 * @see DateBoxItemDataFilter#addDateFormatters
 */
@FunctionalInterface
public interface BoxItemDataFilter {
    void doFilter(List<Map<String, Serializable>> map);
}