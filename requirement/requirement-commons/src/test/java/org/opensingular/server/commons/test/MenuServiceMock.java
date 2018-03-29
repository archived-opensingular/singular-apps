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

package org.opensingular.server.commons.test;

import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.requirement.commons.WorkspaceConfigurationMetadata;
import org.opensingular.requirement.commons.service.dto.BoxConfigurationData;
import org.opensingular.requirement.commons.wicket.view.template.MenuService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MenuServiceMock implements MenuService {

    @Inject
    WorkspaceMetadataMockBean workspaceMetadataMockBean;

    @Override
    public Map<ModuleEntity, List<BoxConfigurationData>> getMap() {
        Map<ModuleEntity, List<BoxConfigurationData>> map = new LinkedHashMap<>();
        for (Map.Entry<ModuleEntity, WorkspaceConfigurationMetadata> config : workspaceMetadataMockBean.getMap().entrySet()) {
            map.put(config.getKey(), config.getValue().getBoxesConfiguration());
        }
        return map;
    }

    @Override
    public List<BoxConfigurationData> getMenusByCategory(ModuleEntity categoria) {
        return workspaceMetadataMockBean.gimmeSomeMock().getBoxesConfiguration();
    }

    @Override
    public BoxConfigurationData getMenuByLabel(String label) {
        return workspaceMetadataMockBean.gimmeSomeMock().getBoxesConfiguration().get(0);
    }

    @Override
    public List<ModuleEntity> getCategories() {
        List<ModuleEntity> categories         = new ArrayList<>();
        ModuleEntity       moduleEntity = new ModuleEntity();
        moduleEntity.setConnectionURL("/nada/nada");
        moduleEntity.setCod("CATEGORIAMOCK");
        moduleEntity.setName("Categoria Mock");
        categories.add(moduleEntity);
        return categories;
    }

    @Override
    public void reset() {

    }

    @Override
    public BoxConfigurationData getDefaultSelectedMenu(ModuleEntity categoriaSelecionada) {
        return null;
    }
}
