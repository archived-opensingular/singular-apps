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

public class DefaultEverythingbox implements ItemBoxFactory {

    @Override
    public boolean appliesTo(IServerContext context) {
        return PServerContext.PETITION == context;
    }

    @Override
    public ItemBox build(IServerContext context) {
        final ItemBox acompanhamento = new ItemBox();
        acompanhamento.setName("Acompanhamento");
        acompanhamento.setDescription("Petições em andamento");
        acompanhamento.setIcone(Icone.CLOCK);
//        acompanhamento.addAction(DefaultActions.VIEW);
        return acompanhamento;
    }

    @Override
    public ItemBoxDataProvider getDataProvider() {
        return ApplicationContextProvider.get().getBean(PetitionItemBoxDataProvider.class);
    }

    @Override
    public List<DatatableField> getDatatableFields() {
        List<DatatableField> fields = new ArrayList<>();
        fields.add(DatatableField.of("Número", "codPeticao"));
        fields.add(DatatableField.of("Dt. Entrada", "processBeginDate"));
        fields.add(DatatableField.of("Situação", "situation"));
        fields.add(DatatableField.of("Dt. Situação", "situationBeginDate"));
        return fields;
    }

}
