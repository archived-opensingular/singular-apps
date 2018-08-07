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

package org.opensingular.requirement.module.wicket.view.util.history;


import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.renderer.RendererUtil;
import org.opensingular.lib.commons.lambda.IFunction;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.lib.wicket.util.button.DropDownButtonPanel;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.requirement.module.form.FormAction;
import org.opensingular.requirement.module.persistence.dto.RequirementHistoryDTO;
import org.opensingular.requirement.module.persistence.entity.form.FormVersionHistoryEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementContentHistoryEntity;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.wicket.SingularSession;
import org.opensingular.requirement.module.wicket.view.template.ServerTemplate;
import org.opensingular.requirement.module.wicket.view.util.DispatcherPageUtil;
import org.wicketstuff.annotation.mount.MountPath;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.FORM_NAME;
import static org.opensingular.requirement.module.wicket.view.util.ActionContext.FORM_VERSION_KEY;
import static org.opensingular.requirement.module.wicket.view.util.ActionContext.REQUIREMENT_ID;


@MountPath("history")
public class HistoryPage extends ServerTemplate {

    private static final long serialVersionUID = -3344810189307767761L;
    private static final int QUANTIDADE_MAX_TAKS_TO_MIDDLE_SIZE = 5;

    @Inject
    private RequirementService<?, ?> requirementService;

    private Long requirementPK;

    public HistoryPage() {
    }

    public HistoryPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        requirementPK = getPage().getPageParameters().get(REQUIREMENT_ID).toOptionalLong();
        add(setupDataTable(createDataProvider()));
        addImageHistoryFLow();
        add(getBtnFechar());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new PackageResourceReference(HistoryPage.class, "HistoryPage.css")));


    }

    private void addImageHistoryFLow() {
        WebComponent imageHistFlow;
        if (requirementPK != null) {
            String classCss = " col-md-12 ";
            FlowInstance flowInstance = requirementService.getRequirement(requirementPK).getFlowInstance();
            flowInstance.getTasksOlderFirst();
            if (flowInstance.getFlowDefinition().getFlowMap().getAllTasks().size() <= QUANTIDADE_MAX_TAKS_TO_MIDDLE_SIZE) {
                classCss = " col-md-6 col-md-offset-3 ";
            }
            byte[] bytes = generateHistImage(flowInstance);
            DynamicImageResource imageResource = new DynamicImageResource() {
                @Override
                protected byte[] getImageData(IResource.Attributes attributes) {
                    return bytes;
                }
            };
            imageHistFlow = new Image("imageHist", imageResource);
            imageHistFlow.setVisible(bytes.length != 0);
            imageHistFlow.add(new AttributeAppender("class", classCss));
        } else {
            imageHistFlow = new WebComponent("imageHist");
            imageHistFlow.setVisible(false);
        }


        add(imageHistFlow);
    }

    private byte[] generateHistImage(FlowInstance flowInstance) {
        return RendererUtil.findRendererForUserDisplay()
                .map(p -> p.generateHistoryPng(flowInstance))
                .orElse(new byte[0]);
    }

    protected AjaxLink<?> getBtnFechar() {
        return new AjaxLink<Void>("btnVoltar") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onCancelar(target);
            }
        };
    }

    protected void onCancelar(AjaxRequestTarget t) {
        t.appendJavaScript("window.close();");
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

            transient List<RequirementHistoryDTO> cache = null;

            @Override
            public long size() {
                if (requirementPK == null) {
                    cache = Collections.emptyList();
                } else if (cache == null) {
                    cache = getHistoryTasks();
                }
                return cache.size();
            }

            @Override
            public Iterator<RequirementHistoryDTO> iterator(int first, int count, String sortProperty, boolean ascending) {
                if (requirementPK == null) {
                    cache = Collections.emptyList();
                } else if (cache == null) {
                    cache = getHistoryTasks();
                }

                return cache.subList(first, first + count).iterator();
            }
        };
    }

    private List<RequirementHistoryDTO> getHistoryTasks() {
        return requirementService.listRequirementContentHistoryByCodRequirement(requirementPK, showHiddenTasks());
    }

    protected boolean showHiddenTasks() {
        return false;
    }


    protected String getBaseUrl() {
        return RequestCycle.get().getRequest().getContextPath() + SingularSession.get().getServerContext().getUrlPath();
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
