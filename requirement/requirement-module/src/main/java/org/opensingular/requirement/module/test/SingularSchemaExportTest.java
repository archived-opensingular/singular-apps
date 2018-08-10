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

package org.opensingular.requirement.module.test;

import org.junit.Test;
import org.opensingular.app.commons.spring.persistence.database.PersistenceConfigurationProvider;
import org.opensingular.app.commons.spring.persistence.database.SingularSchemaExport;

public abstract class SingularSchemaExportTest {

    static final String SCRIPT_FILE = "/src/main/resources/exportScript.sql";

    @Test
    public abstract void exportScriptToFile();

    protected void generateScript() {
        generateScript(System.getProperty("user.dir") + SCRIPT_FILE);
    }

    protected void generateScript(String scriptFilePath) {
        PersistenceConfigurationProvider persistenceConfigurationProvider = getPersistenceConfiguration();
        SingularSchemaExport.generateScriptToFile(
                persistenceConfigurationProvider.getPackagesToScan(false),
                persistenceConfigurationProvider.getDialect(),
                persistenceConfigurationProvider.getSQLScritps(),
                scriptFilePath
        );
    }

    protected PersistenceConfigurationProvider getPersistenceConfiguration() {
        return new PersistenceConfigurationProvider();
    }

}
