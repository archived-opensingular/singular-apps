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

package org.opensingular.requirement.module.persistence.query;

import org.opensingular.flow.persistence.entity.QActor;
import org.opensingular.flow.persistence.entity.QFlowDefinitionEntity;
import org.opensingular.flow.persistence.entity.QFlowInstanceEntity;
import org.opensingular.flow.persistence.entity.QModuleEntity;
import org.opensingular.flow.persistence.entity.QTaskDefinitionEntity;
import org.opensingular.flow.persistence.entity.QTaskInstanceEntity;
import org.opensingular.flow.persistence.entity.QTaskVersionEntity;
import org.opensingular.form.persistence.entity.QFormEntity;
import org.opensingular.form.persistence.entity.QFormTypeEntity;
import org.opensingular.form.persistence.entity.QFormVersionEntity;
import org.opensingular.requirement.module.persistence.entity.form.QApplicantEntity;
import org.opensingular.requirement.module.persistence.entity.form.QDraftEntity;
import org.opensingular.requirement.module.persistence.entity.form.QFormRequirementEntity;
import org.opensingular.requirement.module.persistence.entity.form.QRequirementDefinitionEntity;
import org.opensingular.requirement.module.persistence.entity.form.QRequirementEntity;


public class RequirementSearchAliases {

    public static final String COD_REQUIREMENT = "codRequirement";
    public static final String DESCRIPTION = "description";

    @Deprecated /*SITUATION will be removed in a future version. USE TASK_NAME */
    public static final String SITUATION = "situation";

    public static final String SOLICITANTE = "solicitante";
    public static final String TASK_NAME = "taskName";
    public static final String TASK_TYPE = "taskType";
    public static final String PROCESS_NAME = "processName";
    public static final String CREATION_DATE = "creationDate";
    public static final String TYPE = "type";
    public static final String PROCESS_TYPE = "processType";
    public static final String SITUATION_BEGIN_DATE = "situationBeginDate";
    public static final String TASK_INSTANCE_ID = "taskInstanceId";
    public static final String PROCESS_BEGIN_DATE = "processBeginDate";
    public static final String EDITION_DATE = "editionDate";
    public static final String FLOW_INSTANCE_ID = "flowInstanceId";
    public static final String ROOT_REQUIREMENT           = "rootRequirement";
    public static final String PARENT_REQUIREMENT         = "parentRequirement";
    public static final String VERSION_STAMP              = "versionStamp";
    public static final String COD_USUARIO_ALOCADO        = "codUsuarioAlocado";
    public static final String NOME_USUARIO_ALOCADO       = "nomeUsuarioAlocado";
    public static final String MODULE_COD                 = "moduleCod";
    public static final String MODULE_CONTEXT             = "moduleContext";
    public static final String REQUIREMENT_DEFINITION_ID  = "requirementDefinitionId";
    public static final String REQUIREMENT_DEFINITION_KEY = "requirementDefinitionKey";

    public static final String TASK_ID = "taskId";

    public QRequirementEntity     requirement                   = new QRequirementEntity("requirement");
    public QApplicantEntity       applicantEntity               = new QApplicantEntity("applicantEntity");
    public QFlowInstanceEntity    flowInstance                  = new QFlowInstanceEntity("flowInstance");
    public QFormRequirementEntity formRequirementEntity         = new QFormRequirementEntity("formRequirementEntity");
    public QFormEntity            formEntity                    = new QFormEntity("formEntity");
    public QDraftEntity           currentDraftEntity            = new QDraftEntity("currentDraftEntity");
    public QFormEntity            formDraftEntity               = new QFormEntity("formDraftEntity");
    public QFormVersionEntity     currentFormDraftVersionEntity = new QFormVersionEntity("currentFormDraftVersionEntity");
    public QFormVersionEntity     currentFormVersion            = new QFormVersionEntity("currentFormVersion");
    public QFlowDefinitionEntity  flowDefinitionEntity          = new QFlowDefinitionEntity("flowDefinitionEntity");
    public QModuleEntity                module                        = new QModuleEntity("module");
    public QFormTypeEntity              formType                      = new QFormTypeEntity("formType");
    public QFormTypeEntity              formDraftType         = new QFormTypeEntity("formDraftType");
    public QTaskInstanceEntity          task                  = new QTaskInstanceEntity("task");
    public QTaskDefinitionEntity        taskDefinition        = new QTaskDefinitionEntity("taskDefinition");
    public QTaskVersionEntity           taskVersion           = new QTaskVersionEntity("taskVersion");
    public QActor                       allocatedUser         = new QActor("allocatedUser");
    public QRequirementDefinitionEntity requirementDefinition = new QRequirementDefinitionEntity("requirementDefinitionEntity");
}