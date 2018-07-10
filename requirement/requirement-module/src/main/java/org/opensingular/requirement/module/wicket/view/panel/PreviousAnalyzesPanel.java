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
import org.opensingular.requirement.module.service.PreviousAnalyzesService;
import org.opensingular.requirement.module.service.dto.AnaliseAnteriorDTO;
import org.opensingular.requirement.module.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.FORM_VERSION_KEY;

public class PreviousAnalyzesPanel extends Panel {

    @Inject
    private PreviousAnalyzesService analiseAnteriorService;

    public PreviousAnalyzesPanel(String id, Long requirementEntityPK) {
        super(id);
        construirTabela(analiseAnteriorService.buscarAnalisesAnteriores(requirementEntityPK));
    }

    private void construirTabela(List<AnaliseAnteriorDTO> analiseAnteriorDTOs) {
        add(new ListView<AnaliseAnteriorDTO>("linhas", analiseAnteriorDTOs) {
            @Override
            protected void populateItem(ListItem<AnaliseAnteriorDTO> item) {
                final AnaliseAnteriorDTO analiseAnteriorDTO = item.getModelObject();
                item.add(new Label("nomeAnalise", Model.of(analiseAnteriorDTO.getName())));
                item.add(new Label("responsavel", Model.of(analiseAnteriorDTO.getActor())));
                item.add(new Label("data", Model.of(analiseAnteriorDTO.getDate())));
                item.add(new ListView<AnaliseAnteriorDTO.TypeFormVersion>("botoes", analiseAnteriorDTO.getTypeFormVersions()) {
                    @Override
                    protected void populateItem(ListItem<AnaliseAnteriorDTO.TypeFormVersion> item) {
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
