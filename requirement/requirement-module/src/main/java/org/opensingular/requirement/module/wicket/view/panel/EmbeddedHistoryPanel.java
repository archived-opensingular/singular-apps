package org.opensingular.requirement.module.wicket.view.panel;

import java.util.List;
import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.opensingular.requirement.module.form.FormAction;
import org.opensingular.requirement.module.service.EmbeddedHistoryService;
import org.opensingular.requirement.module.service.dto.EmbeddedHistoryDTO;
import org.opensingular.requirement.module.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.FORM_VERSION_KEY;

/**
 * This panel will show a history about the analysis of the requirement.
 * This panel is a table containing some information of the requirement history.
 */
public class EmbeddedHistoryPanel extends Panel {

    @Inject
    private EmbeddedHistoryService analiseAnteriorService;

    public EmbeddedHistoryPanel(String id, Long requirementEntityPK) {
        super(id);
        buildTable(analiseAnteriorService.buscarAnalisesAnteriores(requirementEntityPK));
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
                item.add(new Label("data", Model.of(embeddedHistoryDTO.getDate())));
                item.add(new ListView<EmbeddedHistoryDTO.TypeFormVersion>("botoes", embeddedHistoryDTO.getTypeFormVersions()) {
                    @Override
                    protected void populateItem(ListItem<EmbeddedHistoryDTO.TypeFormVersion> item) {
                        item.add(appendViewFormButton("button", item.getModelObject().getFormVersionPK(), item.getModelObject().getLabel()));
                    }
                });
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
