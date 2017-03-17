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

public class DefaultEverythingbox implements ItemBoxFactory {

    @Override
    public boolean appliesTo(IServerContext context) {
        return PServerContext.PETITION.equals(context);
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox acompanhamento = new ItemBox();
        acompanhamento.setName("Acompanhamento");
        acompanhamento.setDescription("Petições em andamento");
        acompanhamento.setIcone(Icone.CLOCK);
        acompanhamento.setFieldsDatatable(criarFieldsDatatableAcompanhamento());
        acompanhamento.addAction(DefaultActions.VIEW);
        return acompanhamento;
    }

    @Override
    public ItemBoxDataProvider getDataProvider() {
        return null;
    }


    private List<DatatableField> criarFieldsDatatableAcompanhamento() {
        List<DatatableField> fields = new ArrayList<>();
        fields.add(DatatableField.of("Número", "codPeticao"));
        fields.add(DatatableField.of("Dt. Entrada", "processBeginDate"));
        fields.add(DatatableField.of("Situação", "situation"));
        fields.add(DatatableField.of("Dt. Situação", "situationBeginDate"));
        return fields;
    }
}
