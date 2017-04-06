package org.opensingular.server.module.workspace;

import org.opensingular.lib.support.spring.util.ApplicationContextProvider;
import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.ServerContext;
import org.opensingular.server.commons.flow.actions.DefaultActions;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.ItemBoxDataProvider;
import org.opensingular.server.module.provider.TaskItemBoxDataProvider;

import java.util.ArrayList;
import java.util.List;

import static org.opensingular.server.commons.flow.actions.DefaultActions.ASSIGN;

public class DefaultInbox implements ItemBoxFactory {

    @Override
    public boolean appliesTo(IServerContext context) {
        return ServerContext.WORKLIST == context;
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox caixaEntrada = new ItemBox();
        caixaEntrada.setName("Caixa de Entrada");
        caixaEntrada.setDescription("Petições aguardando ação do usuário");
        caixaEntrada.setIcone(Icone.DOCS);
        caixaEntrada.setEndedTasks(Boolean.FALSE);
        caixaEntrada.addAction(ASSIGN);
        caixaEntrada.addAction(DefaultActions.ANALYSE);
        caixaEntrada.addAction(DefaultActions.RELOCATE);
        caixaEntrada.addAction(DefaultActions.VIEW);
        return caixaEntrada;
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
        fields.add(DatatableField.of("Alocado", "nomeUsuarioAlocado"));
        return fields;
    }

}

