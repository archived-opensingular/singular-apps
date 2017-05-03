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

package org.opensingular.server.commons.persistence.dto;


import org.opensingular.flow.core.SUser;
import org.opensingular.flow.core.TaskType;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.service.dto.BoxItemAction;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class TaskInstanceDTO implements Serializable {

    private Integer taskInstanceId;
    private Integer versionStamp;
    private Integer processInstanceId;
    private Integer taskId;
    private String taskName;
    private Date creationDate;
    private String description;
    private String codUsuarioAlocado;
    private String nomeUsuarioAlocado;
    private String type;
    private String processType;
    private Long codPeticao;
    private Date situationBeginDate;
    private Date processBeginDate;
    private TaskType taskType;
    private boolean possuiPermissao = true;
    private String processGroupCod;
    private String processGroupContext;
    private List<BoxItemAction> actions;


    public TaskInstanceDTO(Integer processInstanceId, Integer taskInstanceId, Integer taskId, Integer versionStamp,
                           Date creationDate, String descricao,
                           SUser usuarioAlocado, String taskName, String type, String processType, Long codPeticao,
                           Date situationBeginDate, Date processBeginDate, TaskType taskType, String processGroupCod, String processGroupContext) {
        setProcessInstanceId(processInstanceId);
        setTaskInstanceId(taskInstanceId);
        setTaskId(taskId);
        setVersionStamp(versionStamp);
        setCreationDate(creationDate);
        setDescription(descricao);
        setCodUsuarioAlocado(usuarioAlocado == null ? null : usuarioAlocado.getCodUsuario());
        setNomeUsuarioAlocado(usuarioAlocado == null ? null : usuarioAlocado.getSimpleName());
        setTaskName(taskName);
        setCodPeticao(codPeticao);
        setType(type);
        setProcessType(processType);
        setSituationBeginDate(situationBeginDate);
        setProcessBeginDate(processBeginDate);
        setTaskType(taskType);
        setProcessGroupCod(processGroupCod);
        try {
            final String path = new URL(processGroupContext).getPath();
            setProcessGroupContext(path.substring(0, path.indexOf('/', 1)));
        } catch (Exception e) {
            throw SingularServerException.rethrow(String.format("Erro ao tentar fazer o parse da URL: %s", processGroupContext), e);
        }
    }


    public TaskInstanceDTO() {
    }

    public Integer getTaskInstanceId() {
        return taskInstanceId;
    }


    public void setTaskInstanceId(Integer taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
    }


    public Integer getTaskId() {
        return taskId;
    }


    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodUsuarioAlocado() {
        return codUsuarioAlocado;
    }

    public void setCodUsuarioAlocado(String codUsuarioAlocado) {
        this.codUsuarioAlocado = codUsuarioAlocado;
    }

    public String getNomeUsuarioAlocado() {
        return nomeUsuarioAlocado;
    }


    public void setNomeUsuarioAlocado(String nomeUsuarioAlocado) {
        this.nomeUsuarioAlocado = nomeUsuarioAlocado;
    }


    public String getTaskName() {
        return taskName;
    }


    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public Long getCodPeticao() {
        return codPeticao;
    }


    public void setCodPeticao(Long codPeticao) {
        this.codPeticao = codPeticao;
    }


    public Integer getProcessInstanceId() {
        return processInstanceId;
    }


    public void setProcessInstanceId(Integer processInstanceId) {
        this.processInstanceId = processInstanceId;
    }


    public Date getSituationBeginDate() {
        return situationBeginDate;
    }


    public void setSituationBeginDate(Date situationBeginDate) {
        this.situationBeginDate = situationBeginDate;
    }


    public Date getProcessBeginDate() {
        return processBeginDate;
    }


    public void setProcessBeginDate(Date processBeginDate) {
        this.processBeginDate = processBeginDate;
    }


    public TaskType getTaskType() {
        return taskType;
    }


    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }


    public boolean isPossuiPermissao() {
        return possuiPermissao;
    }


    public void setPossuiPermissao(boolean possuiPermissao) {
        this.possuiPermissao = possuiPermissao;
    }


    public String getProcessType() {
        return processType;
    }


    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public Integer getVersionStamp() {
        return versionStamp;
    }

    public void setVersionStamp(Integer versionStamp) {
        this.versionStamp = versionStamp;
    }

    public String getProcessGroupCod() {
        return processGroupCod;
    }

    public void setProcessGroupCod(String processGroupCod) {
        this.processGroupCod = processGroupCod;
    }

    public String getProcessGroupContext() {
        return processGroupContext;
    }

    public void setProcessGroupContext(String processGroupContext) {
        this.processGroupContext = processGroupContext;
    }

    public List<BoxItemAction> getActions() {
        return actions;
    }

    public void setActions(List<BoxItemAction> actions) {
        this.actions = actions;
    }
}
