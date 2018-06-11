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

import org.opensingular.lib.commons.util.Loggable;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class H2DropAllObjectsPopulator implements DatabasePopulator, Loggable {

    private final String initScript;

    public H2DropAllObjectsPopulator(DataSource dataSource) {
        if (dataSource instanceof DefaultH2DataSource) {
            this.initScript = ((DefaultH2DataSource) dataSource).getInitScript();
        } else {
            this.initScript = "";
        }
    }

    @Override
    public void populate(Connection connection) throws SQLException, ScriptException {
        try (PreparedStatement drop = connection.prepareStatement("DROP ALL OBJECTS");
             PreparedStatement ps = connection.prepareStatement(initScript)) {
            getLogger().warn("DROPPING EMBBEDED H2 DATABASE, SQL: DROP ALL OBJECTS");
            drop.execute();
            getLogger().warn("REEXECUTING EMBBEDED H2 DATABASE INIT SCRIPTS");
            ps.execute();
        }
    }
}
