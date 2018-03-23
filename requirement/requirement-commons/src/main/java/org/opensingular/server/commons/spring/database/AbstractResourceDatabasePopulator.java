package org.opensingular.server.commons.spring.database;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class AbstractResourceDatabasePopulator extends ResourceDatabasePopulator {


    @Value("classpath:db/ddl/create-tables.sql")
    private Resource sqlCreateTables;

    @Value("classpath:db/dml/insert-flow-data.sql")
    private Resource insertSingularData;

    private List<Resource> scripts = new ArrayList<>();

    @PostConstruct
    public void init() {
        setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        super.addScript(sqlCreateTables);
        super.addScript(insertSingularData);
        scripts.forEach(s -> super.addScript(s));
        scripts.clear();
    }

    @Override
    public void addScript(Resource script) {
        scripts.add(script);
    }

    public void addScriptOnInitialize(Resource script) {
        super.addScript(script);
    }
}
