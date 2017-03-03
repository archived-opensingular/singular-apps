package org.opensingular.server.module.requirement;

import org.opensingular.form.SType;
import org.opensingular.lib.commons.lambda.IFunction;

public class STypeProperty {

    private IFunction<? extends SType, ? extends SType> field;
    private Object                                      matchingValue;

    public STypeProperty() {
    }

    public STypeProperty(IFunction<? extends SType, ? extends SType> field, Object matchingValue) {

        this.field = field;
        this.matchingValue = matchingValue;
    }

    public IFunction<? extends SType, ? extends SType> getField() {

        return field;
    }

    public Object getMatchingValue() {
        return matchingValue;
    }
}
