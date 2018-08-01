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

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.requirement.module.BoxController;
import org.opensingular.requirement.module.BoxControllerFactory;
import org.opensingular.requirement.module.BoxInfo;
import org.opensingular.requirement.module.SingularModuleConfiguration;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.persistence.dao.BoxDAO;
import org.opensingular.requirement.module.persistence.entity.form.BoxEntity;
import org.opensingular.requirement.module.service.dto.BoxDefinitionData;
import org.opensingular.requirement.module.service.dto.ItemBox;
import org.opensingular.requirement.module.workspace.BoxDefinition;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
public class BoxService {
    @Inject
    private BoxDAO boxDAO;

    @Inject
    private BeanFactory beanFactory;

    @Inject
    private BoxControllerFactory boxControllerFactory;

    @Inject
    private SingularModuleConfiguration singularModuleConfiguration;

    @Transactional
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

    public BoxDefinitionData buildBoxDefinitionData(BoxInfo boxInfo, IServerContext context) {
        BoxDefinition factory = beanFactory.getBean(boxInfo.getBoxDefinitionClass());
        ItemBox itemBox = factory.getItemBox();
        itemBox.setFieldsDatatable(factory.getDatatableFields());
        itemBox.setId(boxInfo.getBoxId());
        return new BoxDefinitionData(itemBox, boxInfo.getRequirements());
    }

    public List<BoxDefinitionData> buildItemBoxes(IServerContext context) {
        return singularModuleConfiguration.getBoxByContext(context)
                .stream()
                .map(box -> buildBoxDefinitionData(box, context))
                .collect(Collectors.toList());
    }

    public Optional<BoxController> getBoxControllerByBoxId(String boxId) {
        return singularModuleConfiguration.getBoxByBoxId(boxId).map(boxControllerFactory::create);
    }
}