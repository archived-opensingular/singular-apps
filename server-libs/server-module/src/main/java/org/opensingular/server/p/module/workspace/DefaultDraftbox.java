package org.opensingular.server.p.module.workspace;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.provider.PetitionItemBoxDataProvider;
import org.opensingular.server.module.workspace.ItemBoxFactory;
import org.opensingular.server.p.commons.config.PServerContext;

import java.util.ArrayList;
import java.util.List;

public class DefaultDraftbox implements ItemBoxFactory {

    @Override
    public boolean appliesTo(IServerContext context) {
        return PServerContext.PETITION == context;
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox rascunho = new ItemBox();
        rascunho.setName("Rascunho");
        rascunho.setDescription("Petições de rascunho");
        rascunho.setIcone(Icone.DOCS);
        rascunho.setShowNewButton(true);
        rascunho.setShowDraft(true);
//        rascunho.addAction(DefaultActions.EDIT)
//                .addAction(DefaultActions.VIEW)
//                .addAction(DefaultActions.DELETE);
        return rascunho;
    }

    @Override
    public ItemBoxDataProvider getDataProvider() {
        return ApplicationContextProvider.get().getBean(PetitionItemBoxDataProvider.class);
    }

    @Override
    public List<DatatableField> getDatatableFields() {
        List<DatatableField> fields = new ArrayList<>();
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Edição", "editionDate"));
        fields.add(DatatableField.of("Data de Entrada", "creationDate"));
        return fields;
    }

}
