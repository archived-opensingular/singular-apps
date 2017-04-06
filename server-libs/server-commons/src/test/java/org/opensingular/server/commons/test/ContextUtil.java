package org.opensingular.server.commons.test;

import net.vidageek.mirror.dsl.Mirror;
import org.opensingular.server.commons.config.ServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;

public class ContextUtil {

    public static final String CONTEXT_PATH = "/singular";

    private static Logger logger = LoggerFactory.getLogger(ContextUtil.class);

    public static void prepareRequest(ServletRequest request) {
        logger.info("Configurando Request Context Path: " + CONTEXT_PATH);
        new Mirror().on(request).invoke().setterFor("contextPath").withValue(CONTEXT_PATH);
        String pathInfo = "/singular" + ServerContext.WORKLIST.getUrlPath();
        logger.info("Configurando Request Path Info: " + pathInfo);
        new Mirror().on(request).invoke().setterFor("pathInfo").withValue(pathInfo);
    }
}
