package org.opensingular.server.commons.test;

import org.opensingular.server.commons.admin.healthsystem.validation.database.ValidatorOracle;
import org.opensingular.server.commons.persistence.dto.healthsystem.ColumnInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.TableInfoDTO;
import org.springframework.context.annotation.Primary;

import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Primary
@Named
public class ValidatorOracleMock extends ValidatorOracle {

    private boolean mockEncontradoBanco = true;

    protected List<ColumnInfoDTO> getColumnsInfoFromTable(String table) {
        return null;
    }

    protected List<String> getPermissionEspecificTable(String tabela) {
        if(mockEncontradoBanco){
            return Arrays.asList("SELECT", "UPDATE", "INSERT", "DELETE");
        }else{
            return Arrays.asList("SELECT", "UPDATE", "DELETE");
        }
    }

    @Transactional
    @Override
    public void checkColumnPermissions(TableInfoDTO tableInfoDTO) {
        // pra garantir que vai testar todos os casos de validacao.
        if(mockEncontradoBanco){
            mockEncontradoBanco = false;
            tableInfoDTO.getColumnsInfo().forEach(column->{
                column.setFoundDataBase(true);
                column.setFoundHibernate(true);
            });
        }else{
            mockEncontradoBanco = true;
            boolean mockEncontradoHibernate = true;
            for(ColumnInfoDTO columnInfo : tableInfoDTO.getColumnsInfo()){
                if(mockEncontradoHibernate){
                    columnInfo.setFoundHibernate(mockEncontradoHibernate);
                    mockEncontradoHibernate = !mockEncontradoHibernate;
                }else{
                    columnInfo.setFoundHibernate(mockEncontradoHibernate);
                    mockEncontradoHibernate = !mockEncontradoHibernate;
                }
            }
        }
    }
}
