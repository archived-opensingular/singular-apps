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

package org.opensingular.requirement.module.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.requirement.commons.persistence.dao.server.BoxDAO;
import org.opensingular.requirement.commons.persistence.entity.form.BoxEntity;
import org.opensingular.requirement.commons.service.dto.BoxDefinitionData;
import org.opensingular.requirement.commons.service.dto.ItemBox;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class BoxService {

    @Inject
    private BoxDAO boxDAO;

    public BoxEntity saveBoxDefinition(ModuleEntity module, BoxDefinitionData boxData) {
        ItemBox itemBox = boxData.getItemBox();

        BoxEntity boxEntity = findByModuleAndName(module, itemBox.getName());

        if (boxEntity == null) {
            boxEntity = new BoxEntity();
        }

        boxEntity.setDescription(itemBox.getDescription());
        boxEntity.setIconName(itemBox.getIcone().getCssClass());
        boxEntity.setModule(module);
        boxEntity.setName(itemBox.getName());
        boxDAO.saveOrUpdate(boxEntity);
        boxDAO.flush();
        return boxEntity;
    }

    public BoxEntity findByModuleAndName(ModuleEntity moduleEntity, String name) {
        return boxDAO.findByModuleAndName(moduleEntity, name);
    }
}
