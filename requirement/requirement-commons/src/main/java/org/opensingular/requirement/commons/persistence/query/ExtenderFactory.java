package org.opensingular.requirement.commons.persistence.query;

import org.opensingular.requirement.commons.persistence.context.RequirementSearchContext;

import java.util.function.Function;

public interface ExtenderFactory extends Function<RequirementSearchContext, RequirementSearchExtender> {

}