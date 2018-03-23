package org.opensingular.server.commons.spring.database;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public abstract class AbstractResourceDatabasePopulator extends ResourceDatabasePopulator {


    @Value("classpath:db/ddl/create-table-actor.sql")
    private Resource sqlCreateTableActor;

    @Value("classpath:db/ddl/create-table-tipo-tarefa.sql")
    private Resource sqlCreateTableTipoTarefa;

    @Value("classpath:db/dml/insert-flow-data.sql")
    private Resource insertSingularData;

    private List<Resource> scripts = new ArrayList<>();

    private List<String> scriptsText = new ArrayList<>();

    @PostConstruct
    public void init() {
        setSqlScriptEncoding(StandardCharsets.UTF_8.name());
        addScriptOnInitialize(sqlCreateTableActor);
        addScriptOnInitialize(sqlCreateTableTipoTarefa);
        addScriptOnInitialize(insertSingularData);
        scripts.forEach(s -> addScriptOnInitialize(s));
        scripts.clear();
        setContinueOnError(true);
    }

    @Override
    public void addScript(Resource script) {
        scripts.add(script);
    }

    public void addScriptOnInitialize(Resource script) {
        addScriptTextToList(script);
        super.addScript(script);
    }

    public List<String> getScriptsText() {
        return new ArrayList<>(scriptsText);
    }

    private void addScriptTextToList(Resource script) {
        try {
            File myFile = new File(script.getURI());
            String content = FileUtils.readFileToString(myFile, "UTF-8");
            scriptsText.add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
