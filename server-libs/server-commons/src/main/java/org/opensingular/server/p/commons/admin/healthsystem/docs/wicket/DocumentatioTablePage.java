package org.opensingular.server.p.commons.admin.healthsystem.docs.wicket;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.opensingular.form.STypeComposite;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.server.commons.form.SingularServerSpringTypeLoader;
import org.opensingular.server.p.commons.admin.healthsystem.docs.DocumentationMetadataBuilder;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

import static org.opensingular.lib.wicket.util.util.WicketUtils.*;

public class DocumentatioTablePage extends WebPage {

    private final IModel<List<DocumentationMetadataBuilder.StreamLinedMetadata>> tablesModel;

    @Inject
    private SingularServerSpringTypeLoader typeLoader;

    public DocumentatioTablePage(Class<? extends STypeComposite> stype) {
        tablesModel = new ListModel<>(new DocumentationMetadataBuilder(typeLoader.loadTypeOrException(stype)).getStreamLinedMetadata());
    }

    @Override
    protected void onConfigure() {
        RepeatingView repeatingView = new RepeatingView("tables", tablesModel);
        for (DocumentationMetadataBuilder.StreamLinedMetadata metas : tablesModel.getObject()) {
            IModel<Integer> counterModel = new Model<>(0);
            WebMarkupContainer components = new WebMarkupContainer(repeatingView.newChildId());
            components.add(new Label("title", $m.ofValue(metas.getTableName())));
            components.add(setupDataTable("table", metas, counterModel));
            repeatingView.add(components);
        }
        queue(repeatingView);
    }

    protected BSDataTable<DocumentationRow, String> setupDataTable(String table, DocumentationMetadataBuilder.StreamLinedMetadata metas, IModel<Integer> counterModel) {
        return new BSDataTableBuilder<>(createDataProvider(metas))
                .appendPropertyColumn($m.ofValue("Número"), r -> {
                    if (r instanceof DocumentationRowBlockSeparator) {
                        return ((DocumentationRowBlockSeparator) r).getBlockName();
                    }
                    Integer i = counterModel.getObject();
                    i = i + 1;
                    counterModel.setObject(i);
                    return i;
                })
                .appendPropertyColumn($m.ofValue("Nome do campo"), r -> isBlock(r) ? null : ((DocumentationRowFieldMetadata) r).getFieldName())
                .appendPropertyColumn($m.ofValue("Tipo"), r -> isBlock(r) ? null : ((DocumentationRowFieldMetadata) r).getDocTypeAbbreviation())
                .appendPropertyColumn($m.ofValue("Obrigatório"), r -> isBlock(r) ? null : ((DocumentationRowFieldMetadata) r).getRequired())
                .appendPropertyColumn($m.ofValue("Habilitado"), r -> isBlock(r) ? null : ((DocumentationRowFieldMetadata) r).getEnabled())
                .appendPropertyColumn($m.ofValue("Tamanho"), r -> isBlock(r) ? null : ((DocumentationRowFieldMetadata) r).getFieldSize())
                .appendPropertyColumn($m.ofValue("Regras"), r -> isBlock(r) ? null : ((DocumentationRowFieldMetadata) r).getBusinessRules())
                .appendPropertyColumn($m.ofValue("Mensagens"), r -> isBlock(r) ? null : ((DocumentationRowFieldMetadata) r).getValidationMessages())
                .appendPropertyColumn($m.ofValue("Domínio / Máscara / Hint / Demais observações"), r -> isBlock(r) ? null : ((DocumentationRowFieldMetadata) r).getGeneralInformation())
                .setRowsPerPage(Long.MAX_VALUE)
                .setStripedRows(false)
                .setBorderedTable(false)
                .build("table");
    }

    private boolean isBlock(DocumentationRow r) {
        return r instanceof DocumentationRowBlockSeparator;
    }


    private ISortableDataProvider<DocumentationRow, String> createDataProvider(DocumentationMetadataBuilder.StreamLinedMetadata metas) {
        return new ISortableDataProvider<DocumentationRow, String>() {

            @Override
            public ISortState<String> getSortState() {
                return null;
            }

            @Override
            public long size() {
                return Integer.MAX_VALUE;
            }


            @Override
            public Iterator<? extends DocumentationRow> iterator(long first, long count) {
                return metas.getDocumentationRows().iterator();
            }

            @Override
            public IModel<DocumentationRow> model(DocumentationRow object) {
                return $m.ofValue(object);
            }

            @Override
            public void detach() {

            }

        };
    }


}
