/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.server.p.commons.admin.healthsystem;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.persistence.dto.healthsystem.ColumnInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.SequenceInfoDTO;
import org.opensingular.server.commons.persistence.dto.healthsystem.TableInfoDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class HealthSystemDTOTest {

    @Test
    public void tableInfoDTOTest(){
        TableInfoDTO dto = new TableInfoDTO();

        dto.setTableName("tableName");
        dto.setSchema("schema");
        dto.setFound(true);
        dto.setUserPrivs(Arrays.asList("SELECT", "UPDATE", "ALTER", "DELETE"));
        dto.setColumnsInfo(null);

        Assert.assertTrue(dto.isFound());
        Assert.assertEquals("tableName", dto.getTableName());
        Assert.assertEquals("schema", dto.getSchema());
        Assert.assertNotNull(dto.getUserPrivs());
        Assert.assertNull(dto.getColumnsInfo());
    }

    @Test
    public void columnInfoDTOTest(){
        ColumnInfoDTO columnDTO = new ColumnInfoDTO();

        columnDTO.setTableName("tableName");
        columnDTO.setSchema("schema");
        columnDTO.setColumnName("columnName");
        columnDTO.setDataType("varchar");
        columnDTO.setFoundDataBase(false);
        columnDTO.setFoundHibernate(true);
        columnDTO.setCharLength(null);
        columnDTO.setDataLength(new BigDecimal(50));
        columnDTO.setDataPrecision(new BigDecimal(10));
        columnDTO.setNullable(false);

        Assert.assertEquals("tableName", columnDTO.getTableName());
        Assert.assertEquals("schema", columnDTO.getSchema());
        Assert.assertEquals("columnName", columnDTO.getColumnName());
        Assert.assertEquals("varchar", columnDTO.getDataType());
        Assert.assertEquals(new BigDecimal(50), columnDTO.getDataLength());
        Assert.assertEquals(new BigDecimal(10), columnDTO.getDataPrecision());
        Assert.assertNull(columnDTO.getCharLength());
        Assert.assertFalse(columnDTO.isFoundDataBase());
        Assert.assertTrue(columnDTO.isFoundHibernate());
        Assert.assertFalse(columnDTO.isNullable());

        Assert.assertNotNull(new ColumnInfoDTO("columnName2", false));
    }

    @Test
    public void sequenceInfoDTOTest(){
        SequenceInfoDTO sequenceDto = new SequenceInfoDTO();

        sequenceDto.setFound(true);
        sequenceDto.setSequenceName("sequence");
        sequenceDto.setMaxValue(new BigDecimal(100));
        sequenceDto.setMinValue(new BigDecimal(0));
        sequenceDto.setCurrentValue(new BigDecimal(10));
        sequenceDto.setIncrement(new BigDecimal(1));

        Assert.assertEquals("sequence", sequenceDto.getSequenceName());
        Assert.assertEquals(new BigDecimal(100), sequenceDto.getMaxValue());
        Assert.assertEquals(new BigDecimal(0), sequenceDto.getMinValue());
        Assert.assertEquals(new BigDecimal(10), sequenceDto.getCurrentValue());
        Assert.assertEquals(new BigDecimal(1), sequenceDto.getIncrement());
        Assert.assertTrue(sequenceDto.isFound());
    }
}
