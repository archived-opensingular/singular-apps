package org.opensingular.requirement.module.wicket.view.util.history.processview;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.opensingular.requirement.module.service.RequirementInstance;

public class InfoHistoryPanel extends Panel {

    public InfoHistoryPanel(String id, RequirementInstance requirementInstance) {
        super(id);
        createInfoOfRequirementHistory(requirementInstance);
    }

    private void createInfoOfRequirementHistory(RequirementInstance r) {
        add(new Label("descricao", Model.of(r.getDescription())));
        add(new Label("protocolo", Model.of(r.getCod())));
        add(new Label("dataEntrada", Model.of(r.getFlowInstance().getBeginDate())));
        add(new Label("situacaoAtual", Model.of(r.getCurrentTaskNameOrException())));
        add(new Label("solicitante", Model.of(r.getApplicantName())));
    }
}
