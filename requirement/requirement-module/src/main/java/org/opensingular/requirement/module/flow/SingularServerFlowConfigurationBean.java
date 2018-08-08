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

package org.opensingular.requirement.module.flow;

import org.opensingular.flow.core.Flow;
import org.opensingular.flow.persistence.util.HibernateSingularFlowConfigurationBean;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.SingularModule;
import org.opensingular.requirement.module.form.DefinitionsPackageProvider;
import org.opensingular.schedule.IScheduleService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

public class SingularServerFlowConfigurationBean extends HibernateSingularFlowConfigurationBean implements Loggable {

    @Inject
    protected SingularModule singularModule;

    @Inject
    protected PlatformTransactionManager transactionManager;

    @Inject
    private IScheduleService scheduleService;

    @Inject
    private DefinitionsPackageProvider definitionsPackageProvider;

    @PostConstruct
    protected void postConstruct() {
        this.setModuleCod(singularModule.abbreviation());
        this.setDefinitionsPackages(definitionsPackageProvider.get());
        Flow.setConf(this, true);
        initializeFlowDefinitionsDatabase();
    }

    @Override
    protected IScheduleService getScheduleService() {
        return scheduleService;
    }

    @Transactional
    public void initializeFlowDefinitionsDatabase() {
        if (SingularProperties.get().isTrue(SingularProperties.SINGULAR_EAGER_LOAD_FLOW_DEFINITIONS)) {
            new TransactionTemplate(transactionManager).execute(status -> {
                getLogger().info("INITIALIZING FLOW DEFINITIONS");
                getDefinitions().forEach(d -> {
                    try {
                        getLogger().info("INITIALIZING {}....", d.getName());
                        d.getEntityFlowVersion();
                    } catch (Exception e) {
                        getLogger().error(e.getMessage(), e);
                    }
                });
                return null;
            });
        }
    }

}