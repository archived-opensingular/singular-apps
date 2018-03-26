package org.opensingular.server.commons.spring.database;

import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class OracleResourceDatabasePopulator extends AbstractResourceDatabasePopulator {

    @Value("classpath:db/ddl/oracle/create-function.sql")
    private Resource functionDateDiff;

    @Value("classpath:db/ddl/oracle/create-table-actor.sql")
    private Resource sqlCreateTableActor;

    @PostConstruct
    public void init() {
        addScriptOnInitialize(sqlCreateTableActor);
        addScript(functionDateDiff);
        super.init();
    }

    @Override
    public List<String> getScriptsPath() {
        List<String> scriptsPath = super.getScriptsPath();
        scriptsPath.add("db/ddl/oracle/create-table-actor.sql");
        scriptsPath.add("db/ddl/oracle/create-function.sql");
        return scriptsPath;
    }

}
