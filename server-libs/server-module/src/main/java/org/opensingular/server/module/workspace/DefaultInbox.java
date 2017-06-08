package org.opensingular.server.module.workspace;

import org.opensingular.lib.wicket.util.resource.Icone;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.ServerContext;
import org.opensingular.server.commons.service.dto.DatatableField;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.opensingular.server.module.ActionProviderBuilder;
import org.opensingular.server.module.BoxItemDataProvider;
import org.opensingular.server.module.provider.RequirementBoxItemDataProvider;

import java.util.ArrayList;
import java.util.List;

public class DefaultInbox implements ItemBoxFactory {

    @Override
    public boolean appliesTo(IServerContext context) {
        return ServerContext.WORKLIST.isSameContext(context);
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox caixaEntrada = new ItemBox();
        caixaEntrada.setName("Caixa de Entrada");
        caixaEntrada.setDescription("Petições aguardando ação do usuário");
        caixaEntrada.setIcone(Icone.DOCS);
        caixaEntrada.setEndedTasks(Boolean.FALSE);
        return caixaEntrada;
    }

    @Override
    public BoxItemDataProvider getDataProvider() {
        return new RequirementBoxItemDataProvider(Boolean.TRUE, new ActionProviderBuilder()
                .addAssignAction()
                .addAnalyseAction()
                .addRelocateAction()
                .addViewAction());
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
        fields.add(DatatableField.of("Alocado", "nomeUsuarioAlocado"));
        return fields;
    }

}

