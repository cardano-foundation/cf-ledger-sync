package org.cardanofoundation.configs.restassuredconfigs;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.logging.LogRecord;

public class RequestResponseFilter implements Filter {


    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        Allure.addAttachment("Request Details: ",
                "HTTP method: " + requestSpec.getMethod() + "\n" +
                        "URI: " + requestSpec.getURI() + "\n" +
                        "Headers:\n" + requestSpec.getHeaders() + "\n" +
                        "Payload (if applicable): " + requestSpec.getBody() + "\n" +
                        "Path params (if applicable): " + requestSpec.getPathParams() + "\n" +
                        "Query params (if applicable): " + requestSpec.getQueryParams() + "\n" +
                        "Form params (if applicable): " + requestSpec.getFormParams());

        Allure.addAttachment("Response Details: ",
                "Status code: " + response.getStatusCode() + "\n" +
                        "Status line: " + response.statusLine() + "\n" +
                        "Response time: " + response.time() + "\n" +
                        "Response headers: " + response.headers() + "\n" +
                        "Response json:\n" + response.getBody().asPrettyString());

        return response;
    }
}
