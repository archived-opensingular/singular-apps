package org.opensingular.singular.server.p.core.wicket.entrada;


import org.opensingular.form.wicket.enums.AnnotationMode;
import org.opensingular.form.wicket.enums.ViewMode;
import org.opensingular.singular.server.commons.form.FormActions;
import org.opensingular.singular.server.commons.persistence.dto.PeticaoDTO;
import org.opensingular.singular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.singular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.singular.server.commons.service.PetitionService;
import org.opensingular.singular.server.commons.wicket.view.util.DispatcherPageUtil;
import org.opensingular.singular.server.p.core.wicket.view.AbstractPeticaoCaixaContent;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.column.BSActionColumn;
import org.opensingular.lib.wicket.util.resource.Icone;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import javax.inject.Inject;
import java.util.List;

import static org.opensingular.singular.server.commons.util.Parameters.SIGLA_FORM_NAME;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;
import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class EntradaContent extends AbstractPeticaoCaixaContent<PeticaoDTO> {

    @Inject
    protected PetitionService<PetitionEntity> peticaoService;

    public EntradaContent(String id, String processGroupCod, String siglaProcesso) {
        super(id, processGroupCod, siglaProcesso);
    }

    @Override
    public QuickFilter montarFiltroBasico() {
        return new QuickFilter()
                .withFilter(getFiltroRapidoModelObject())
                .withRascunho(false);
    }

    @Override
    protected long countQuickSearch(QuickFilter filter, List<String> processesNames, List<String> formNames) {
        return peticaoService.countQuickSearch(filter, processesNames, formNames);
    }

    @Override
    protected List<PeticaoDTO> quickSearch(QuickFilter filtro, List<String> siglasProcesso, List<String> formNames) {
        return peticaoService.quickSearch(filtro, siglasProcesso, formNames);
    }

    @Override
    protected void appendPropertyColumns(BSDataTableBuilder<PeticaoDTO, String, IColumn<PeticaoDTO, String>> builder) {
//        builder.appendPropertyColumn(getMessage("label.table.column.process.number"), "t.numeroProcesso", IPetitionDTO::getProcessNumber);
        builder.appendPropertyColumn(getMessage("label.table.column.process"), "p.processName", PeticaoDTO::getProcessName);
        builder.appendPropertyColumn(getMessage("label.table.column.in.date"), "pie.beginDate", PeticaoDTO::getProcessBeginDate);
        builder.appendPropertyColumn(getMessage("label.table.column.situation"), "task.name", PeticaoDTO::getSituation);
        builder.appendPropertyColumn(getMessage("label.table.column.situation.date"), "ct.beginDate", PeticaoDTO::getSituationBeginDate);
    }

    @Override
    protected Pair<String, SortOrder> getSortProperty() {
        return Pair.of("pie.beginDate", SortOrder.DESCENDING);
    }

    @Override
    protected IModel<?> getContentTitleModel() {
        return $m.ofValue("Exigência");
    }

    @Override
    protected IModel<?> getContentSubtitleModel() {
        return $m.ofValue("Petições pendentes de ajuste");
    }

    @Override
    protected void appendActionColumns(BSDataTableBuilder<PeticaoDTO, String, IColumn<PeticaoDTO, String>> builder) {
        BSActionColumn<PeticaoDTO, String> actionColumn = new BSActionColumn<>(getMessage("label.table.column.actions"));
        appendCumprirExigenciaAction(actionColumn);
        appendViewAction(actionColumn);
        builder.appendColumn(actionColumn);
    }

    private void appendCumprirExigenciaAction(BSActionColumn<PeticaoDTO, String> actionColumn) {
        actionColumn
                .appendStaticAction(getMessage("label.table.column.requirement"),
                        Icone.PENCIL, (id, pet) -> criarLink(id, pet, ViewMode.EDIT, AnnotationMode.READ_ONLY));
    }

    private WebMarkupContainer criarLink(String id, IModel<PeticaoDTO> peticaoModel, ViewMode vm, AnnotationMode annotationMode) {
        String href = DispatcherPageUtil.
                baseURL(getBaseUrl())
                .formAction(FormActions.FORM_FILL_WITH_ANALYSIS.getId())
                .formId(peticaoModel.getObject().getCodPeticao())
                .param(SIGLA_FORM_NAME, peticaoModel.getObject().getType())
                .build();
        WebMarkupContainer link = new WebMarkupContainer(id);
        link.add($b.attr("target", "_blank"));
        link.add($b.attr("href", href));
        return link;
    }
}
