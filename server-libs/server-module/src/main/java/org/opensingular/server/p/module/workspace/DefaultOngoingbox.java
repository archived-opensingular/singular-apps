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

public class DefaultOngoingbox implements BoxDefinition {

    @Override
    public boolean appliesTo(IServerContext context) {
        return PServerContext.REQUIREMENT.isSameContext(context);
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox acompanhamento = new ItemBox();
        acompanhamento.setName("Acompanhamento");
        acompanhamento.setDescription("Requerimentos em andamento");
        acompanhamento.setIcone(DefaultIcons.CLOCK);
        return acompanhamento;
    }

    @Override
    public BoxItemDataProvider getDataProvider() {
        return new RequirementBoxItemDataProvider(Boolean.FALSE, new ActionProviderBuilder().addViewAction());
    }

    @Override
    public List<DatatableField> getDatatableFields() {
        List<DatatableField> fields = new ArrayList<>();
        fields.add(DatatableField.of("Número", "codPeticao"));
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Entrada", "processBeginDate"));
        fields.add(DatatableField.of("Situação", "situation"));
        fields.add(DatatableField.of("Dt. Situação", "situationBeginDate"));
        return fields;
    }

}
