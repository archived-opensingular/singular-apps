package org.opensingular.server.commons.metadata;

/**
 * @deprecated Essa solução não deve ser difundida
 */
@Deprecated
public interface SingularServerMetadata {

    String getServerBaseUrl();

    default String concatToBaseUrl(String str){
        return getServerBaseUrl()+ str;
    }
}