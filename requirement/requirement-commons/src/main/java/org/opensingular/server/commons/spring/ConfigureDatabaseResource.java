package org.opensingular.server.commons.spring;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nonnull;

import com.google.common.base.Joiner;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServer2008Dialect;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.opensingular.server.commons.exception.ResourceDatabasePopularException;
import org.opensingular.server.commons.spring.database.AbstractResourceDatabasePopulator;
import org.opensingular.server.commons.spring.database.SingularDataBaseEnum;
import org.opensingular.server.commons.spring.database.SingularDataBaseSuport;

public class ConfigureDatabaseResource {

    public String[] getHibernatePackagesToScan() {
        return new String[]{
                "org.opensingular.flow.persistence.entity",
                "org.opensingular.server.commons.persistence.entity",
                "org.opensingular.app.commons.mail.persistence.entity",
                "org.opensingular.form.persistence.entity"};
    }

    public Properties getHibernateProperties() {
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
        hibernateProperties.setProperty("hibernate.hbm2ddl.import_files", getImportFiles());
        if (isDatabaseInitializerEnabled()) {
            hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        }
        /*não utilizar a singleton region factory para não conflitar com o cache do singular-server */
        hibernateProperties.setProperty("net.sf.ehcache.configurationResourceName", "/default-singular-ehcache.xml");
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        return hibernateProperties;
    }

    public String getImportFiles(String... directoryAndFile) {
        return Joiner.on(", ").join(directoryAndFile);
    }

    @Nonnull
    public Class<? extends Dialect> getHibernateDialect() {
        return SQLServer2008Dialect.class;
    }

    public boolean isDatabaseInitializerEnabled() {
        return !SingularProperties.get().isFalse("singular.enabled.h2.inserts");
    }

    public List<String> getScriptsPath(){
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
                .orElseThrow(() -> new ResourceDatabasePopularException("Dialect not Supported. Look for supported values in " + SingularDefaultPersistenceConfiguration.class + ".getSupportedDatabases()"));
    }
}
