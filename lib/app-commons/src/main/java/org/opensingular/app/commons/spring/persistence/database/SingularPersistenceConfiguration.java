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

/**
 * Interface responsible for configuration of the Persistence.
 *
 * @see PersistenceConfigurationProvider
 */
public interface SingularPersistenceConfiguration {

    /**
     * Configure hibernates packages to scan in addition to the singular platform packages, ie: singular entities packages
     * is already configured by the platform.
     *
     * @param packagesConfig A configuration class that contains a pre-populated list with singular default packages,
     *                                  cleaning or removing pre-populated data in this list would led to platform malfunction.
     */
    void configureHibernatePackagesToScan(PackageScanConfiguration packagesConfig);

    /**
     * Method responsible to configure the list of scripts files that will be execute after the creation of Entities by Hibernate.
     * The DML scripts should be there.
     *
     * @param scripts The list of scripts paths.
     */
    void configureInitSQLScripts(List<String> scripts);

    /**
     * The properties of Hibernate.
     *
     * @param properties The properties of Hibernate.
     */
    default void configureHibernateProperties(Properties properties) {
    }

    /**
     * Method to configure the Schema or Catalog Replacement.
     * This method use the param to configure a list contains the placeholder and the new name of a schema
     * or something that want to replace on Prepare Statment executement.
     *
     * @param replacements The list containing the Object with the replacements name.
     */
    default void configureQueryReplacements(List<DatabaseObjectNameReplacement> replacements) {
    }

    /**
     * The Dialect used in Procjet.
     *
     * @return the Dialect used in Procjet
     */
    Class<? extends Dialect> getHibernateDialect();

    /**
     * A specific configuration of the Actor Table for the project.
     *
     * @return The Script path of the actor, use the Singular by default.
     */
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

    /**
     * Method that returns a implmentation of interface SingularDataBaseSuport that should contains somo scripts of specific Dialect,
     * for example the creation of function, and the actor table script.
     *
     * <p>By default this method use the Dialect configuration and try to find a implementation in the SingularDataBaseEnum,
     * if don't find it'll throw a exception, and the responsible of the project have to implement this method.</p>
     *
     * @return the implementation of SingularDataBaseSuport.
     */
    default SingularDataBaseSuport getDatabaseSupport() {
        return SingularDataBaseEnum.getForDialect(getHibernateDialect());
    }

}
