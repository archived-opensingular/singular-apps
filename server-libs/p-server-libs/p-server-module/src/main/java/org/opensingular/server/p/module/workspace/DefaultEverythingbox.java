package org.opensingular.server.p.module.workspace;

import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.flow.actions.DefaultActions;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.workspace.SingularItemBox;
import org.opensingular.server.p.commons.config.PServerContext;

import java.util.ArrayList;
import java.util.List;

import static org.opensingular.server.commons.rest.DefaultServerREST.COUNT_PETITIONS;
import static org.opensingular.server.commons.rest.DefaultServerREST.SEARCH_PETITIONS;

public class DefaultEverythingbox implements SingularItemBox {

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
        acompanhamento.setSearchEndpoint(SEARCH_PETITIONS);
        acompanhamento.setCountEndpoint(COUNT_PETITIONS);
        acompanhamento.setFieldsDatatable(criarFieldsDatatableAcompanhamento());
        acompanhamento.addAction(DefaultActions.VIEW);
        return acompanhamento;
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
