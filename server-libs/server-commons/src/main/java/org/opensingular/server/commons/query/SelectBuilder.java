package org.opensingular.server.commons.query;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DslExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SelectBuilder {

    private List<Expression<?>> columns = new ArrayList<>();

    public <T extends Expression<X>, X> SelectBuilder add(Expression<?> exp) {
        columns.add(exp);
        return this;
    }

    public SelectBuilder addCase(Function<CaseBuilder, DslExpression<?>> builder) {
        columns.add(builder.apply(new CaseBuilder()));
        return this;
    }

    public Expression<?>[] build() {
        return columns.toArray(new Expression<?>[]{});
    }
}
