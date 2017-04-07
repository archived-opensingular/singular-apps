package org.opensingular.server.commons.test;

import net.vidageek.mirror.dsl.Mirror;
import org.opensingular.server.commons.config.ServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;

public class ContextUtil {

    private static String contextPath = "/singular";
    private static String pathInfo    = contextPath + ServerContext.WORKLIST.getUrlPath();
    private static Logger logger      = LoggerFactory.getLogger(ContextUtil.class);

    public static void prepareRequest(ServletRequest request) {
        logger.info("Configurando Request Context Path: " + contextPath);
        new Mirror().on(request).invoke().setterFor("contextPath").withValue(contextPath);
        logger.info("Configurando Request Path Info: " + pathInfo);
        new Mirror().on(request).invoke().setterFor("pathInfo").withValue(pathInfo);
    }

    public static void setContextPath(String contextPath) {
        ContextUtil.contextPath = contextPath;
    }

    public static void setPathInfo(String pathInfo) {
        ContextUtil.pathInfo = pathInfo;
    }
}
