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

package org.opensingular.requirement.commons.spring.database;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class H2ResourceDatabasePopulator extends AbstractResourceDatabasePopulator {

    @Value("classpath:db/ddl/h2/create-function.sql")
    private Resource functionAliasDateDiff;

    @Value("classpath:db/ddl/h2/drop-all.sql")
    private Resource dropSchema;

    @Value("classpath:db/ddl/h2/create-schema.sql")
    private Resource createSchema;

    @Value("classpath:db/ddl/oracle/create-table-actor.sql")
    private Resource sqlCreateTableActor;


    @PostConstruct
    public void init() {
        addScriptOnInitialize(dropSchema);
        addScriptOnInitialize(createSchema);
        addScriptOnInitialize(sqlCreateTableActor);
        addScript(functionAliasDateDiff);
        super.init();
        setContinueOnError(false);
    }

    @Override
    public List<String> getScriptsPath() {
        List<String> scriptsPath = new ArrayList<>();
        scriptsPath.add("db/ddl/h2/drop-all.sql");
        scriptsPath.add("db/ddl/h2/create-schema.sql");
        scriptsPath.add("db/ddl/oracle/create-table-actor.sql");
        scriptsPath.add("db/ddl/h2/create-function.sql");
        return scriptsPath;
    }

}
