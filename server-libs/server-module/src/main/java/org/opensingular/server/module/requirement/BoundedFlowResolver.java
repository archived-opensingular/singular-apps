package org.opensingular.server.module.requirement;

import com.google.common.collect.Sets;
import org.opensingular.flow.core.FlowDefinition;
import org.opensingular.form.SIComposite;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.flow.FlowResolver;
import org.opensingular.server.commons.wicket.view.form.FormPageExecutionContext;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Responsible for resolve a {@link FlowDefinition} based {@link SIComposite}
 * data. This particular implementation enforce that the returned {@link FlowDefinition} is among the ones
 * listed in the {@link #allowedFlows} property
 */
public class BoundedFlowResolver implements FlowResolver {

    private Set<Class<? extends FlowDefinition>> allowedFlows = new HashSet<>();
    private FlowResolver resolver;

    /**
     * @param resolver
     *  An simple resolver or lambda expression which takes the root {@link SIComposite} filled with input date
     *  in order to resolve the proper {@link FlowDefinition} class.
     * @param allowedFlows
     *  This set of  {@link FlowDefinition} classes in which the {@param resolver} is allowed to pick
     *  the proper  {@link FlowDefinition} class.
     */
    public BoundedFlowResolver(FlowResolver resolver, Set<Class<? extends FlowDefinition>> allowedFlows) {
        this.resolver = resolver;
        this.allowedFlows.addAll(allowedFlows);
    }

    /**
     *
     * @param resolver
     *  An simple resolver or lambda expression which takes the root {@link SIComposite} filled with input date
     *  in order to resolve the proper {@link FlowDefinition} class.
     * @param allowedFlows
     *  This set of  {@link FlowDefinition} classes in which the {@param resolver} is allowed to pick
     *  the proper  {@link FlowDefinition} class.
     */
    public BoundedFlowResolver(FlowResolver resolver, Class<? extends FlowDefinition>... allowedFlows) {
        this(resolver, (Sets.newHashSet(allowedFlows)));

    }

    @Override
    public Optional<Class<? extends FlowDefinition>> resolve(FormPageExecutionContext cfg, SIComposite iRoot) {
        return resolver.resolve(cfg, iRoot);
    }

    private final Class<? extends FlowDefinition> checkResolvedFlow(Optional<Class<? extends FlowDefinition>> flowClass) {
        if (flowClass.isPresent()) {
            if (allowedFlows.contains(flowClass.get())) {
                return flowClass.get();
            }
        }
        throw SingularServerException.rethrow("A definição de processo resolvida não está declarada na definição do requerimento.");
    }
}
