package org.opensingular.server.commons.metadata;


/**
 * @deprecated Essa solução não deve ser difundida
 */
@Deprecated
public class DefaultSingularServerMetadata implements SingularServerMetadata{

    @Override
    public String getServerBaseUrl() {
        return "/singular";
    }

}
