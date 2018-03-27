package org.opensingular.server.commons.spring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nonnull;

import com.google.common.base.Joiner;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.opensingular.server.commons.exception.ResourceDatabasePopularException;
import org.opensingular.server.commons.spring.database.AbstractResourceDatabasePopulator;
import org.opensingular.server.commons.spring.database.SingularDataBaseEnum;
import org.opensingular.server.commons.spring.database.SingularDataBaseSuport;

public class ConfigureDatabaseResource {

    protected String[] getHibernatePackagesToScan() {
        return new String[]{
                "org.opensingular.flow.persistence.entity",
                "org.opensingular.server.commons.persistence.entity",
                "org.opensingular.app.commons.mail.persistence.entity",
                "org.opensingular.form.persistence.entity"};
    }

    protected String getUrlConnection() {
        return "jdbc:h2:./singularserverdb;AUTO_SERVER=TRUE;mode=ORACLE;CACHE_SIZE=4096;EARLY_FILTER=1;MULTI_THREADED=1;LOCK_TIMEOUT=15000;";
    }

    protected Properties getHibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", getHibernateDialect().getName());
        hibernateProperties.setProperty("hibernate.connection.isolation", "2");
        hibernateProperties.setProperty("hibernate.jdbc.batch_size", "30");
        hibernateProperties.setProperty("hibernate.show_sql", "false");
        hibernateProperties.setProperty("hibernate.format_sql", "true");
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        hibernateProperties.setProperty("hibernate.jdbc.use_get_generated_keys", "true");
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "true");
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", "true");
        hibernateProperties.setProperty("hibernate.hbm2ddl.import_files", getFormatImporFiles());
        hibernateProperties.setProperty("hibernate.hbm2ddl.import_files_sql_extractor", "org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor"); //Propriedade necessária para aceitar formatação das linhas dos import_files
        if (isDatabaseInitializerEnabled()) {
            hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        } else {
            hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        }
        /*não utilizar a singleton region factory para não conflitar com o cache do singular-server */
        hibernateProperties.setProperty("net.sf.ehcache.configurationResourceName", "/default-singular-ehcache.xml");
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        return hibernateProperties;
    }

    private String getFormatImporFiles() {
        List<String> listOrdenada = getImportFiles();
        //O hibernate executa os scripts na ordem reversa que é colocado na lista.
        listOrdenada.sort(Collections.reverseOrder());
        return Joiner.on(", ").join(listOrdenada);
    }

    public List<String> getImportFiles(String... directoryAndFile) {
        return Arrays.asList(directoryAndFile);
    }

    @Nonnull
    public Class<? extends Dialect> getHibernateDialect() {
        return Oracle10gDialect.class;
    }

    public boolean isDatabaseInitializerEnabled() {
        return !SingularProperties.get().isFalse("singular.enabled.h2.inserts");
    }

    public List<String> getScriptsPath() {
        return databasePopulator().getScriptsPath();
    }

    /**
     * Retorna os DataBase Populator supportado ate o momento, caso seja necessário adicioanr alterar o SingularDataBaseEnum
     *
     * @return Lista de Data Base suportado pelo projeto.
     * @See SingularDataBaseEnum
     */
    protected List<SingularDataBaseSuport> getSupportedDatabases() {
        return Arrays.asList(SingularDataBaseEnum.values());
    }


    /**
     * Responsavel por criar um DataBasePopulator de acordo com o dialect informado.
     * Favor olhar o metodo getSupportedDatabases() para maiores informações.
     *
     * @return retorna o dataBasePopulator especifico de acordo com o dialect.
     */
    AbstractResourceDatabasePopulator databasePopulator() {
        if (SqlUtil.useEmbeddedDatabase()) {
            return SingularDataBaseEnum.H2.getPopulatorBeanInstance();
        }
        return getSupportedDatabases()
                .stream()
                .filter(f -> f.isDialectSupported(getHibernateDialect()))
                .findFirst()
                .map(SingularDataBaseSuport::getPopulatorBeanInstance)
                .orElseThrow(() -> new ResourceDatabasePopularException("Dialect not Supported. Look for supported values in "
                        + SingularDefaultPersistenceConfiguration.class + ".getSupportedDatabases()"));

    }


}
