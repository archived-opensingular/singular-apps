package org.opensingular.server.commons.spring.database;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class H2ResourceDatabasePopulator extends AbstractResourceDatabasePopulator{

    @Value("classpath:db/ddl/h2/create-function.sql")
    private Resource functionAliasDateDiff;

    @PostConstruct
    public void init(){
        addScript(functionAliasDateDiff);
        super.init();
    }

}
