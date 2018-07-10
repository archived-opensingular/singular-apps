package org.opensingular.app.commons.spring.persistence.database;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;

public class SingularSchemaExport implements Loggable {


    private SingularSchemaExport() {

    }

    /**
     * Método com objeto de gerar o script de toda a base do singular, inclusive com os inserts.
     *
     * @param packages          O pacote na qual deverá ser gerado o script.
     * @param dialect           O dialect do banco escolhido.
     * @param directoryFileName O diretorio na qual será gerado o script.
     * @param scriptsPath       O path dos scripts adicionais.
     * @return Return all the scripts DML and DDL that will be executed by Hibernate.
     */
    public static void generateScriptToFile(String[] packages, Class<? extends Dialect> dialect, List<String> scriptsPath, String directoryFileName) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(directoryFileName), StandardCharsets.UTF_8))) {//NOSONAR
            writer.write(generateScript(packages, dialect, scriptsPath).toString());
        } catch (Exception e) {
            throw new ExportScriptGenerationException(e.getMessage(), e);
        }
    }

    /**
     * Método com objeto de gerar o script de toda a base do singular, inclusive com os inserts.
     *
     * @param packages    O pacote na qual deverá ser gerado o script.
     * @param dialect     O dialect do banco escolhido.
     * @param scriptsPath O path dos scripts adicionais.
     * @return Return all the scripts DML and DDL that will be executed by Hibernate.
     */
    public static StringBuilder generateScript(String[] packages, Class<? extends Dialect> dialect, List<String> scriptsPath) {

        StringBuilder scriptsText = readScriptsContent(scriptsPath);


        try {
            Set<Class<?>> typesAnnotatedWith = SingularClassPathScanner.get().findClassesAnnotatedWith(Entity.class);
            List<Class<?>> list = typesAnnotatedWith
                    .stream()
                    .filter(t -> filterPackages(t, packages))
                    .collect(Collectors.toList());

            //create a minimal configuration
            Configuration cfg = new Configuration();
            cfg.setProperty("hibernate.dialect", dialect != null ? dialect.getName() : H2Dialect.class.getName());
            cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");

            for (Class<?> c : list) {
                cfg.addAnnotatedClass(c);
            }

            //build all the mappings, before calling the AuditConfiguration
            cfg.buildMappings();

            Thread.currentThread().getContextClassLoader().getResource("db/ddl/drops.sql");
            Dialect hibDialect = Dialect.getDialect(cfg.getProperties());
            String[] scriptsEntities = cfg.generateSchemaCreationScript(hibDialect);
            return formatterScript(scriptsEntities, scriptsText);
        } catch (Exception e) {
            throw new ExportScriptGenerationException(e.getMessage(), e);
        }
    }

    private static boolean filterPackages(Class<?> t, String[] packages) {
        for (String somePackage : packages) {
            if (t.getPackage().getName().startsWith(somePackage)) {
                return true;
            }
        }
        return false;
    }

    private static StringBuilder readScriptsContent(List<String> scriptsPath) {
        try {
            StringBuilder scriptsText = new StringBuilder();
            if (CollectionUtils.isNotEmpty(scriptsPath)) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                for (String script : scriptsPath) {
                    script = removeStartingSlash(script);
                    configureScriptTextByScriptPath(scriptsText, classLoader, script);
                }
            }
            return scriptsText;
        } catch (IOException e) {
            throw SingularException.rethrow(e);
        }
    }

    private static void configureScriptTextByScriptPath(StringBuilder scriptsText, ClassLoader classLoader,
            String scriptPath) throws IOException {
        InputStream stream = classLoader.getResourceAsStream(scriptPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            while (line != null) {
                scriptsText.append(line).append('\n');
                line = reader.readLine();
            }
        }
    }

    private static String removeStartingSlash(String script) {
        if (script.startsWith("/")) {
            return script.replaceFirst("/", "");
        }
        return script;
    }

    /**
     * Method reponsible to formmat the script, should skip line, and put the DDL and DML scripts.
     *
     * @param scriptsEntities   the script of the entities generated by hibernate.
     * @param scriptsAdicionais some extra scripts, normally DML scripts.
     * @return A StringBuilder containg all DML and DDL scripts.
     */
    private static StringBuilder formatterScript(String[] scriptsEntities, StringBuilder scriptsAdicionais) {
        Formatter formatter = FormatStyle.DDL.getFormatter();

        StringBuilder stringSql = new StringBuilder();

        for (String string : scriptsEntities) {
            String lineFormated = formatter.format(string) + "; \n";
            stringSql.append(lineFormated);
        }

        if (scriptsAdicionais != null) {
            stringSql.append(scriptsAdicionais);
        }

        return stringSql;
    }


}
