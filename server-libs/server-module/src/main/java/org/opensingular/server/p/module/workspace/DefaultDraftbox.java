package org.opensingular.server.p.module.workspace;

import org.opensingular.lib.wicket.util.resource.DefaultIcons;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.ActionProviderBuilder;
import org.opensingular.server.module.BoxItemDataProvider;
import org.opensingular.server.module.provider.RequirementBoxItemDataProvider;
import org.opensingular.server.module.workspace.BoxDefinition;
import org.opensingular.server.p.commons.config.PServerContext;

import java.util.ArrayList;
import java.util.List;

public class DefaultDraftbox implements BoxDefinition {

    @Override
    public boolean appliesTo(IServerContext context) {
        return PServerContext.PETITION.isSameContext(context);
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox rascunho = new ItemBox();
        rascunho.setName("Rascunho");
        rascunho.setDescription("Petições de rascunho");
        rascunho.setIcone(DefaultIcons.DOCS);
        rascunho.setShowNewButton(true);
        rascunho.setShowDraft(true);
        return rascunho;
    }

    @Override
    public BoxItemDataProvider getDataProvider() {
        return new RequirementBoxItemDataProvider(Boolean.FALSE, new ActionProviderBuilder()
                .addEditAction()
                .addViewAction()
                .addDeleteAction());
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
