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

package org.opensingular.server.commons.wicket.historico;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.lib.wicket.util.button.DropDownButtonPanel;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.form.FormActions;
import org.opensingular.server.commons.persistence.dto.PetitionHistoryDTO;
import org.opensingular.server.commons.persistence.entity.form.FormVersionHistoryEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionContentHistoryEntity;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.util.Parameters;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.view.template.Content;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static org.opensingular.server.commons.util.Parameters.FORM_VERSION_KEY;

public abstract class AbstractHistoricoContent extends Content {

    private static final long serialVersionUID = 8587873133590041152L;

    @Inject
    private PetitionService<?> petitionService;

    private int    instancePK;
    private long   petitionPK;
    private String processGroupPK;

    public AbstractHistoricoContent(String id) {
        super(id);
    }

    @Override
    protected IModel<?> getContentTitleModel() {
        return new ResourceModel("label.historico.title");
    }

    @Override
    protected IModel<?> getContentSubtitleModel() {
        return null;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        petitionPK = getPage().getPageParameters().get(Parameters.PETITION_ID).toLong();
        instancePK = getPage().getPageParameters().get(Parameters.INSTANCE_ID).toInt();
        processGroupPK = getPage().getPageParameters().get(Parameters.PROCESS_GROUP_PARAM_NAME).toString();
        queue(setupDataTable(createDataProvider()));
        queue(getBtnCancelar());
    }

    protected AjaxLink<?> getBtnCancelar() {
        return new AjaxLink<Void>("btnVoltar") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onCancelar(target);
            }
        };
    }

    protected abstract void onCancelar(AjaxRequestTarget t);

    protected BSDataTable<PetitionHistoryDTO, String> setupDataTable(BaseDataProvider<PetitionHistoryDTO, String> dataProvider) {
        return new BSDataTableBuilder<>(dataProvider)
                .appendPropertyColumn(
                        getMessage("label.table.column.task.name"),
                        p -> p.getTask().getTask().getName()
                )
                .appendPropertyColumn(
                        getMessage("label.table.column.begin.date"),
                        p -> p.getTask().getBeginDate()
                )
                .appendPropertyColumn(
                        getMessage("label.table.column.end.date"),
                        p -> p.getTask().getEndDate()
                )
                .appendPropertyColumn(
                        getMessage("label.table.column.allocated.user"),
                        p -> Optional.of(p).map(PetitionHistoryDTO::getTask).map(TaskInstanceEntity::getAllocatedUser).map(Actor::getNome).orElse("")
                )
//                .appendActionColumn(
//                        Model.of(""),
//                        column -> column.appendComponentFactory((id, model) -> {
//
//                            final DropDownButtonPanel dropDownButtonPanel;
//
//                            dropDownButtonPanel = new DropDownButtonPanel(id)
//                                    .setDropdownLabel(Model.of("Formularios"))
//                                    .setInvisibleIfEmpty(Boolean.TRUE)
//                                    .setPullRight(Boolean.TRUE);
//
//                            Optional.of(model.getObject())
//                                    .map(PetitionHistoryDTO::getPetitionContentHistory)
//                                    .map(PetitionContentHistoryEntity::getFormVersionHistoryEntities)
//                                    .ifPresent(list -> {
//                                        list.forEach(fvh -> {
//                                            dropDownButtonPanel
//                                                    .addButton(Model.of(fvh.getFormVersion().getFormEntity().getFormType().getLabel()), viewFormButton(fvh.getCodFormVersion()));
//                                        });
//                                    });
//
//                            return dropDownButtonPanel;
//                        })
//                )
                .build("tabela");
    }

//    private IFunction<String, Button> viewFormButton(final Long versionPK) {
//        final String url = DispatcherPageUtil
//                .baseURL(getBaseUrl())
//                .formAction(FormActions.FORM_ANALYSIS_VIEW.getId())
//                .formId(null)
//                .param(FORM_VERSION_KEY, versionPK)
//                .build();
//        return id -> new Button(id) {
//            @Override
//            protected String getOnClickScript() {
//                return ";var newtab = window.open('" + url + "'); newtab.opener=null;";
//            }
//        };
//    }

    protected Map<String, String> buildViewFormParameters(IModel<PetitionHistoryDTO> model) {
        final Map<String, String> params = new HashMap<>();
        if (model.getObject().getPetitionContentHistory() != null) {
            params.put(Parameters.FORM_VERSION_KEY, model
                    .getObject()
                    .getPetitionContentHistory()
                    .getFormVersionHistoryEntities()
                    .stream()
                    .filter(f -> SimNao.SIM.equals(f.getMainForm()))
                    .findFirst()
                    .map(FormVersionHistoryEntity::getCodFormVersion)
                    .map(Object::toString)
                    .orElse(null));
        }
        return params;
    }

    protected BaseDataProvider<PetitionHistoryDTO, String> createDataProvider() {
        return new BaseDataProvider<PetitionHistoryDTO, String>() {
            @Override
            public long size() {
                return petitionService.listPetitionContentHistoryByPetitionCod(petitionPK).size();
            }

            @Override
            public Iterator<PetitionHistoryDTO> iterator(int first, int count, String sortProperty, boolean ascending) {
                return petitionService.listPetitionContentHistoryByPetitionCod(petitionPK).iterator();
            }
        };
    }

    protected String getBaseUrl() {
        return getModuleContext() + SingularSession.get().getServerContext().getUrlPath();
    }

    public String getModuleContext() {
        final String groupConnectionURL = petitionService.findByProcessGroupCod(processGroupPK).getConnectionURL();
        try {
            final String path = new URL(groupConnectionURL).getPath();
            return path.substring(0, path.indexOf("/", 1));
        } catch (Exception e) {
            throw new SingularServerException(String.format("Erro ao tentar fazer o parse da URL: %s", groupConnectionURL), e);
        }
    }

}
