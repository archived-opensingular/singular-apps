package org.opensingular.server.commons.metadata;

@Deprecated
public class DefaultSingularServerMetadata implements SingularServerMetadata{

    @Override
    public String getServerBaseUrl() {
        return "/singular";
    }

}
