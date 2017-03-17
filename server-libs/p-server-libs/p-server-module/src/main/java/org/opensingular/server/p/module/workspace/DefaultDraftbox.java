package org.opensingular.server.p.module.workspace;

import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.flow.actions.DefaultActions;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.workspace.ItemBoxFactory;
import org.opensingular.server.p.commons.config.PServerContext;

import java.util.ArrayList;
import java.util.List;

import static org.opensingular.server.commons.rest.DefaultServerREST.COUNT_PETITIONS;
import static org.opensingular.server.commons.rest.DefaultServerREST.SEARCH_PETITIONS;

public class DefaultDraftbox implements ItemBoxFactory {

    @Override
    public boolean appliesTo(IServerContext context) {
        return PServerContext.PETITION.equals(context);
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox rascunho = new ItemBox();
        rascunho.setName("Rascunho");
        rascunho.setDescription("Petições de rascunho");
        rascunho.setIcone(Icone.DOCS);
        rascunho.setShowNewButton(true);
        rascunho.setShowDraft(true);
        rascunho.setFieldsDatatable(criarFieldsDatatableRascunho());
        rascunho.addAction(DefaultActions.EDIT)
                .addAction(DefaultActions.VIEW)
                .addAction(DefaultActions.DELETE);
        return rascunho;
    }

    @Override
    public ItemBoxDataProvider getDataProvider() {
        return null;
    }

    private List<DatatableField> criarFieldsDatatableRascunho() {
        List<DatatableField> fields = new ArrayList<>();
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Edição", "editionDate"));
        fields.add(DatatableField.of("Data de Entrada", "creationDate"));
        return fields;
    }
}
