package org.opensingular.server.module.requirement;

import org.opensingular.flow.core.ProcessDefinition;
import org.opensingular.form.SIComposite;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.flow.FlowResolver;
import org.opensingular.server.commons.wicket.view.form.FormPageConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BoundedFlowResolver implements FlowResolver {

    private Set<Class<? extends ProcessDefinition>> allowedFlows = new HashSet<>();
    private FlowResolver resolver;

    public BoundedFlowResolver(FlowResolver resolver, Set<Class<? extends ProcessDefinition>> allowedFlows) {
        this.resolver = resolver;
        this.allowedFlows.addAll(allowedFlows);
    }

    public BoundedFlowResolver(FlowResolver resolver, Class<? extends ProcessDefinition>... allowedFlows) {
        this.resolver = resolver;
        this.allowedFlows.addAll((Arrays.asList(allowedFlows)));
    }

    @Override
    public Optional<Class<? extends ProcessDefinition>> resolve(FormPageConfig cfg, SIComposite iRoot) {
        return resolver.resolve(cfg, iRoot);
    }

    private final Class<? extends ProcessDefinition> checkResolvedFlow(Optional<Class<? extends ProcessDefinition>> flowClass) {
        if (flowClass.isPresent()) {
            if (allowedFlows.contains(flowClass.get())) {
                return flowClass.get();
            }
        }
        throw SingularServerException.rethrow("A definição de processo resolvida não está declarada na definição do requerimento.");
    }
}
