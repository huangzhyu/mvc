package com.hzy.mvc;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Suppliers;

public class BaseControllerTest {

    private String baseUrl = "http://localhost:" + Simulator.UT_PORT + "/simulator";

    private RestTemplate restClient = new RestTemplate();

    private static Simulator getEndpoint() {
        return Suppliers.memoize(new Simulator()).get();
    }

    @BeforeClass
    public static void startServer() throws Exception {
        getEndpoint().start(Simulator.UT_PORT, "src/main/webapp");
    }

    @AfterClass
    public static void stopServer() throws Exception {
        getEndpoint().stop();
    }

    protected <T> T getForObject(URI uri, Class<T> responseType) {
        return restClient.getForObject(uri, responseType);
    }

    protected <T> T getForObject(String url, Class<T> responseType, Object... uriParameter) {
        return restClient.getForObject(baseUrl.concat(url), responseType, uriParameter);
    }

    protected <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriParameter) {
        return restClient.getForObject(baseUrl.concat(url), responseType, uriParameter);
    }

    protected <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriParameter) {
        return restClient.getForEntity(baseUrl.concat(url), responseType, uriParameter);
    }

    protected <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriParameter) {
        return restClient.getForEntity(baseUrl.concat(url), responseType, uriParameter);
    }

    protected <T> ResponseEntity<T> getForEntity(URI uri, Class<T> responseType) {
        return restClient.getForEntity(uri, responseType);
    }

    protected <T> T postForObject(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        return restClient.postForObject(baseUrl.concat(url), requestBody, responseType, uriVariables);
    }

    protected <T> T postForObject(URI uri, Object requestBody, Class<T> responseType) {
        return restClient.postForObject(uri, requestBody, responseType);
    }

    protected <T> T postForObject(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriParameter) {
        return restClient.postForObject(baseUrl.concat(url), requestBody, responseType, uriParameter);
    }

    protected <T> ResponseEntity<T> postForEntity(String url, Object requestBody, Class<T> responseType,
                                                  Object... uriParameter) {
        return restClient.postForEntity(baseUrl.concat(url), requestBody, responseType, uriParameter);
    }

    protected <T> ResponseEntity<T> postForEntity(String url, Object requestBody, Class<T> responseType,
                                                  Map<String, ?> uriParameter) {
        return restClient.postForEntity(baseUrl.concat(url), requestBody, responseType, uriParameter);
    }

    protected <T> ResponseEntity<T> postForEntity(URI uri, Object requestBody, Class<T> responseType) {
        return restClient.postForEntity(uri, requestBody, responseType);
    }

    protected void upadte(String url, Object requestBody, Object... uriParameter) {
        restClient.put(baseUrl.concat(url), requestBody, uriParameter);
    }

    protected void upadte(String url, Object requestBody, Map<String, ?> uriParameter) {
        restClient.put(baseUrl.concat(url), requestBody, uriParameter);
    }

    protected void upadte(URI uri, Object requestBody) {
        restClient.put(uri, requestBody);
    }

    protected URI create(String url, Object requestBody, Object... uriParameter) {
        return restClient.postForLocation(baseUrl.concat(url), requestBody, uriParameter);
    }

    protected URI create(String url, Object requestBody, Map<String, ?> uriParameter) {
        return restClient.postForLocation(baseUrl.concat(url), requestBody, uriParameter);
    }

    protected URI create(URI uri, Object requestBody) {
        return restClient.postForLocation(uri, requestBody);
    }

    protected void delete(String url, Object... urlParameter) {
        restClient.delete(baseUrl.concat(url), urlParameter);
    }

    protected void delete(String url, Map<String, ?> urlParameter) {
        restClient.delete(baseUrl.concat(url), urlParameter);
    }

    protected void delete(URI uri) {
        restClient.delete(uri);
    }

    protected <T> ResponseEntity<T> requestWithHttpHeaders(URI uri, HttpMethod method,
                                                           Map<String, List<String>> headers, Class<T> responseType) {
        return restClient.exchange(uri, method,
                new HttpEntity<Object>(new LinkedMultiValueMap<String, String>(headers)), responseType);
    }

    protected <T> ResponseEntity<T> requestWithHttpHeaders(String url, HttpMethod method,
                                                           Map<String, List<String>> headers, Class<T> responseType, Map<String, ?> uriVariables) {
        return restClient.exchange(baseUrl.concat(url), method, new HttpEntity<Object>(
                new LinkedMultiValueMap<String, String>(headers)), responseType, uriVariables);
    }

    protected <T> ResponseEntity<T> requestWithHttpHeaders(String url, HttpMethod method,
                                                           Map<String, List<String>> headers, Class<T> responseType, Object... uriVariables) {
        return restClient.exchange(baseUrl.concat(url), method, new HttpEntity<Object>(
                new LinkedMultiValueMap<String, String>(headers)), responseType, uriVariables);
    }

    protected <T> T execute(URI uri, HttpMethod method, final Map<? extends String, ? extends List<String>> headers,
                            final TypeReference<T> type) {

        return this.restClient.execute(uri, method, new RequestCallbackImpl(headers),
                new ResponseExtractorImpl<T>(type));
    }

    protected <T> T execute(String url, HttpMethod method, final Map<? extends String, ? extends List<String>> headers,
                            final TypeReference<T> type, Map<String, ?> urlParameter) {

        return this.restClient.execute(baseUrl.concat(url), method, new RequestCallbackImpl(headers),
                new ResponseExtractorImpl<T>(type), urlParameter);
    }

    protected <T> T execute(String url, HttpMethod method, final Map<? extends String, ? extends List<String>> headers,
                            final TypeReference<T> type, Object... urlParameter) {
        return this.restClient.execute(baseUrl.concat(url), method, new RequestCallbackImpl(headers),
                new ResponseExtractorImpl<T>(type), urlParameter);
    }

    private static class RequestCallbackImpl implements RequestCallback {

        private Map<? extends String, ? extends List<String>> headers;

        RequestCallbackImpl(Map<? extends String, ? extends List<String>> headers) {
            super();
            this.headers = headers;
        }

        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (headers == null || headers.isEmpty()) {
                return;
            }
            request.getHeaders().putAll(headers);
        }

    }

    private static class ResponseExtractorImpl<T> implements ResponseExtractor<T> {
        private TypeReference<T> type;

        ResponseExtractorImpl(TypeReference<T> type) {
            super();
            this.type = type;
        }

        public T extractData(ClientHttpResponse response) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.disable(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES);
            return mapper.readValue(IOUtils.toString(response.getBody(), "UTF-8"), type);
        }
    }

}
