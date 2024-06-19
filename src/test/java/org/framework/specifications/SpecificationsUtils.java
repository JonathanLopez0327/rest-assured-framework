package org.framework.specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.framework.report.ExtentReportManager;

import java.util.Map;
import static io.restassured.RestAssured.given;

/**
 * This class provides methods to build different types of request specifications.
 */
public class SpecificationsUtils {

    public static RequestSpecification buildRequestWithHeaders(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers) {
        return given()
                .baseUri(baseUrl)
                .basePath(basePath)
                .contentType(contentType)
                .headers(headers);
    }

    public static RequestSpecification buildRequestAddingUrlEncodedParams(String baseUrl, String basePath, Map<String, String> requestParams) {
        return given()
                .baseUri(baseUrl)
                .basePath(basePath)
                .contentType(ContentType.URLENC)
                .formParams(requestParams);
    }

    public static RequestSpecification buildRequestAddingJsonBody(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers, Map<String, String> jsonBody) {
        return given()
                .baseUri(baseUrl)
                .basePath(basePath)
                .contentType(contentType)
                .headers(headers)
                .body(jsonBody);
    }

    public static RequestSpecBuilder buildRequestAddingQueryParameters(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers, Map<String, String> queryParams, Map<String, String> bodyParams) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath(basePath)
                .setContentType(contentType)
                .addHeaders(headers)
                .addQueryParams(queryParams)
                .setBody(bodyParams);
    }

    public static void printRequestLogInReport(RequestSpecification requestSpecification) {
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.query(requestSpecification);
        ExtentReportManager.logInfoDetails("Endpoint is: " + "<span id='uniqueValue'>" + queryableRequestSpecification.getBaseUri() + queryableRequestSpecification.getBasePath() + "</span>");
        ExtentReportManager.logInfoDetails("Method is: " + "<span id='uniqueValue'>" + queryableRequestSpecification.getMethod()+ "</span>");

        StringBuilder headersInfo = new StringBuilder("Request Headers: <br>");
        queryableRequestSpecification.getHeaders().asList().forEach(header -> {
            headersInfo.append("<span id='uniqueName'>").append(header.getName()).append("</span>: <span id='uniqueValue'>").append(header.getValue()).append("</span><br>");
        });
        ExtentReportManager.logInfoDetails(headersInfo.toString());

        if (queryableRequestSpecification.getBody() != null) {
            ExtentReportManager.logInfoDetails("Request body is: ");
            ExtentReportManager.logJson(queryableRequestSpecification.getBody());
        }
    }

    public static void printResponseLogInReport(Response response) {
        ExtentReportManager.logInfoDetails("Response status is: " + response.getStatusCode());

        StringBuilder headersInfo = new StringBuilder("Response Headers: <br>");
        response.getHeaders().asList().forEach(header -> {
            headersInfo.append("<span id='uniqueName'>").append(header.getName()).append("</span>: <span id='uniqueValue'>").append(header.getValue()).append("</span><br>");
        });
        ExtentReportManager.logInfoDetails(headersInfo.toString());

        ExtentReportManager.logInfoDetails("Response body is: ");
        ExtentReportManager.logJson(response.getBody().prettyPrint());
    }


}
