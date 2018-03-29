/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.commons.persistence.query;

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
