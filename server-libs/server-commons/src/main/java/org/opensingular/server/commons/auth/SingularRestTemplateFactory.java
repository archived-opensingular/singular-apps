package org.opensingular.server.commons.auth;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.server.commons.exception.SingularServerException;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.opensingular.server.commons.config.DefaultRestSecurity.SINGULAR_MODULE_PASSWORD;
import static org.opensingular.server.commons.config.DefaultRestSecurity.SINGULAR_MODULE_USERNAME;

public class SingularRestTemplateFactory {
    private static final SingularRestTemplateFactory INSTANCE = new SingularRestTemplateFactory();

    private SingularRestTemplateFactory() {
    }

    public RestTemplate newRestTemplate() {
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() {
        final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient());
        return clientHttpRequestFactory;
    }

    private HttpClient httpClient() {
        String u = SingularProperties.get().getProperty(SINGULAR_MODULE_USERNAME);
        String p = SingularProperties.get().getProperty(SINGULAR_MODULE_PASSWORD);
        if (StringUtils.isAnyBlank(u, p)) {
            throw new SingularServerException("Não foi definido a senha ou o password da segurança rest");
        }
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(u, p));
        return HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
    }

    public static SingularRestTemplateFactory getInstance() {
        return INSTANCE;
    }
}
