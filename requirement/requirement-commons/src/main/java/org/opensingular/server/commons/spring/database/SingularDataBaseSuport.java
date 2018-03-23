package org.opensingular.server.commons.spring.database;

import org.hibernate.dialect.Dialect;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public interface SingularDataBaseSuport {

    ResourceDatabasePopulator getPopulatorBeanInstance();

    boolean isDialectSupported(Class<? extends Dialect> dialect);
}
