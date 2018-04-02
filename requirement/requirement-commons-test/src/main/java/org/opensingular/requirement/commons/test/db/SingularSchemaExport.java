package org.opensingular.requirement.commons.test.db;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.opensingular.lib.commons.base.SingularException;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.commons.exception.ExportScriptGenerationException;
import org.springframework.core.io.Resource;

import javax.persistence.Entity;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SingularSchemaExport implements Loggable {

    /**
     * Método com objeto de gerar o script de toda a base do singular, inclusive com os inserts.
     *
     * @param packages          O pacote na qual deverá ser gerado o script.
     * @param dialect           O dialect do banco escolhido.
     * @param directoryFileName O diretorio na qual será gerado o script.
     * @param scriptsPath       O path dos scripts adicionais.
     * @return
     */
    public static Resource generateScript(String[] packages, Class<? extends Dialect> dialect,
                                          String directoryFileName, List<String> scriptsPath) {

        StringBuilder scriptsText = readScriptsContent(scriptsPath);


        try {
            Set<Class<?>>  typesAnnotatedWith = SingularClassPathScanner.get().findClassesAnnotatedWith(Entity.class);
            List<Class<?>> list               = typesAnnotatedWith.stream().filter(t -> filterPackages(t, packages)).collect(Collectors.toList());

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
            try (PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(new FileOutputStream(directoryFileName), "UTF-8"))) {
                Dialect  hibDialect = Dialect.getDialect(cfg.getProperties());
                String[] strings    = cfg.generateSchemaCreationScript(hibDialect);
                write(writer, strings, scriptsText);
            }
            return null;
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
            StringBuilder scriptsText = new StringBuilder("");
            if (CollectionUtils.isNotEmpty(scriptsPath)) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                for (String script : scriptsPath) {
                    if (script.startsWith("/")) {
                        script = script.replaceFirst("/", "");
                    }
                    InputStream stream  = classLoader.getResourceAsStream(script);
                    String      content = IOUtils.toString(stream, "UTF-8");
                    scriptsText.append(content);
                }
            }
            return scriptsText;
        } catch (IOException e) {
            throw SingularException.rethrow(e);
        }
    }

    private static void write(PrintWriter writer, String[] lines, StringBuilder scriptsAdicionais) {
        Formatter formatter = FormatStyle.DDL.getFormatter();

        if (scriptsAdicionais != null) {
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
