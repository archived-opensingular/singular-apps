/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.studio.core.panel;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.studio.core.definition.StudioQueryContext;
import org.opensingular.studio.core.definition.StudioTableDataProvider;

import java.util.Iterator;
import java.util.Optional;

import static org.opensingular.lib.wicket.util.util.Shortcuts.$m;

public class StudioDataProviderAdapter extends SortableDataProvider<SInstance, String> {

    private StudioTableDataProvider       studioTableDataProvider;
    private IFunction<FormKey, SInstance> instanceLoader;
    private IModel                        filter;

    public StudioDataProviderAdapter(StudioTableDataProvider studioTableDataProvider,
                                     IFunction<FormKey, SInstance> instanceLoader,
                                     IModel filter) {
        this.studioTableDataProvider = studioTableDataProvider;
        this.instanceLoader = instanceLoader;
        this.filter = filter;
    }

    @Override
    public Iterator<? extends SInstance> iterator(long first, long count) {
        return studioTableDataProvider.iterator(new StudioQueryContext<>().setFirst(first)
                .setCount(count)
                .setFilter(filter)
                .setSortProperty(getSortOptional().map(SortParam::getProperty).orElse(null))
                .setAsc(getSortOptional().map(SortParam::isAscending).orElse(null)));
    }

    private Optional<SortParam<String>> getSortOptional() {
        return Optional.ofNullable(getSort());
    }

    @Override
    public long size() {
        return studioTableDataProvider
                .size(new StudioQueryContext<>().setFilter(filter));
    }

    @Override
    public IModel<SInstance> model(SInstance object) {
        final FormKey key = FormKey.from(object);
        return $m.loadable(object, () -> instanceLoader.apply(key));
    }
}
