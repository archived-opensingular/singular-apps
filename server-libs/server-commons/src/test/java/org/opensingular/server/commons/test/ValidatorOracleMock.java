package org.opensingular.server.commons.test;

import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.opensingular.server.commons.admin.healthsystem.validation.database.ValidatorOracle;
import org.opensingular.server.commons.persistence.dto.healthsystem.ColumnInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.SequenceInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.TableInfoDTO;
import org.springframework.context.annotation.Primary;

import javax.inject.Named;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValidatorOracleMock extends ValidatorOracle {

    private boolean mockEncontradoBanco = true;

    @Override
    protected List<ColumnInfoDTO> getColumnsInfoFromTable(String table) {
        ColumnInfoDTO column = new ColumnInfoDTO();

        column.setSchema("");
        column.setColumnName("");
        column.setDataType("");
        column.setTableName("");
        column.setNullable(false);
        column.setFoundDataBase(true);

        List<ColumnInfoDTO> columns = new ArrayList<>();
        columns.add(column);

        return columns;
    }

    @Override
    protected List<String> getPermissionEspecificTable(String tabela) {
        if(mockEncontradoBanco){
            mockEncontradoBanco = !mockEncontradoBanco;
            return Arrays.asList("SELECT", "UPDATE", "INSERT", "DELETE");
        }else{
            return Arrays.asList("SELECT", "UPDATE", "DELETE");
        }
    }

    @Override
    protected SequenceInfoDTO getSequenceInfoDTO(String sequenceName) {
        SequenceInfoDTO info = new SequenceInfoDTO();

        info.setSequenceName(sequenceName);
        info.setFound(true);

        return info;
    }

}
