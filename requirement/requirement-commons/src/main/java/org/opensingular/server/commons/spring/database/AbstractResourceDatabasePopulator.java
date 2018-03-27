package org.opensingular.server.commons.spring.database;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public abstract class AbstractResourceDatabasePopulator extends ResourceDatabasePopulator {


    private List<Resource> scripts = new ArrayList<>();

    @PostConstruct
    public void init() {
        setSqlScriptEncoding(StandardCharsets.UTF_8.name());
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
        return new ArrayList<>();
    }
}
