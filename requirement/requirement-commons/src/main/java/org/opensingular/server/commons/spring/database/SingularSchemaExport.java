package org.opensingular.server.commons.spring.database;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.opensingular.lib.commons.scan.SingularClassPathScanner;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.RESTPaths;
import org.opensingular.server.commons.exception.ExportScriptGenerationException;
import org.springframework.core.io.Resource;

public class SingularSchemaExport implements Loggable {


    /**
     * Método com objeto de gerar o script de toda a base do singular, inclusive com os inserts.
     *
     * @param packageStr        O pacote na qual deverá ser gerado o script.
     * @param dialect           O dialect do banco escolhido.
     * @param directoryFileName O diretorio na qual será gerado o script.
     * @param scriptsAdicionais scripts adicionais.
     * @return
     */
    public static Resource generateScript(String packageStr, Class<? extends Dialect> dialect,
            String directoryFileName, List<String> scriptsAdicionais) {

        try {
            Set<Class<?>> typesAnnotatedWith = SingularClassPathScanner.get().findClassesAnnotatedWith(Entity.class);
            List<Class<?>> list = typesAnnotatedWith.stream().filter(c ->
                    Strings.isNotEmpty(packageStr) ? c.getPackage().getName().startsWith(packageStr) : true
            ).collect(Collectors.toList());


            //create a minimal configuration
            Configuration cfg = new Configuration();
            cfg.setProperty("hibernate.dialect", dialect != null ? dialect.getName() : SQLServer2008Dialect.class.getName());
            cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");

            for (Class<?> c : list) {
                cfg.addAnnotatedClass(c);
            }

            //build all the mappings, before calling the AuditConfiguration
            cfg.buildMappings();


            RESTPaths.class.getClassLoader().getResource("db/ddl/drops.sql");
            File tempFile = new File(directoryFileName);
            try (PrintWriter writer = new PrintWriter(tempFile)) {
                Dialect hibDialect = Dialect.getDialect(cfg.getProperties());
                String[] strings = cfg.generateSchemaCreationScript(hibDialect);
                write(writer, strings, scriptsAdicionais);
            }
            return null;

            //execute the export    NÃO É POSSIVEL FAZER COM SCRIPTS ADICIONAIS POR CAUSA do exporter.acceptsImportScripts()
//           SchemaExport export = new SchemaExport(cfg);
//
//
//            if (Strings.isEmpty(directoryFileName)) {
//                File tempFile = File.createTempFile("schema-singular", ".sql");
//                exportParameters(export,tempFile.getAbsolutePath());
//                StringBuilder sqls = new StringBuilder("");
//                for (String sql : FileUtils.readLines(tempFile, StandardCharsets.UTF_8.name())) {
//                    sqls.append(sql);
//                }
//                return new ByteArrayResource(sqls.toString().getBytes());
//            } else {
//                exportParameters(export, directoryFileName);
//                return null;
//            }


        } catch (Exception e) {
            throw new ExportScriptGenerationException(e.getMessage(), e);
        }
    }

    private static void write(PrintWriter writer, String[] lines, List<String> scriptsAdicionais) {
        Formatter formatter = FormatStyle.DDL.getFormatter();
        for (String string : lines) {
            String lineFormated = formatter.format(string) + "; \n";
            System.out.println(lineFormated);
            writer.write(lineFormated);
        }
        scriptsAdicionais.forEach(s -> {
            String lineFormated = formatter.format(s) + "; \n";
            System.out.println(lineFormated);
            writer.write(lineFormated);
        });
    }


}
