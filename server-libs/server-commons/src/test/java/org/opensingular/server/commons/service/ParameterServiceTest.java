package org.opensingular.server.commons.service;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.server.commons.persistence.dao.ParameterDAO;
import org.opensingular.server.commons.persistence.dao.flow.ProcessGroupDAO;
import org.opensingular.server.commons.persistence.entity.parameter.ParameterEntity;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.springframework.test.annotation.Rollback;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

public class ParameterServiceTest extends SingularCommonsBaseTest {
    @Inject
    private ParameterService parameterService;

    @Inject
    private ParameterDAO parameterDAO;

    @Inject
    private ProcessGroupDAO processGroupDAO;

    @Test
    @Transactional
    @Rollback
    public void testFindByNameAndProcessGroup(){
        ProcessGroupEntity groupEntity = new ProcessGroupEntity();
        groupEntity.setCod("1");
        groupEntity.setName("groupName");
        groupEntity.setConnectionURL("connectionUrl Test");
        processGroupDAO.save(groupEntity);

        ParameterEntity entity = new ParameterEntity();
        entity.setName("testParameter");
        entity.setCodProcessGroup(groupEntity.getCod());
        entity.setValue("valor teste");

        parameterDAO.save(entity);

        Optional<ParameterEntity> testParameter = parameterService
                .findByNameAndProcessGroup("testParameter", groupEntity.getCod());
        Assert.assertEquals(entity, testParameter.get());

        testParameter = parameterService.findByNameAndProcessGroup("testParameter", groupEntity);
        Assert.assertEquals(entity, testParameter.get());

        Assert.assertEquals(entity.getName(), testParameter.get().getName());
        Assert.assertEquals(entity.getCod(), testParameter.get().getCod());
    }
}
