package org.opensingular.server.module.workspace;

import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.ServerContext;
import org.opensingular.server.commons.flow.actions.DefaultActions;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;

import java.util.ArrayList;
import java.util.List;

import static org.opensingular.server.commons.rest.DefaultServerREST.COUNT_TASKS;
import static org.opensingular.server.commons.rest.DefaultServerREST.SEARCH_TASKS;

public class DefaultDonebox implements ItemBoxFactory {

    @Override
    public boolean appliesTo(IServerContext context) {
        return ServerContext.WORKLIST.equals(context);
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox concluidas = new ItemBox();
        concluidas.setName("Concluídas");
        concluidas.setDescription("Petições concluídas");
        concluidas.setIcone(Icone.DOCS);
        concluidas.setSearchEndpoint(SEARCH_TASKS);
        concluidas.setCountEndpoint(COUNT_TASKS);
        concluidas.setEndedTasks(Boolean.TRUE);
        concluidas.setFieldsDatatable(criarFieldsDatatableWorklistConcluidas());
        concluidas.addAction(DefaultActions.VIEW);
        return concluidas;
    }

    protected List<DatatableField> criarFieldsDatatableWorklistConcluidas() {
        List<DatatableField> fields = new ArrayList<>();
        fields.add(DatatableField.of("Número", "codPeticao"));
        fields.add(DatatableField.of("Dt. de Entrada", "creationDate"));
        fields.add(DatatableField.of("Solicitante", "solicitante"));
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Situação", "situationBeginDate"));
        fields.add(DatatableField.of("Situação", "taskName"));
        return fields;
    }


}
