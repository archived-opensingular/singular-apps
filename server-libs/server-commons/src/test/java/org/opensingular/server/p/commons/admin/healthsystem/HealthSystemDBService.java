package org.opensingular.server.p.commons.admin.healthsystem;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.persistence.dto.healthsystem.HealthInfoDTO;
import org.opensingular.server.commons.service.HealthSystemDbService;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;

import javax.inject.Inject;

public class HealthSystemDBService extends SingularCommonsBaseTest {
    @Inject
    private HealthSystemDbService service;

    @Test
    public void getAllDbMetaDataTest(){
        HealthInfoDTO allDbMetaData = service.getAllDbMetaData();

        Assert.assertNotNull(allDbMetaData);
        Assert.assertNotNull(allDbMetaData.getTablesList());
    }
}
