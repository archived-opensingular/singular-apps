/*
 * Copyright (c) 2016, Singular and/or its affiliates. All rights reserved.
 * Singular PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.opensingular.server.commons.wicket.view.template;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.opensingular.flow.persistence.entity.ProcessGroupEntity;
import org.opensingular.lib.wicket.util.behavior.BSSelectInitBehaviour;
import org.opensingular.lib.wicket.util.behavior.FormComponentAjaxUpdateBehavior;
import org.opensingular.lib.wicket.util.menu.AbstractMenuItem;
import org.opensingular.server.commons.service.dto.BoxConfigurationData;
import org.opensingular.server.commons.wicket.SingularSession;

import javax.inject.Inject;
import java.util.List;

import static org.opensingular.server.commons.wicket.view.util.ActionContext.MENU_PARAM_NAME;
import static org.opensingular.server.commons.wicket.view.util.ActionContext.PROCESS_GROUP_PARAM_NAME;

public class SelecaoMenuItem extends AbstractMenuItem {

    @Inject
    @SpringBean(required = false)
    private MenuService menuService;

    private List<ProcessGroupEntity> categorias;

    public SelecaoMenuItem(List<ProcessGroupEntity> categorias) {
        super("menu-item");
        this.categorias = categorias;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Form                      form  = new Form<String>("form");
        Model<ProcessGroupEntity> model = new Model<>(SingularSession.get().getCategoriaSelecionada());
        final DropDownChoice<ProcessGroupEntity> select = new DropDownChoice<>("select", model, categorias,
                new ChoiceRenderer<>("name", "cod"));

        form.add(select);
        select.add(new BSSelectInitBehaviour());
        select.add(new FormComponentAjaxUpdateBehavior("change", (target, component) -> {
            final ProcessGroupEntity categoriaSelecionada = (ProcessGroupEntity) component.getDefaultModelObject();
            SingularSession.get().setCategoriaSelecionada(categoriaSelecionada);
            getPage().getPageParameters().set(PROCESS_GROUP_PARAM_NAME, categoriaSelecionada.getCod());
            final BoxConfigurationData boxConfigurationMetadataDTO = getDefaultMenuSelection(categoriaSelecionada);
            if (boxConfigurationMetadataDTO != null) {
                getPage().getPageParameters().set(MENU_PARAM_NAME, boxConfigurationMetadataDTO.getLabel());
            } else {
                getPage().getPageParameters().remove(MENU_PARAM_NAME);
            }
            setResponsePage(getPage().getClass(), getPage().getPageParameters());
        }));

        add(form);
    }

    private BoxConfigurationData getDefaultMenuSelection(ProcessGroupEntity categoriaSelecionada) {
        if(menuService != null){
            return menuService.getDefaultSelectedMenu(categoriaSelecionada);
        }
        return null;
    }

    @Override
    protected boolean configureActiveItem() {
        return false;
    }
}
