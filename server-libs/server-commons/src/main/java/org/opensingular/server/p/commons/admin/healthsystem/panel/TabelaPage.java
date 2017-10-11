package org.opensingular.server.p.commons.admin.healthsystem.panel;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensingular.form.SType;
import org.opensingular.form.STypeComposite;
import org.opensingular.form.STypes;
import org.opensingular.lib.wicket.util.datatable.column.BSPropertyColumn;
import org.opensingular.server.commons.form.SingularServerSpringTypeLoader;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    protected DataTable<DocumentationRow, String> setupDataTable() {
        return new DataTable<DocumentationRow, String>("tabela", appendColumn(), createDataProvider(), Integer.MAX_VALUE) {
            @Override
            protected Item<DocumentationRow> newRowItem(String id, int index, IModel<DocumentationRow> model) {
                Item<DocumentationRow> item = super.newRowItem(id, index, model);
                if (model.getObject().getRowType() == DocumentationRow.RowType.SEPARATOR) {
                    item.add(new Behavior() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onComponentTag(Component component, ComponentTag tag) {

                            component
                                    .getResponse()
                                    .write(
                                            "<tr><td colspan=\"" + getColumns().size() + 1 + "\">Separador Exemplo</td></tr>");

                        }
                    });
                }
                return item;
            }
        };
    }

    private List<IColumn<DocumentationRow, String>> appendColumn() {
        List<IColumn<DocumentationRow, String>> list = new ArrayList<>();
        list.add(new BSPropertyColumn<DocumentationRow, String>($m.ofValue("Número"), r -> {
            Integer i = counterModel.getObject();
            i = i + 1;
            counterModel.setObject(i);
            return i;
        }));
        list.add(new BSPropertyColumn<DocumentationRow, String>($m.ofValue("Nome do campo"), r -> ((FormDocumentationMetadata) r).getFieldName()));
        list.add(new BSPropertyColumn<DocumentationRow, String>($m.ofValue("Tipo"), r -> ((FormDocumentationMetadata) r).getFieldTypeAbbreviation()));
        list.add(new BSPropertyColumn<DocumentationRow, String>($m.ofValue("Obrigatório"), r -> ((FormDocumentationMetadata) r).isRequired()));
        list.add(new BSPropertyColumn<DocumentationRow, String>($m.ofValue("Habilitado"), r -> ((FormDocumentationMetadata) r).isEnabled()));
        list.add(new BSPropertyColumn<DocumentationRow, String>($m.ofValue("Tamanho"), r -> ((FormDocumentationMetadata) r).getFieldSize()));
        list.add(new BSPropertyColumn<DocumentationRow, String>($m.ofValue("Regras"), r -> ((FormDocumentationMetadata) r).getBusinessRules()));
        list.add(new BSPropertyColumn<DocumentationRow, String>($m.ofValue("Mensagens"), r -> ((FormDocumentationMetadata) r).getMessages()));
        list.add(new BSPropertyColumn<DocumentationRow, String>($m.ofValue("Domínio / Máscara / Hint / Demais observações"), r -> ((FormDocumentationMetadata) r).getGeneralInformation()));
        return list;
    }


    private IDataProvider<DocumentationRow> createDataProvider() {
        return new IDataProvider<DocumentationRow>() {

            @Override
            public long size() {
                return Integer.MAX_VALUE;
            }

            @Override
            public Iterator<FormDocumentationMetadata> iterator(long first, long count) {
                STypeComposite<?> rootType = (STypeComposite<?>) typeLoader.loadTypeOrException((Class<? extends SType>) getDefaultModelObject());
                return STypes
                        .streamDescendants(rootType, true)
                        .map(type -> new FormDocumentationMetadata(rootType, type))
                        .filter(FormDocumentationMetadata::isFormInputField).iterator();
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
