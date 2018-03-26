package org.opensingular.server.commons.spring.database;

import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class MSSQLResourceDatabasePopulator extends AbstractResourceDatabasePopulator {

    @Value("classpath:db/ddl/sqlserver/create-function.sql")
    private Resource functionToChar;

    @PostConstruct
    public void init() {
        addScript(functionToChar);
        super.init();
    }


    @Override
    public List<String> getScriptsPath() {
        List<String> scriptsPath = super.getScriptsPath();
        scriptsPath.add("db/ddl/sqlserver/create-function.sql");
        return scriptsPath;
    }
}
