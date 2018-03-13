package org.opensingular.studio.core.definition;

import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;

/**
 * @param <T> the query filter instance
 */
public class StudioQueryContext<T extends SInstance> {

    private Long      first;
    private Long      count;
    private IModel<T> filter;
    private String    sortProperty;
    private Boolean   asc;

    public Long getFirst() {
        return first;
    }

    public StudioQueryContext<T> setFirst(Long first) {
        this.first = first;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public StudioQueryContext<T> setCount(Long count) {
        this.count = count;
        return this;
    }

    public IModel<T> getFilter() {
        return filter;
    }

    public StudioQueryContext<T> setFilter(IModel<T> filter) {
        this.filter = filter;
        return this;
    }

    public String getSortProperty() {
        return sortProperty;
    }

    public StudioQueryContext<T> setSortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
        return this;
    }

    public Boolean getAsc() {
        return asc;
    }

    public StudioQueryContext<T> setAsc(Boolean asc) {
        this.asc = asc;
        return this;
    }
}
