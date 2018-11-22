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

package org.opensingular.requirement.module.wicket.view.util.history.processview;


import com.google.gson.Gson;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.opensingular.flow.core.FlowInstance;
import org.opensingular.flow.core.renderer.RendererUtil;
import org.opensingular.lib.wicket.util.image.PhotoSwipeBehavior;
import org.opensingular.lib.wicket.util.image.PhotoSwipePanel;
import org.opensingular.requirement.module.service.RequirementInstance;
import org.opensingular.requirement.module.service.RequirementService;
import org.opensingular.requirement.module.wicket.view.panel.EmbeddedHistoryPanel;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.opensingular.lib.wicket.util.util.Shortcuts.$m;

public class ProcessViewPanel extends Panel {

    private WebMarkupContainer     historyPanel = new WebMarkupContainer("historyPanel");
    private IModel<ProcessViewDTO> processTree  = new Model<>();
    private PhotoSwipePanel        gallery;

    private static final String IMAGE_HIST_ID = "imageHist";

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new PackageResourceReference(ProcessViewPanel.class, "ProcessViewPanel.css")));

    }

    @Inject
    private RequirementService requirementService;

    @Override
    protected boolean getStatelessHint() {
        return false;
    }

    public ProcessViewPanel(String id, Long rootRequirementEntityPK, Long currentRequirementPK) {
        super(id);
        final RequirementInstance<?, ?> requirement        = requirementService.loadRequirementInstance(rootRequirementEntityPK);
        final RequirementInstance<?, ?> currentRequirement = requirementService.loadRequirementInstance(currentRequirementPK);
        Form<Void>                      form               = new Form<>("form");
        this.add(form);
        WebMarkupContainer treePanel = new WebMarkupContainer("treePanel");
        form.add(treePanel);
        form.add(historyPanel);
        buildHistoryPanel(currentRequirement);

        processTree.setObject(getProcessViewDTO(requirement, new ProcessViewDTO()));

        treePanel.add(new Behavior() {
            @Override
            public void onConfigure(Component component) {
                if (processTree.getObject().getChildren().isEmpty()) {
                    component.setVisible(false);
                    component.add(new AttributeModifier("class", "hidden-md"));
                } else {
                    component.setVisible(true);
                    component.add(new AttributeModifier("class", "col-md-3"));
                }
            }
        });


        form.add(new AbstractDefaultAjaxBehavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                String init = " ";
                init += "$('#tree').jstree({                                                       ";
                init += "    'core' : {                                                            ";
                init += "        'data' : " + new Gson().toJson(Collections.singletonList(processTree.getObject())) + ",";
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
                init += "    $('#tree').jstree('select_node', '" + currentRequirementPK + "');                       ";
                init += "  });                                                                     ";
                response.render(OnDomReadyHeaderItem.forScript(init));

                String updater = "";
                updater += "$('#tree').on('changed.jstree', function(event, data) {";
                updater += "    Wicket.Ajax.ajax({'u':'" + this.getCallbackUrl() + "&requirementCod='+data.instance.get_selected(true)[0].original.id});";
                updater += "});";
                response.render(OnDomReadyHeaderItem.forScript(updater));
            }

            @Override
            protected void respond(AjaxRequestTarget target) {
                final long                requirementCod      = getRequest().getRequestParameters().getParameterValue("requirementCod").toLong();
                RequirementInstance<?, ?> requirementInstance = requirementService.loadRequirementInstance(requirementCod);
                buildHistoryPanel(requirementInstance);
                target.add(historyPanel);
            }
        });

    }

    private void buildHistoryPanel(RequirementInstance<?, ?> requirement) {
        historyPanel.addOrReplace(newFlowPanel(requirement));
        historyPanel.addOrReplace(new EmbeddedHistoryPanel("historico", requirement.getCod()));
        historyPanel.addOrReplace(newDadosRequerimento(requirement));

        historyPanel.add(new Behavior() {
            @Override
            public void onConfigure(Component component) {
                if (processTree.getObject().getChildren().isEmpty()) {
                    component.add(new AttributeModifier("class", "col-lg-12"));
                } else {
                    component.add(new AttributeModifier("class", "col-lg-9"));
                }
            }
        });
    }

    private WebMarkupContainer newFlowPanel(RequirementInstance<?, ?> requirementInstance) {
        WebComponent component = buildImageFlow(IMAGE_HIST_ID, requirementInstance);
        WebMarkupContainer flowPanel = new WebMarkupContainer("flowPanel") {
            @Override
            public boolean isVisible() {
                return component.isVisible();
            }
        };
        flowPanel.add(component);
        gallery = new PhotoSwipePanel("gallery", PhotoSwipeBehavior.forImages($m.get(() -> {
            Component img = component;
            return (img instanceof Image)
                    ? new Image[]{(Image) img}
                    : new Image[0];
        })));
        flowPanel.add(gallery);
        return flowPanel;
    }

    private ProcessViewDTO getProcessViewDTO(RequirementInstance<?, ?> requirement, ProcessViewDTO node) {
        node.text = requirement.getDescription();
        node.id = requirement.getCod();

        for (RequirementInstance<?, ?> child : requirement.getChildrenRequirements()) {
            node.addChild(getProcessViewDTO(child, new ProcessViewDTO()));
        }

        return node;
    }


    private WebComponent buildImageFlow(String id, RequirementInstance<?, ?> requirementInstance) {
        WebComponent imageHistFlow;
        if (requirementInstance != null) {
            FlowInstance flowInstance = requirementInstance.getFlowInstance();
            flowInstance.getTasksOlderFirst();
            byte[] bytes = generateHistImage(flowInstance);
            DynamicImageResource imageResource = new DynamicImageResource() {
                @Override
                protected byte[] getImageData(Attributes attributes) {
                    return bytes;
                }
            };
            imageHistFlow = new Image(id, imageResource);
            imageHistFlow.setVisible(bytes.length != 0);
            imageHistFlow.add(new AjaxEventBehavior("click") {
                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    gallery.setVisible(true);
                    target.add(gallery);
                }
            });
        } else {
            imageHistFlow = new WebComponent(id);
            imageHistFlow.setVisible(false);
        }

        return imageHistFlow;
    }

    private byte[] generateHistImage(FlowInstance flowInstance) {
        return RendererUtil
                .findRendererForUserDisplay()
                .map(p -> p.generateHistoryPng(flowInstance))
                .orElse(new byte[0]);
    }


    public static class ProcessViewDTO implements Serializable {
        private String               text;
        private Long                 id;
        private List<ProcessViewDTO> children = new ArrayList<>();

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<ProcessViewDTO> getChildren() {
            return children;
        }

        public void setChildren(List<ProcessViewDTO> children) {
            this.children = children;
        }

        public void addChild(ProcessViewDTO child) {
            children.add(child);
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

    }

    private WebMarkupContainer newDadosRequerimento(RequirementInstance r) {
        WebMarkupContainer dadosRequerimento = new WebMarkupContainer("dadosRequerimento");
        dadosRequerimento.add(new Label("descricao", Model.of(r.getDescription())));
        dadosRequerimento.add(new Label("protocolo", Model.of(r.getCod())));
        dadosRequerimento.add(new Label("dataEntrada", Model.of(r.getFlowInstance().getBeginDate())));
        dadosRequerimento.add(new Label("situacaoAtual", Model.of(r.getCurrentTaskNameOrException())));
        dadosRequerimento.add(new Label("solicitante", Model.of(r.getApplicantName())));
        return dadosRequerimento;
    }

}
