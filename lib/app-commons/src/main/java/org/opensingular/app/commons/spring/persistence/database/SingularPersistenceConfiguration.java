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

import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

import org.hibernate.dialect.Dialect;
import org.opensingular.lib.support.persistence.DatabaseObjectNameReplacement;

public interface SingularPersistenceConfiguration {

    /**
     * Configure hibernates packages to scan in addition to the singular platform packages, ie: singular entities packages
     * is already configured by the platform.
     * @param packagesToScan
     *  A pre-populated list with singular default packages, cleaning or removing pre-populated data in this list
     *  would led to platform malfunction.
     */
    void configureHibernatePackagesToScan(List<String> packagesToScan);

    void configureInitSQLScripts(List<String> scripts);

    default void configureHibernateProperties(Properties properties) {
    }

    default void configureQueryReplacements(List<DatabaseObjectNameReplacement> replacements) {
    }

    Class<? extends Dialect> getHibernateDialect();

    default String getActorTableScript() {
        return getDatabaseSupport().getDefaultActorScript();
    }

    /**
     * Must return a Pooled data source backed by H2 engine
     * Defaults to {@link DefaultH2DataSource} pooled by {@link com.zaxxer.hikari.HikariDataSource}
     */
    default DefaultH2DataSource getEmbeddedDataSource() {
        return new DefaultH2DataSource();
    }

    /**
     * Must return a Pooled data source.
     * Defaults to {@link DefaultJNDIDataSource} pooled by {@link com.zaxxer.hikari.HikariDataSource}
     */
    default DataSource getNonEmbeddedDataSource() {
        return new DefaultJNDIDataSource();
    }

    default SingularDataBaseSuport getDatabaseSupport() {
        return SingularDataBaseEnum.getForDialect(getHibernateDialect());
    }

}
