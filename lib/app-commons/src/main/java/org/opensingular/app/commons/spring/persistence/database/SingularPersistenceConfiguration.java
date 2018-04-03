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

package org.opensingular.app.commons.spring.persistence.database;

import org.hibernate.dialect.Dialect;
import org.opensingular.lib.support.persistence.DatabaseObjectNameReplacement;
import org.opensingular.lib.support.persistence.util.SqlUtil;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public interface SingularPersistenceConfiguration {

    void configureHibernatePackagesToScan(List<String> packagesToScan);

    void configureInitSQLScripts(List<String> scripts);

    Class<? extends Dialect> getHibernateDialect();

    default String getActorTableScript() {
        return SingularDataBaseEnum.getForDialect(getHibernateDialect()).getDefaultActorScript();
    }

    default void configureHibernateProperties(Properties properties) {
    }

    default DataSource getEmbeddedDataSource() {
        return new DefaultH2DataSource();
    }

    default DataSource getNonEmbeddedDataSource() {
        return new DefaultJNDIDataSource();
    }

    default SingularDataBaseSuport getDatabaseSupport() {
        return SingularDataBaseEnum.getForDialect(getHibernateDialect());
    }

    default List<DatabaseObjectNameReplacement> getSchemaReplacements() {
        return new ArrayList<>();
    }
}
