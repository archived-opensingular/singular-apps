package org.opensingular.server.p.commons.admin.healthsystem.panel;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.STypes;
import org.opensingular.lib.wicket.util.datatable.BSDataTable;
import org.opensingular.lib.wicket.util.datatable.BSDataTableBuilder;
import org.opensingular.lib.wicket.util.datatable.BaseDataProvider;
import org.opensingular.server.commons.form.SingularServerSpringTypeLoader;

import javax.inject.Inject;
import java.util.Iterator;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$m;

public class TabelaPage extends WebPage {

    private IModel<Integer> counterModel = new Model<>(0);

    @Inject
    private SingularServerSpringTypeLoader typeLoader;

    public TabelaPage(IModel<Class<? extends STypeComposite>> model) {
        super(model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        queue(setupDataTable());
    }

    protected BSDataTable<FormDocumentationMetadata, String> setupDataTable() {
        return new BSDataTableBuilder<>(createDataProvider())
                .appendPropertyColumn($m.ofValue("Número"), c -> {
                    Integer value = counterModel.getObject() + 1;
                    counterModel.setObject(value);
                    return value;
                })
                .appendPropertyColumn($m.ofValue("Nome do campo"), FormDocumentationMetadata::getFieldName)
                .appendPropertyColumn($m.ofValue("Tipo"), FormDocumentationMetadata::getFieldTypeAbbreviation)
                .appendPropertyColumn($m.ofValue("Obrigatório"), FormDocumentationMetadata::isRequired)
                .appendPropertyColumn($m.ofValue("Habilitado"), FormDocumentationMetadata::isEnabled)
                .appendPropertyColumn($m.ofValue("Tamanho"), FormDocumentationMetadata::getFieldSize)
                .appendPropertyColumn($m.ofValue("Regras"), FormDocumentationMetadata::getBusinessRules)
                .appendPropertyColumn($m.ofValue("Mensagens"), FormDocumentationMetadata::getMessages)
                .appendPropertyColumn($m.ofValue("Domínio / Máscara / Hint / Demais observações"), FormDocumentationMetadata::getGeneralInformation)
                .setRowsPerPage(Long.MAX_VALUE)
                .setStripedRows(false)
                .setBorderedTable(false)
                .build("tabela");
    }


    private BaseDataProvider<FormDocumentationMetadata, String> createDataProvider() {
        return new BaseDataProvider<FormDocumentationMetadata, String>() {
            @Override
            public Iterator<? extends FormDocumentationMetadata> iterator(int first, int count, String sortProperty, boolean ascending) {
                STypeComposite<?> type = (STypeComposite<?>) typeLoader.loadTypeOrException((Class<? extends SType>) getDefaultModelObject());
                return STypes
                        .streamDescendants(type, true)
                        .map(stype -> new FormDocumentationMetadata(type, stype))
                        .filter(FormDocumentationMetadata::isFormInputField).iterator();
            }
            @Override
            public long size() {
                return Integer.MAX_VALUE;
            }
        };
    }


}
