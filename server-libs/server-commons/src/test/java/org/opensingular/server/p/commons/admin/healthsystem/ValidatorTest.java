package org.opensingular.server.p.commons.admin.healthsystem;

import org.junit.Before;
import org.junit.Test;
import org.opensingular.server.commons.admin.healthsystem.validation.database.IValidatorDatabase;
import org.opensingular.server.commons.persistence.dto.healthsystem.ColumnInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.TableInfoDTO;
import org.opensingular.server.commons.test.SingularCommonsBaseTest;
import org.opensingular.server.commons.test.SingularServletContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.reset;

@TestExecutionListeners(listeners = {SingularServletContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ValidatorTest extends SingularCommonsBaseTest {
    
    @Inject
    private IValidatorDatabase validatorDatabase;

    @Before
    public void setUp() {
        reset(validatorDatabase);
    }

    @Test
    public void getTablesPermission() {
        TableInfoDTO       tableInfoDTO = new TableInfoDTO();
        tableInfoDTO.setSchema("DBSINGULAR");
        tableInfoDTO.setTableName("TB_REQUISICAO");
        List<TableInfoDTO> permissions = validatorDatabase.getTablesPermission(Collections.singletonList(tableInfoDTO));

        assertEquals(1L, permissions.size());
    }

    @Test
    public void checkColumnPermissions() {
        TableInfoDTO       tableInfoDTO = new TableInfoDTO();
        tableInfoDTO.setSchema("DBSINGULAR");
        tableInfoDTO.setTableName("TB_REQUISICAO");

        ColumnInfoDTO cod = new ColumnInfoDTO("CO_REQUISICAO", false);
        ColumnInfoDTO instance = new ColumnInfoDTO("CO_INSTANCIA_PROCESSO", false);
        tableInfoDTO.setColumnsInfo(Arrays.asList(cod, instance));

        validatorDatabase.checkColumnPermissions(tableInfoDTO);
    }

    @Test
    public void checkSequences() {
        validatorDatabase.checkSequences(Collections.singletonList(""));
    }

    @Test
    public void getAllInfoTable() {
        validatorDatabase.getAllInfoTable(Collections.singletonList(""));
    }

    @Test
    public void checkAllInfoTable() {
        TableInfoDTO       tableInfoDTO = new TableInfoDTO();
        tableInfoDTO.setSchema("DBSINGULAR");
        tableInfoDTO.setTableName("TB_REQUISICAO");

        validatorDatabase.checkAllInfoTable(Collections.singletonList(tableInfoDTO));
    }
    
}
