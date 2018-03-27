package org.opensingular.server.commons.test.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.RESTPaths;
import org.opensingular.server.commons.exception.ExportScriptGenerationException;
import org.opensingular.server.commons.spring.SingularDefaultPersistenceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

public class SingularSchemaExport implements Loggable {

    private static final Logger logger = LoggerFactory.getLogger(SingularSchemaExport.class);

    /**
     * Método com objeto de gerar o script de toda a base do singular, inclusive com os inserts.
     *
     * @param packageStr        O pacote na qual deverá ser gerado o script.
     * @param dialect           O dialect do banco escolhido.
     * @param directoryFileName O diretorio na qual será gerado o script.
     * @param scriptsPath O path dos scripts adicionais.
     * @return
     */
    public static Resource generateScript(String packageStr, Class<? extends Dialect> dialect,
            String directoryFileName, List<String> scriptsPath) {

        StringBuilder scriptsText = gerarScriptsTextByFilesPath(scriptsPath);


        try {
            Set<Class<?>> typesAnnotatedWith = SingularClassPathScanner.get().findClassesAnnotatedWith(Entity.class);
            List<Class<?>> list = typesAnnotatedWith.stream().filter(c ->
                    Strings.isNotEmpty(packageStr) ? c.getPackage().getName().startsWith(packageStr) : true
            ).collect(Collectors.toList());


            //create a minimal configuration
            Configuration cfg = new Configuration();
            cfg.setProperty("hibernate.dialect", dialect != null ? dialect.getName() : H2Dialect.class.getName());
            cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");

            for (Class<?> c : list) {
                cfg.addAnnotatedClass(c);
            }

            //build all the mappings, before calling the AuditConfiguration
            cfg.buildMappings();


            RESTPaths.class.getClassLoader().getResource("db/ddl/drops.sql");
            try (PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(new FileOutputStream(directoryFileName), "UTF-8"))) {
                Dialect hibDialect = Dialect.getDialect(cfg.getProperties());
                String[] strings = cfg.generateSchemaCreationScript(hibDialect);
                write(writer, strings, scriptsText);
            }
            return null;
        } catch (Exception e) {
            throw new ExportScriptGenerationException(e.getMessage(), e);
        }
    }

    private static StringBuilder gerarScriptsTextByFilesPath(List<String> scriptsPath) {
        StringBuilder scriptsText = new StringBuilder("");
        if(CollectionUtils.isNotEmpty(scriptsPath)) {
            ClassLoader classLoader = SingularDefaultPersistenceConfiguration.class.getClassLoader();
            scriptsPath.forEach(script -> {
                InputStream stream = classLoader.getResourceAsStream(script);
                try {
                    String content = IOUtils.toString(stream, "UTF-8");
                    scriptsText.append(content);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            });
        }
        return scriptsText;
    }

    private static void write(PrintWriter writer, String[] lines, StringBuilder scriptsAdicionais) {
        Formatter formatter = FormatStyle.DDL.getFormatter();

        if(scriptsAdicionais != null) {
            System.out.println(scriptsAdicionais);
            writer.write(formatter.format(scriptsAdicionais.toString()));
        }

        for (String string : lines) {
            String lineFormated = formatter.format(string) + "; \n";
            System.out.println(lineFormated);
            writer.write(lineFormated);
        }
    }


}
