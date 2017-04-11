package org.opensingular.server.commons.test;

import org.opensingular.server.commons.admin.healthsystem.validation.database.ValidatorOracle;
import org.opensingular.server.commons.persistence.dto.healthsystem.ColumnInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.SequenceInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.TableInfoDTO;
import org.springframework.context.annotation.Primary;

import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Primary
@Named
public class ValidatorOracleMock extends ValidatorOracle {

    protected List<ColumnInfoDTO> getColumnsInfoFromTable(String table) {
        return null; // MOCK
    }

    protected List<String> getPermissionEspecificTable(String tabela) {
        return Arrays.asList("SELECT", "UPDATE", "ALTER", "DELETE");
    }

    @Transactional
    @Override
    public void checkColumnPermissions(TableInfoDTO tableInfoDTO) {
        // MOCK
    }
}
