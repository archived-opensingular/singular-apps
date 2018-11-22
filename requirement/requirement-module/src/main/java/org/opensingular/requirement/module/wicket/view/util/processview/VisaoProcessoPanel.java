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

package org.opensingular.requirement.module.wicket.view.util.processview;


import com.google.gson.Gson;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.renderer.FlowRendererProviderExtension;
import org.opensingular.lib.commons.extension.SingularExtensionUtil;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.wicket.view.panel.EmbeddedHistoryPanel;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisaoProcessoPanel extends Panel {

    private final Form<Void> form;

    private static final int QUANTIDADE_MAX_TAKS_TO_MIDDLE_SIZE = 5;


    @Inject
    private RequirementService requirementService;

    @Override
    protected boolean getStatelessHint() {
        return false;
    }

    public VisaoProcessoPanel(String id, Long rootRequirementEntityPK, Long currentRequirement) {
        super(id);
        this.form = new Form<>("form");
        add(form);
        form.add(buildImageFlow(rootRequirementEntityPK));
        form.add(new EmbeddedHistoryPanel("historico", rootRequirementEntityPK));

        final RequirementInstance<?, ?> requirement = requirementService.loadRequirementInstance(rootRequirementEntityPK);

        addOrReplaceDadosRequerimento(requirement);

        VisaoProcessoDTO root = new VisaoProcessoDTO();
        root.text = requirement.getDescription();
        root.requirementCod = rootRequirementEntityPK;

        for (RequirementInstance<?, ?> child : requirement.getChildrenRequirements()) {
            VisaoProcessoDTO node = new VisaoProcessoDTO();
            node.text = child.getDescription();
            node.requirementCod = child.getCod();
            root.addChild(node);
        }

        form.add(new AbstractDefaultAjaxBehavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                String init = "";
                init += "$('#tree').jstree({                                                       ";
                init += "    'core' : {                                                            ";
                init += "        'data' : " + new Gson().toJson(Collections.singletonList(root)) + ",";
                init += "        'themes' : {                                                      ";
                init += "            'responsive': false                                           ";
                init += "        }                                                                 ";
                init += "    },                                                                    ";
                init += "    'types' : {                                                           ";
                init += "        'default' : {                                                     ";
                init += "            'icon' : 'fa fa-folder icon-state-warning icon-lg'            ";
                init += "        },                                                                ";
                init += "        'file' : {                                                        ";
                init += "            'icon' : 'fa fa-file icon-state-warning icon-lg'              ";
                init += "        }                                                                 ";
                init += "    },                                                                    ";
                init += "    'plugins': ['types']                                                  ";
                init += "})                                                                        ";
                init += ".on('loaded.jstree', function() {                                         ";
                init += "    $('#tree').jstree('open_all');                                        ";
                init += "  });                                                                       ";
                response.render(OnDomReadyHeaderItem.forScript(init));

                String updater = "";
                updater += "$('#tree').on('changed.jstree', function(event, data) {";
                updater += "    Wicket.Ajax.ajax({'u':'" + this.getCallbackUrl() + "&requirementCod='+data.instance.get_selected(true)[0].original.requirementCod});";
                updater += "});";
                response.render(OnDomReadyHeaderItem.forScript(updater));
            }

            @Override
            protected void respond(AjaxRequestTarget target) {
                final long                 requirementCod = getRequest().getRequestParameters().getParameterValue("requirementCod").toLong();
                final EmbeddedHistoryPanel historico      = new EmbeddedHistoryPanel("historico", requirementCod);
                form.replace(historico);
                target.add(historico);
                target.add(addOrReplaceDadosRequerimento(requirementService.loadRequirementInstance(requirementCod)));
            }
        });

    }


    private WebComponent buildImageFlow(Long requirementPK) {
        WebComponent imageHistFlow;
        if (requirementPK != null) {
            String       classCss     = " col-md-12 ";
            FlowInstance flowInstance = requirementService.loadRequirementInstance(requirementPK).getFlowInstance();
            flowInstance.getTasksOlderFirst();
            if (flowInstance.getFlowDefinition().getFlowMap().getAllTasks().size() <= QUANTIDADE_MAX_TAKS_TO_MIDDLE_SIZE) {
                classCss = " col-md-6 col-md-offset-3 ";
            }
            byte[] bytes = generateHistImage(flowInstance);
            DynamicImageResource imageResource = new DynamicImageResource() {
                @Override
                protected byte[] getImageData(Attributes attributes) {
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

        return imageHistFlow;
    }

    private byte[] generateHistImage(FlowInstance flowInstance) {
        return SingularExtensionUtil.get()
                .findExtensions(FlowRendererProviderExtension.class)
                .stream()
                .findFirst()
                .map(p -> p.getRenderer().generateHistoryPng(flowInstance))
                .orElse(new byte[0]);
    }


    public static class VisaoProcessoDTO implements Serializable {
        private String                 text;
        private Long                   requirementCod;
        private List<VisaoProcessoDTO> children;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<VisaoProcessoDTO> getChildren() {
            return children;
        }

        public void setChildren(List<VisaoProcessoDTO> children) {
            this.children = children;
        }

        public void addChild(VisaoProcessoDTO child) {
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(child);
        }


        public Long getRequirementCod() {
            return requirementCod;
        }

        public void setRequirementCod(Long requirementCod) {
            this.requirementCod = requirementCod;
        }

    }

    private WebMarkupContainer addOrReplaceDadosRequerimento(RequirementInstance r) {
        WebMarkupContainer dadosRequerimento = new WebMarkupContainer("dadosRequerimento");
        dadosRequerimento.add(new Label("descricao", Model.of(r.getDescription())));
        dadosRequerimento.add(new Label("protocolo", Model.of(r.getCod())));
        dadosRequerimento.add(new Label("dataEntrada", Model.of(r.getFlowInstance().getBeginDate())));
        dadosRequerimento.add(new Label("situacaoAtual", Model.of(r.getCurrentTaskNameOrException())));
        dadosRequerimento.add(new Label("solicitante", Model.of(r.getApplicantName())));
        form.addOrReplace(dadosRequerimento);
        return dadosRequerimento;
    }

}
