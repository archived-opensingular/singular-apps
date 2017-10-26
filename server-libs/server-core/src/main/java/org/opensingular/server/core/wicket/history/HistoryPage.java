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

package org.opensingular.server.core.wicket.history;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.lib.wicket.util.button.DropDownButtonPanel;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.persistence.dto.RequirementHistoryDTO;
import org.opensingular.server.commons.persistence.entity.form.FormVersionHistoryEntity;
import org.opensingular.server.commons.persistence.entity.form.RequirementContentHistoryEntity;
import org.opensingular.server.commons.service.RequirementService;
import org.opensingular.server.commons.wicket.SingularSession;
import org.opensingular.server.commons.wicket.view.template.ServerTemplate;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;
import org.wicketstuff.annotation.mount.MountPath;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.opensingular.server.commons.wicket.view.util.ActionContext.FORM_NAME;
import static org.opensingular.server.commons.wicket.view.util.ActionContext.FORM_VERSION_KEY;
import static org.opensingular.server.commons.wicket.view.util.ActionContext.MENU_PARAM_NAME;
import static org.opensingular.server.commons.wicket.view.util.ActionContext.MODULE_PARAM_NAME;
import static org.opensingular.server.commons.wicket.view.util.ActionContext.REQUIREMENT_ID;


@MountPath("history")
public class HistoryPage extends ServerTemplate {

    private static final long serialVersionUID = -3344810189307767761L;

    @Inject
    private RequirementService<?, ?> requirementService;

    private long requirementPK;
    private String modulePK;
    private String menu;

    public HistoryPage() {
    }

    public HistoryPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        requirementPK = getPage().getPageParameters().get(REQUIREMENT_ID).toLong();
        modulePK = getPage().getPageParameters().get(MODULE_PARAM_NAME).toString();
        menu = getPage().getPageParameters().get(MENU_PARAM_NAME).toString();
        add(setupDataTable(createDataProvider()));
        add(getBtnCancelar());
    }

    protected AjaxLink<?> getBtnCancelar() {
        return new AjaxLink<Void>("btnVoltar") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onCancelar(target);
            }
        };
    }

    protected void onCancelar(AjaxRequestTarget t) {
        t.appendJavaScript("window.history.go(-1);");
    }

    protected BSDataTable<RequirementHistoryDTO, String> setupDataTable(BaseDataProvider<RequirementHistoryDTO, String> dataProvider) {
        return new BSDataTableBuilder<>(dataProvider)
                .appendPropertyColumn(
                        getMessage("label.table.column.task.name"),
                        p -> p.getTaskName()
                )
                .appendPropertyColumn(
                        getMessage("label.table.column.begin.date"),
                        p -> p.getBeginDate()
                )
                .appendPropertyColumn(
                        getMessage("label.table.column.end.date"),
                        p -> p.getEndDate()
                )
                .appendPropertyColumn(
                        getMessage("label.table.column.allocated.user"),
                        p -> p.getAllocatedUser()
                )
                .appendActionColumn(
                        Model.of(""),
                        column -> column.appendComponentFactory((id, model) -> {

                            final DropDownButtonPanel dropDownButtonPanel;

                            dropDownButtonPanel = new DropDownButtonPanel(id)
                                    .setDropdownLabel(Model.of("Formulários"))
                                    .setInvisibleIfEmpty(Boolean.TRUE)
                                    .setPullRight(Boolean.TRUE);

                            Optional.of(model.getObject())
                                    .map(RequirementHistoryDTO::getRequirementContentHistory)
                                    .map(RequirementContentHistoryEntity::getFormVersionHistoryEntities)
                                    .ifPresent(list -> list.forEach(fvh -> dropDownButtonPanel
                                            .addButton(Model.of(fvh.getFormVersion().getFormEntity().getFormType().getLabel()), viewFormButton(fvh))));

                            return dropDownButtonPanel;
                        })
                )
                .build("tabela");
    }

    private IFunction<String, Button> viewFormButton(final FormVersionHistoryEntity formVersionHistoryEntity) {
        final String url = DispatcherPageUtil
                .baseURL(getBaseUrl())
                .formAction(FormAction.FORM_ANALYSIS_VIEW.getId())
                .requirementId(null)
                .param(FORM_NAME, formVersionHistoryEntity.getFormVersion().getFormEntity().getFormType().getAbbreviation())
                .param(FORM_VERSION_KEY, formVersionHistoryEntity.getCod().getCodFormVersion())
                .build();
        return id -> new Button(id) {
            @Override
            protected String getOnClickScript() {
                return ";var newtab = window.open('" + url + "'); newtab.opener=null;";
            }
        };
    }

    protected Map<String, String> buildViewFormParameters(IModel<RequirementHistoryDTO> model) {
        final Map<String, String> params = new HashMap<>();
        if (model.getObject().getRequirementContentHistory() != null) {
            params.put(FORM_VERSION_KEY, model
                    .getObject()
                    .getRequirementContentHistory()
                    .getFormVersionHistoryEntities()
                    .stream()
                    .filter(f -> SimNao.SIM == f.getMainForm())
                    .findFirst()
                    .map(FormVersionHistoryEntity::getCodFormVersion)
                    .map(Object::toString)
                    .orElse(null));
        }
        return params;
    }

    protected BaseDataProvider<RequirementHistoryDTO, String> createDataProvider() {
        return new BaseDataProvider<RequirementHistoryDTO, String>() {

            transient List<RequirementHistoryDTO> cache = requirementService.listRequirementContentHistoryByCodRequirement(
                    requirementPK, menu, isFilterAllowedHistoryTasks());

            @Override
            public long size() {
                if (cache == null) {
                    cache = requirementService.listRequirementContentHistoryByCodRequirement(requirementPK, menu, isFilterAllowedHistoryTasks());
                }
                return cache.size();
            }

            @Override
            public Iterator<RequirementHistoryDTO> iterator(int first, int count, String sortProperty, boolean ascending) {
                if (cache == null) {
                    cache = requirementService.listRequirementContentHistoryByCodRequirement(requirementPK, menu, isFilterAllowedHistoryTasks());
                }
                return cache.subList(first, first + count).iterator();
            }
        };
    }

    protected boolean isFilterAllowedHistoryTasks() {
        return false;
    }

    protected String getBaseUrl() {
        return getModuleContext() + SingularSession.get().getServerContext().getUrlPath();
    }

    public String getModuleContext() {
        final String groupConnectionURL = requirementService.findByModuleCod(modulePK).getConnectionURL();
        try {
            final String path = new URL(groupConnectionURL).getPath();
            int indexOf = path.indexOf('/', 1);
            if(indexOf > 0) {
                return path.substring(0, indexOf);
            }
            return groupConnectionURL;
        } catch (Exception e) {
            throw SingularServerException.rethrow(String.format("Erro ao tentar fazer o parse da URL: %s", groupConnectionURL), e);
        }
    }


    @Override
    protected IModel<String> getContentTitle() {
        return new ResourceModel("label.historico.title");
    }

    @Override
    protected IModel<String> getContentSubtitle() {
        return new Model<>();
    }

    @Override
    protected boolean isWithMenu() {
        return false;
    }

}
