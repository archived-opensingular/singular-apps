package org.opensingular.server.commons.spring.database;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class MSSQLResourceDatabasePopulator extends AbstractResourceDatabasePopulator {

    @Value("classpath:db/ddl/sqlserver/create-function.sql")
    private Resource functionToChar;

    @PostConstruct
    public void init() {
     //   addScript(functionToChar);
        super.init();
    }

}
