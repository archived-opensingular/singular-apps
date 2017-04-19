package org.opensingular.server.module.workspace;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.ServerContext;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.provider.TaskItemBoxDataProvider;

import java.util.ArrayList;
import java.util.List;

public class DefaultDonebox implements ItemBoxFactory {

    @Override
    public boolean appliesTo(IServerContext context) {
        return ServerContext.WORKLIST == context;
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox concluidas = new ItemBox();
        concluidas.setName("Concluídas");
        concluidas.setDescription("Petições concluídas");
        concluidas.setIcone(Icone.DOCS);
        concluidas.setEndedTasks(Boolean.TRUE);
//        concluidas.addAction(DefaultActions.VIEW);
        return concluidas;
    }

    @Override
    public ItemBoxDataProvider getDataProvider() {
        return ApplicationContextProvider.get().getBean(TaskItemBoxDataProvider.class);
    }

    @Override
    public List<DatatableField> getDatatableFields() {
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
