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

package org.opensingular.requirement.module.wicket.view.panel;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.opensingular.requirement.module.form.FormAction;
import org.opensingular.requirement.module.service.EmbeddedHistoryService;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.dto.EmbeddedHistoryDTO;
import org.opensingular.requirement.module.wicket.view.util.DispatcherPageUtil;

import javax.inject.Inject;
import java.util.List;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.FORM_VERSION_KEY;

/**
 * This panel will show a history about the analysis of the requirement.
 * This panel is a table containing some information of the requirement history.
 */
public class EmbeddedHistoryPanel extends Panel {

    @Inject
    private EmbeddedHistoryService analiseAnteriorService;

    public EmbeddedHistoryPanel(String id, RequirementInstance<?, ?> requirement) {
        super(id);
        buildTable(getEmbeddedHistoryDTOS(requirement));
    }

    private List<EmbeddedHistoryDTO> getEmbeddedHistoryDTOS(RequirementInstance<?, ?> requirementInstance) {
        List<EmbeddedHistoryDTO> hists = Lists.newArrayList(analiseAnteriorService.buscarAnalisesAnteriores(requirementInstance.getCod()));
        hists.add(0, analiseAnteriorService.findMainFormFirstVersion(requirementInstance.getCod()));
        return hists;
    }

    /**
     * Method responsible for create the table that contains some information about the requirement history.
     *
     * @param embeddedHistoryDTOS Dto that contains the information about the requiriment.
     */
    private void buildTable(List<EmbeddedHistoryDTO> embeddedHistoryDTOS) {
        add(new ListView<EmbeddedHistoryDTO>("linhas", embeddedHistoryDTOS) {
            @Override
            protected void populateItem(ListItem<EmbeddedHistoryDTO> item) {
                final EmbeddedHistoryDTO embeddedHistoryDTO = item.getModelObject();
                item.add(new Label("nomeAnalise", Model.of(embeddedHistoryDTO.getName())));
                item.add(new Label("responsavel", Model.of(embeddedHistoryDTO.getActor())));
                item.add(new Label("executedTransition", Model.of(embeddedHistoryDTO.getExecutedTransition())));
                item.add(new Label("data", Model.of(embeddedHistoryDTO.getDate())));

                WebMarkupContainer formulariosBtn = new WebMarkupContainer("formulariosBtn");
                formulariosBtn.setVisible(CollectionUtils.isNotEmpty(item.getModelObject().getTypeFormVersions()));

                formulariosBtn.add(new ListView<EmbeddedHistoryDTO.TypeFormVersion>("botoes", embeddedHistoryDTO.getTypeFormVersions()) {
                    @Override
                    protected void populateItem(ListItem<EmbeddedHistoryDTO.TypeFormVersion> item) {
                        item.add(appendViewFormButton("button", item.getModelObject().getFormVersionPK(), item.getModelObject().getLabel()));
                    }
                });

                item.add(formulariosBtn);
            }
        });
    }

    private Button appendViewFormButton(final String idButton, final Long versionPK, final String label) {

        final String url = DispatcherPageUtil
                .baseURL(DispatcherPageUtil.getBaseURL())
                .formAction(FormAction.FORM_ANALYSIS_VIEW.getId())
                .requirementId(null)
                .param(FORM_VERSION_KEY, versionPK)
                .build();

        final Button viewButton = new Button(idButton) {
            @Override
            protected String getOnClickScript() {
                return ";var newtab = window.open('" + url + "'); newtab.opener=null;";
            }
        };

        viewButton.add(new Label("button-label", label));

        return viewButton;
    }
}
