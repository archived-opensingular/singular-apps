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

package org.opensingular.requirement.commons.test;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.opensingular.app.commons.spring.persistence.database.DefaultH2DataSource;
import org.opensingular.app.commons.spring.persistence.database.SingularPersistenceConfiguration;

import javax.sql.DataSource;
import java.util.List;

public class CommonsPersistenceConfiguration implements SingularPersistenceConfiguration {
    @Override
    public void configureHibernatePackagesToScan(List<String> packagesToScan) {

    }

    @Override
    public void configureInitSQLScripts(List<String> scripts) {
    }

    @Override
    public DataSource getEmbeddedDataSource() {
        return new DefaultH2DataSource().setMode("ORACLE");
    }

    @Override
    public Class<? extends Dialect> getHibernateDialect() {
        return Oracle10gDialect.class;
    }

}
