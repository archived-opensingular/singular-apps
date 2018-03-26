package org.opensingular.server.commons.spring.database;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public abstract class AbstractResourceDatabasePopulator extends ResourceDatabasePopulator {

    @Value("classpath:db/ddl/create-table-actor.sql")
    private Resource sqlCreateTableActor;


    private List<Resource> scripts = new ArrayList<>();

    @PostConstruct
    public void init() {
        setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        addScriptOnInitialize(sqlCreateTableActor);
        scripts.forEach(s -> addScriptOnInitialize(s));
        scripts.clear();
        setContinueOnError(true);
    }

    @Override
    public void addScript(Resource script) {
        scripts.add(script);
    }

    public void addScriptOnInitialize(Resource script) {
        super.addScript(script);
    }

    public List<String> getScriptsPath(){
        List<String> scriptsPath = new ArrayList<>();
        scriptsPath.add("db/ddl/create-table-actor.sql");
        return scriptsPath;
    }
}
