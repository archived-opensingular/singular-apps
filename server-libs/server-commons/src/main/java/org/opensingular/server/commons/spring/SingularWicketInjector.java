package org.opensingular.server.commons.spring;

import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;

public class SingularWicketInjector extends SpringComponentInjector {

    private IFieldValueFactory fieldValueFactory;

    public SingularWicketInjector(WebApplication webapp) {
        super(webapp);
        init(true);
    }

    public SingularWicketInjector(WebApplication webapp, ApplicationContext ctx) {
        super(webapp, ctx);
        init(true);
    }

    public SingularWicketInjector(WebApplication webapp, ApplicationContext ctx, boolean wrapInProxies) {
        super(webapp, ctx, wrapInProxies);
        init(wrapInProxies);
    }

    private void init(boolean wrapInProxies) {
        fieldValueFactory = new SingularAnnotationProxyFieldValueFactory(wrapInProxies);
    }


    @Override
    public void inject(Object object) {
        super.inject(object, fieldValueFactory);
    }
}
