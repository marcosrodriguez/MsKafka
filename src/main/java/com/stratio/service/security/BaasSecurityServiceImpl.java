package com.stratio.service.security;

import com.stratio.gosec_sso.GosecSimpleAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Service
@Slf4j
public class BaasSecurityServiceImpl implements BaasSecurityService {

    @Value("${dcos.entrypoint}")
    private String entrypoint;

    private static final String DCOS_ACS_AUTH_COOKIE = "dcos-acs-auth-cookie=";

    @Override
    public String getSecurityToken(String user, String pass) {
        return getSSOcookie(entrypoint, user, pass);
    }

    @Override
    public Boolean isValidToken(String token) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("https://" + entrypoint)
                .path("/marathon/v2/apps");
        log.info(builder.build().encode().toUri().toString());

        try {
            return executeSecuredRestTemplate(token, builder.toUriString(), String.class).getStatusCode() != HttpStatus.UNAUTHORIZED;
        } catch (HttpStatusCodeException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex) {
            log.info("Rest template error {} ", ex.getLocalizedMessage());
        }
        return false;

    }

    private <T> ResponseEntity<?> executeSecuredRestTemplate(String token, String uri, Class<T> body)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, HttpStatusCodeException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", DCOS_ACS_AUTH_COOKIE + token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        return getRestTemplate().exchange(
                uri, HttpMethod.GET, requestEntity, body);

    }

    private RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }

    private static String getSSOcookie(String dcosEntrypoint, String ssoUser, String ssoPass) {

        log.debug("Getting SSO cookie for accessing to Marathon.");

        String url = String.format("https://%s/login?firstUser=false", dcosEntrypoint);
        log.debug("URL used form login: " + url);

        GosecSimpleAuthenticator gosecSimpleAuthenticator = new GosecSimpleAuthenticator(url, ssoUser, ssoPass);
        Boolean authenticated = gosecSimpleAuthenticator.authenticate();
        log.info(String.format("Login: %s", authenticated.toString()));

        return gosecSimpleAuthenticator.getDcosToken();
    }
}
