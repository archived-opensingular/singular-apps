package org.opensingular.server.commons.persistence.context;

import org.opensingular.flow.persistence.entity.QActor;
import org.opensingular.flow.persistence.entity.QProcessDefinitionEntity;
import org.opensingular.flow.persistence.entity.QModuleEntity;
import org.opensingular.flow.persistence.entity.QProcessInstanceEntity;
import org.opensingular.flow.persistence.entity.QTaskDefinitionEntity;
import org.opensingular.flow.persistence.entity.QTaskInstanceEntity;
import org.opensingular.flow.persistence.entity.QTaskVersionEntity;
import org.opensingular.form.persistence.entity.QFormEntity;
import org.opensingular.form.persistence.entity.QFormTypeEntity;
import org.opensingular.form.persistence.entity.QFormVersionEntity;
import org.opensingular.server.commons.persistence.entity.form.QDraftEntity;
import org.opensingular.server.commons.persistence.entity.form.QFormPetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.QPetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.QPetitionerEntity;

public class RequirementSearchAliases {

    public QPetitionEntity          petition                      = new QPetitionEntity("petition");
    public QPetitionerEntity        petitionerEntity              = new QPetitionerEntity("petitionerEntity");
    public QProcessInstanceEntity   processInstance               = new QProcessInstanceEntity("processInstance");
    public QFormPetitionEntity      formPetitionEntity            = new QFormPetitionEntity("formPetitionEntity");
    public QFormEntity              formEntity                    = new QFormEntity("formEntity");
    public QDraftEntity             currentDraftEntity            = new QDraftEntity("currentDraftEntity");
    public QFormEntity              formDraftEntity               = new QFormEntity("formDraftEntity");
    public QFormVersionEntity       currentFormDraftVersionEntity = new QFormVersionEntity("currentFormDraftVersionEntity");
    public QFormVersionEntity       currentFormVersion            = new QFormVersionEntity("currentFormVersion");
    public QProcessDefinitionEntity processDefinitionEntity       = new QProcessDefinitionEntity("processDefinitionEntity");
    public QModuleEntity      module                  = new QModuleEntity("module");
    public QFormTypeEntity          formType                      = new QFormTypeEntity("formType");
    public QFormTypeEntity          formDraftType                 = new QFormTypeEntity("formDraftType");
    public QTaskInstanceEntity      task                          = new QTaskInstanceEntity("task");
    public QTaskDefinitionEntity    taskDefinition                = new QTaskDefinitionEntity("taskDefinition");
    public QTaskVersionEntity       taskVersion                   = new QTaskVersionEntity("taskVersion");
    public QActor                   allocatedUser                 = new QActor("allocatedUser");

}
