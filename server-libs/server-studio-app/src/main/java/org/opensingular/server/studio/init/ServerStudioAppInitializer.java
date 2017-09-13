package org.opensingular.server.studio.init;

import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.wicket.util.template.SkinOptions;
import org.opensingular.server.commons.config.IServerContext;
import org.opensingular.server.commons.config.SingularSpringWebMVCConfig;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.spring.SingularDefaultBeanFactory;
import org.opensingular.server.commons.wicket.SingularServerApplication;
import org.opensingular.server.p.commons.admin.AdministrationApplication;
import org.opensingular.server.p.commons.config.PServerContext;
import org.opensingular.server.p.commons.config.PSpringSecurityInitializer;
import org.opensingular.server.p.commons.config.PWebInitializer;
import org.opensingular.server.single.config.SingleAppInitializer;
import org.opensingular.server.studio.spring.ServerStudioRequirementBeanFactory;
import org.opensingular.server.studio.spring.StudioSpringSecurityInitializer;
import org.opensingular.server.studio.spring.ServerStudioWebMVCConfig;
import org.opensingular.server.studio.wicket.ServerStudioApplication;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.function.Predicate;

public interface ServerStudioAppInitializer extends SingleAppInitializer {

    PServerContext STUDIO = new PServerContext("STUDIO", "/*", "singular.studio");

    @Override
    default Class<? extends SingularDefaultBeanFactory> beanFactory() {
        return ServerStudioRequirementBeanFactory.class;
    }

    @Override
    default PWebInitializer webConfiguration() {
        return new PWebInitializer() {

            @Override
            protected void configureCAS(ServletContext servletContext) {
            }

            @Override
            public IServerContext[] serverContexts() {
                return new IServerContext[]{PServerContext.REQUIREMENT, PServerContext.WORKLIST, PServerContext.ADMINISTRATION, STUDIO};
            }

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setAttribute(SingleAppInitializer.INITSKIN_CONSUMER_REQ_PARAM,
                        (IConsumer<SkinOptions>) ServerStudioAppInitializer.this::initRequirementSkin);
                servletContext.setAttribute(SingleAppInitializer.INITSKIN_CONSUMER_ANL_PARAM,
                        (IConsumer<SkinOptions>) ServerStudioAppInitializer.this::initAnalysisSkin);
                super.onStartup(servletContext);
            }

            @Override
            protected Class<? extends SingularServerApplication> getWicketApplicationClass(IServerContext currentContext) {
                Predicate<IServerContext> sameContextCheck = (i) -> i.isSameContext(currentContext);
                if (sameContextCheck.test(PServerContext.WORKLIST)) {
                    return AnalysisApplication.class;
                }
                if (sameContextCheck.test(PServerContext.REQUIREMENT)) {
                    return PetitionApplication.class;
                }
                if (sameContextCheck.test(PServerContext.ADMINISTRATION)) {
                    return AdministrationApplication.class;
                }
                if (sameContextCheck.test(STUDIO)) {
                    return ServerStudioApplication.class;
                }
                throw new SingularServerException("Contexto inválido");
            }
        };
    }

    @Override
    default Class<? extends SingularSpringWebMVCConfig> getSingularSpringWebMVCConfig() {
        return ServerStudioWebMVCConfig.class;
    }

    @Override
    default PSpringSecurityInitializer springSecurityConfiguration() {
        return new StudioSpringSecurityInitializer();
    }
}
