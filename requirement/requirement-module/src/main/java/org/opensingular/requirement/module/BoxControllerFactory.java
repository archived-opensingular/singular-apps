package org.opensingular.requirement.module;

import org.springframework.beans.factory.BeanFactory;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * TODO Vinicius, devemos conversar sobre a maneira padrão de criar objetos que usam spring beans e ao
 * mesmo tempo possuem estado, acredito que essa abordagem é mais facil de refatorar
 */
@Named
public class BoxControllerFactory {
    @Inject
    private BeanFactory beanFactory;

    public BoxController create(BoxInfo boxInfo) {
        return new BoxController(boxInfo, beanFactory.getBean(boxInfo.getBoxDefinitionClass()).getDataProvider());
    }
}