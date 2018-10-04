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

package org.opensingular.app.commons.spring.persistence.database;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.DatabaseObjectNameReplacement;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;

import javax.annotation.Nonnull;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingularDataBasePopulator extends ResourceDatabasePopulator implements Loggable {

    private String                           sqlScriptEncoding;
    private PersistenceConfigurationProvider persistenceConfigurationProvider;

    public SingularDataBasePopulator(@Nonnull PersistenceConfigurationProvider persistenceConfigurationProvider) {
        this.persistenceConfigurationProvider = persistenceConfigurationProvider;
        this.setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        this.setSeparator(";");
    }

    @Override
    public void populate(Connection connection) throws ScriptException {
        super.addScript(new ByteArrayResource(formattedScriptsToExecute(persistenceConfigurationProvider).toString().getBytes(Charset.forName(this.sqlScriptEncoding)),"Singular Schema Export Hibernate DDL + SQL Files"));
        super.populate(connection);
    }

    /**
     * Method to create a StringBuilder with all scripts to execute.
     * The script contains the packages that have to create the entities, and set the replace schema or Catalog.
     *
     * @param persistenceConfigurationProvider The implementation of the interface that contains the packages to Scan, and some replaces that is necessary.
     * @return StringBuilder containg all DML and DDL Scripts.
     */
    private StringBuilder formattedScriptsToExecute(PersistenceConfigurationProvider persistenceConfigurationProvider) {
        StringBuilder scripts = SingularSchemaExport.generateScript(
                persistenceConfigurationProvider.getPackagesToScan(false),
                persistenceConfigurationProvider.getDialect(),
                persistenceConfigurationProvider.getSQLScritps()
        );
        for (DatabaseObjectNameReplacement schemaReplacement : persistenceConfigurationProvider.getSchemaReplacements()) {
            Pattern p     = Pattern.compile(Pattern.quote(schemaReplacement.getOriginalObjectName()));
            Matcher m     = p.matcher(scripts);
            int     start = 0;
            while (m.find(start)) {
                scripts.replace(m.start(), m.end(), schemaReplacement.getObjectNameReplacement());
                start = m.start() + schemaReplacement.getObjectNameReplacement().length();
            }
        }
        return scripts;
    }

    @Override
    public void setSqlScriptEncoding(String sqlScriptEncoding) {
        this.sqlScriptEncoding = sqlScriptEncoding;
        super.setSqlScriptEncoding(sqlScriptEncoding);
    }

}
