package org.opensingular.server.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.apache.wicket.model.IModel;
import org.opensingular.flow.persistence.entity.Actor;
import org.opensingular.flow.persistence.entity.ModuleEntity;
import org.opensingular.server.commons.box.BoxItemDataImpl;
import org.opensingular.server.commons.box.BoxItemDataMap;
import org.opensingular.server.commons.box.action.ActionResponse;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.service.dto.ItemActionConfirmation;
import org.opensingular.server.commons.service.dto.ItemBox;
import org.springframework.context.annotation.Primary;

@Primary
@Named
public class BoxServiceMock extends BoxService {

    @Override
    public List<Actor> buscarUsuarios(ModuleEntity module, IModel<BoxItemDataMap> currentModel, ItemActionConfirmation confirmation) {
        Actor actor = new Actor(1, "USUARIO.TESTE", "Usuário de Teste", "usuarioteste@teste.com.br");
        return Collections.singletonList(actor);
    }

    @Override
    public <T extends ActionResponse> T callModule(String url, Object arg, Class<T> clazz) {
        ActionResponse response = new ActionResponse("Sucesso", true);
        return clazz.cast(response);
    }

    @Override
    public long countQuickSearch(ModuleEntity module, ItemBox itemBox, QuickFilter filter) {
        return 2L;
    }

    @Override
    public List<BoxItemDataMap> quickSearch(ModuleEntity module, ItemBox itemBox, QuickFilter filter) {
        List<BoxItemDataMap> result = new ArrayList<>();
        result.add(createItem("1", "Descrição", "Situação", "Processo",
                "25/04/2017 08:49", "Form tipo", "Chave processo",
                "25/04/2017 08:49", "25/04/2017 08:49", "25/04/2017 08:49",
                "1", null, null));
        result.add(createItem("2", "Descrição", "Situação", "Processo",
                "25/04/2017 08:49", "Form tipo", "Chave processo",
                "25/04/2017 08:49", "25/04/2017 08:49", "25/04/2017 08:49",
                "1", null, null));

        return result;
    }

    private BoxItemDataMap createItem(String codPeticao, String description, String situation, String processName,
                                    String creationDate, String type, String processType, String situationBeginDate,
                                    String processBeginDate, String editionDate, String processInstanceId, String rootPetition,
                                    String parentPetition) {
        Map<String, Serializable> item = new HashMap<>();
        item.put("codPeticao", codPeticao);
        item.put("description", description);
        item.put("situation", situation);
        item.put("processName", processName);
        item.put("creationDate", creationDate);
        item.put("type", type);
        item.put("processType", processType);
        item.put("situationBeginDate", situationBeginDate);
        item.put("processBeginDate", processBeginDate);
        item.put("editionDate", editionDate);
        item.put("processInstanceId", processInstanceId);
        item.put("rootPetition", rootPetition);
        item.put("parentPetition", parentPetition);

        BoxItemDataImpl i = new BoxItemDataImpl();
        i.setRawMap(item);

        i.getBoxItemActions()
                .addEditAction(i)
                .addViewAction(i)
                .addDeleteAction(i)
                .addRelocateAction(i);

        return new BoxItemDataMap(i);
    }


}
