package org.opensingular.server.commons.query;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SelectBuilder implements Serializable {

    private List<Expression<?>> columns = new ArrayList<>();

    public SelectBuilder add(@Nonnull Expression<?> exp) {
        columns.add(exp);
        return this;
    }

    public SelectBuilder addCase(@Nonnull Function<CaseBuilder, Expression<?>> builder) {
        columns.add(builder.apply(new CaseBuilder()));
        return this;
    }

    public Expression<?>[] build() {
        return columns.toArray(new Expression<?>[]{});
    }
}
