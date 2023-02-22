package org.cardanofoundation.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.cardanofoundation.utils.RestBase.KeyType.httpmethod;
import static org.cardanofoundation.utils.RestBase.RequestFunctions.clear;

@Component
public class RestBase {

    private ThreadLocal<Response> response = ThreadLocal.withInitial(()->null);
    private ThreadLocal<String> requestUrl = ThreadLocal.withInitial(()->"");
    private ThreadLocal<Boolean> removeBodyFromPost = ThreadLocal.withInitial(()-> false);
    private ThreadLocal<HashMap<String, Object>> headersMap = ThreadLocal.withInitial(HashMap::new);
    private ThreadLocal<DocumentContext> documentContext = ThreadLocal.withInitial(() -> null);
    protected ThreadLocal<HashMap<String, Object>> queryParamsMap = ThreadLocal.withInitial(HashMap::new);
    protected ThreadLocal<HashMap<String, Object>> formParamsMap = ThreadLocal.withInitial(HashMap::new);
    protected ThreadLocal<RequestSpecification> requestSpecification = ThreadLocal.withInitial(() -> null);
    protected ThreadLocal<RestBase.HttpMethods> overrideHttpMethod = ThreadLocal.withInitial(() -> null);

    public enum KeyType {
        httpmethod, requestpath, contenttype, header, body, parameter, formparameter, cookie
    }

    public enum RequestFunctions {
        remove, amend, clear
    }

    protected enum HttpMethods {
        GET, POST, PUT, DELETE
    }

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String API_HOSTNAME = "https://dev.cf-jkaur-cardano-wallet.metadata.dev.cf-deployments.org/v2";


    public void setHeaders(HashMap<String, Object> headers) {
        headersMap.get().clear();
        headersMap.set(headers);
    }

    public void post(String reqName, String url, boolean headers, boolean payload, boolean formParams, boolean queryParams) {
        buildRequest(url, headers, payload, formParams, queryParams);
        if (getOverrideHttpMethod() == null) {
            post(url);
        } else {
            overRideHttpMethod(url);
            overrideHttpMethod.set(null);
        }
    }

    private void overRideHttpMethod(String url) {

        if (getOverrideHttpMethod() == HttpMethods.GET) {
            setRequestSpecification(getRequestSpecification().body(""));
            get(url);
        } else if (getOverrideHttpMethod() == HttpMethods.POST) {
            setRequestSpecification(getRequestSpecification().body("{\"dummyKey\": \"dummyVal\"}"));
            post(url);
        } else if (getOverrideHttpMethod() == HttpMethods.DELETE) {
            setRequestSpecification(getRequestSpecification().body("{\"dummyKey\": \"dummyVal\"}"));
            delete(url);
        }else if (getOverrideHttpMethod() == HttpMethods.PUT) {
            setRequestSpecification(getRequestSpecification().body("{\"dummyKey\": \"dummyVal\"}"));
            put(url);
        }
    }

    private void put(String url) {
        try {
            setResponse(getRequestSpecification().when().put(url));
        } catch (Exception f) {
            System.out.println("Unable to execute put method for url - " +  url);
            throw f;
        }
        captureRequestResponse();
    }


    private void delete(String url) {
        try {
            setResponse(getRequestSpecification().when().delete(url));
        } catch (Exception f) {
            System.out.println("Unable to execute post delete for url - " + url);
            throw f;
        }
        captureRequestResponse();
    }



    private void get(String url) {
        try {
            setResponse(getRequestSpecification().urlEncodingEnabled(false).when().get(url));
        } catch (Exception f) {
            System.out.println("Unable to execute GET method for url - " + url);
            throw f;
        }

        captureRequestResponse();
    }



    private HttpMethods getOverrideHttpMethod() {
        return overrideHttpMethod.get();
    }

    private void post(String url) {
        try {
            setResponse(getRequestSpecification().when().log().all().post(url));
        } catch (Exception ex) {
            System.out.println("Unable to execute post method for url: "+url);
            ex.printStackTrace();
            throw ex;
        }

        System.out.println(getResponse().body().asString());
        captureRequestResponse();
    }

    private void captureRequestResponse() {
    }

    private void setResponse(Response res) {
        response.set(res);
    }

    private RequestSpecification getRequestSpecification() {
        return requestSpecification.get();
    }

    private void buildRequest(String url, boolean headers, boolean payload, boolean formParams, boolean queryParams) {

        setTargetUrl(url);

        RequestSpecification spec = given().
                filter(new RequestResponseFilter());

        RestAssuredConfig myConfig = setRequestConfig();
        spec.config(myConfig);

        if (payload && !getRemovePayloadFlag()) {
            spec.body(getDocumentContext().jsonString());
        }

        if (headers) {
            spec.headers(getHeadersFromMap());
        }

        if (queryParams) {
            spec.queryParams(getQueryParams());
        }

        if (formParams) {
            spec.formParams(getFormParams());
        }

        setRequestSpecification(spec);
    }

    private void setRequestSpecification(RequestSpecification spec) {
        requestSpecification.set(spec);
    }

    private HashMap<String, Object> getFormParams() {
        return formParamsMap.get();
    }

    private HashMap<String, Object> getQueryParams() {
        return queryParamsMap.get();
    }

    private Headers getHeadersFromMap() {
        List<Header> list = new ArrayList<>();
        for (String key : getHeaders().keySet()) {
            if (getHeaders().get(key)==null) {
                list.add(new Header(key, null));
            } else {
                list.add(new Header(key, getHeaders().get(key).toString()));
            }
        }
        return new Headers(list);
    }

    private HashMap<String, Object> getHeaders() {
        return headersMap.get();
    }

    private DocumentContext getDocumentContext() {
        return documentContext.get();
    }

    private void setDocumentContext(DocumentContext context) {
        documentContext.set(context);
    }

    private boolean getRemovePayloadFlag() {
        return removeBodyFromPost.get();
    }

    private RestAssuredConfig setRequestConfig() {
        RestAssuredConfig restAssuredConfig = new RestAssuredConfig();
        restAssuredConfig = restAssuredConfig.sslConfig(new SSLConfig().relaxedHTTPSValidation());
        return restAssuredConfig;
    }

    private void setTargetUrl(String url) {
        requestUrl.set(url);
    }

    public Response getResponse() {
        return response.get();
    }

    public void setPayload(String payload) {
        DocumentContext context = JsonPath.parse(payload);
        setDocumentContext(context);
    }

    public void amendHttpMethod(String httpMethod) {
        amendRequestSpecification(httpmethod, RequestFunctions.amend, "", httpMethod, "");
    }

    public void amendRequestSpecification(KeyType keyType, RequestFunctions function, String key, String amendedVal, String optionalJsonPath) {

        switch (keyType){
            case httpmethod ->
                setOverrideHttpMethod(HttpMethods.valueOf(amendedVal.toUpperCase()));
            case header -> {
                switch (function) {
                    case remove -> {
                        removeHeader(key);
                    }
                    case clear -> {
                        clearHeaders();
                    }
                }
            }
        }
    }

    private void clearHeaders() {
        headersMap.get().clear();
    }

    private void removeHeader(String key) {
        headersMap.get().remove(key);
    }

    private void setOverrideHttpMethod(HttpMethods method) {
        overrideHttpMethod.set(method);
    }
}
