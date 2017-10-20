/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opensingular.server.commons.service;

import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.lib.support.persistence.util.SqlUtil;
import org.opensingular.server.commons.admin.healthsystem.validation.database.IValidatorDatabase;
import org.opensingular.server.commons.admin.healthsystem.validation.database.ValidatorFactory;
import org.opensingular.server.commons.persistence.dao.HealthSystemDAO;
import org.opensingular.server.commons.persistence.dto.healthsystem.ColumnInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.HealthInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.TableInfoDTO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HealthSystemDbService implements Loggable {
    @Inject
    private HealthSystemDAO saudeDao;

    public HealthInfoDTO getAllDbMetaData() {
        List<TableInfoDTO> tables = new ArrayList<>();
        try {
            Map<String, ClassMetadata> map = saudeDao.getAllDbMetaData();
            map.forEach((k, v) -> tables.add(getTableInfo((AbstractEntityPersister) v)));

            Optional<IValidatorDatabase> validator = verificaDialetoUtilizado();
            if (validator.isPresent()) {
                validator.get().checkAllInfoTable(tables);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            tables.clear();
        }
        return new HealthInfoDTO(tables);
    }

    private TableInfoDTO getTableInfo(AbstractEntityPersister persister) {
        TableInfoDTO tableInfoDTO = new TableInfoDTO();

        String[] name = SqlUtil.replaceSingularSchemaName(persister.getTableName()).split("\\.");
        tableInfoDTO.setSchema(name[0]);
        tableInfoDTO.setTableName(name[1]);

        List<String> columns = new ArrayList<>();

        String[] propertyNames = persister.getPropertyNames();

        Arrays.asList(propertyNames).forEach(propertyName ->
                columns.add(persister.getPropertyColumnNames(propertyName)[0]));

        Arrays.asList(persister.getIdentifierColumnNames()).forEach(chave -> {
            if (!columns.contains(chave)) {
                columns.add(chave);
            }
        });

        List<ColumnInfoDTO> columnsType = new ArrayList<>();
        columns.forEach(col -> columnsType.add(new ColumnInfoDTO(col, true)));
        tableInfoDTO.setColumnsInfo(columnsType);

        return tableInfoDTO;
    }

    private Optional<IValidatorDatabase> verificaDialetoUtilizado() {
        String hibernateDialect = saudeDao.getHibernateDialect();
        return Optional.ofNullable(ValidatorFactory.getValidator(hibernateDialect));
    }
}
