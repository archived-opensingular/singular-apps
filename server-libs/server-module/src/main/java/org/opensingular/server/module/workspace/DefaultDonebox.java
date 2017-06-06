package org.opensingular.server.module.workspace;

import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.ServerContext;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.ActionProviderBuilder;
import org.opensingular.server.module.BoxItemDataProvider;
import org.opensingular.server.module.provider.TaskBoxItemDataProvider;

import java.util.ArrayList;
import java.util.List;

public class DefaultDonebox implements ItemBoxFactory {

    @Override
    public boolean appliesTo(IServerContext context) {
        return ServerContext.WORKLIST.isSameContext(context);
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox concluidas = new ItemBox();
        concluidas.setName("Concluídas");
        concluidas.setDescription("Petições concluídas");
        concluidas.setIcone(Icone.DOCS);
        concluidas.setEndedTasks(Boolean.TRUE);
        return concluidas;
    }

    @Override
    public BoxItemDataProvider getDataProvider() {
        return new TaskBoxItemDataProvider(
                new ActionProviderBuilder()
                        .addViewAction()
        );
    }

    @Override
    public List<DatatableField> getDatatableFields() {
        List<DatatableField> fields = new ArrayList<>();
        fields.add(DatatableField.of("Número", "codPeticao"));
        fields.add(DatatableField.of("Dt. de Entrada", "processBeginDate"));
        fields.add(DatatableField.of("Solicitante", "solicitante"));
        fields.add(DatatableField.of("Descrição", "description"));
        fields.add(DatatableField.of("Dt. Situação", "situationBeginDate"));
        fields.add(DatatableField.of("Situação", "taskName"));
        return fields;
    }

}
