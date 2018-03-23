package org.opensingular.server.commons.spring.database;

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

    @Value("classpath:db/ddl/oracle/create-sequence.sql")
    private Resource createSequence;

    @PostConstruct
    public void init() {
        addScriptOnInitialize(dropSchema);
        addScriptOnInitialize(createSchema);
        addScript(createSequence);
        addScript(functionAliasDateDiff);
        super.init();
        setContinueOnError(false);
    }

}
