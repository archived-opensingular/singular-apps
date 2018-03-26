package org.opensingular.server.commons.spring.database;

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

    @Override
    public List<String> getScriptsPath() {
        List<String> scriptsPath = new ArrayList<>();
        scriptsPath.add("db/ddl/h2/drop-all.sql");
        scriptsPath.add("db/ddl/h2/create-schema.sql");
        scriptsPath.addAll(super.getScriptsPath());
        scriptsPath.add("db/ddl/h2/create-function.sql");
        scriptsPath.add("db/ddl/oracle/create-sequence.sql");
        return scriptsPath;
    }

}
