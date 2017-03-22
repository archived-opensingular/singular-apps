package org.opensingular.server.commons.metadata;

@Deprecated
public interface SingularServerMetadata {

    String getServerBaseUrl();

    default String concatToBaseUrl(String str){
        return getServerBaseUrl()+ str;
    }
}