package org.framework.specifications;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.framework.report.ExtentReportManager;
import org.framework.utils.Protocols;

import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;

/**
 * This class provides methods to build different types of request specifications.
 */
public class SpecificationsUtils {

    public static Response generateAuthToken(String clientId, String clientSecret, String endpoint, Map<String, String> formParams) {
        return RestAssured.given()
                .auth()
                .preemptive()
                .basic(clientId, clientSecret)
                .contentType(ContentType.URLENC)
                .formParams(formParams)
                .post(endpoint);
    }

    private static RequestSpecBuilder createRequestSpecBuilder(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath(basePath)
                .setContentType(contentType)
                .addHeaders(headers);
    }

    public static RequestSpecification buildRequest(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers, Map<String, String> bodyParams, Map<String, String> queryParams, ContentType bodyContentType) {
        RequestSpecBuilder builder = createRequestSpecBuilder(baseUrl, basePath, contentType, headers);

        Optional.ofNullable(bodyParams).ifPresent(params -> builder.setBody(params).setContentType(bodyContentType));
        Optional.ofNullable(queryParams).ifPresent(builder::addQueryParams);

        return given().spec(builder.build());
    }

    public static RequestSpecification buildSecureRequest(String accessToken, String baseUrl, String basePath, ContentType contentType, Map<String, String> headers, Map<String, String> bodyParams, Map<String, String> queryParams, ContentType bodyContentType) {
        RequestSpecBuilder builder = createRequestSpecBuilder(baseUrl, basePath, contentType, headers);

        Optional.ofNullable(bodyParams).ifPresent(params -> builder.setBody(params).setContentType(bodyContentType));
        Optional.ofNullable(queryParams).ifPresent(builder::addQueryParams);

        return given().auth().oauth2(accessToken).spec(builder.build());
    }

    public static RequestSpecification buildRequestWithRelaxedHttpsValidation(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers, Map<String, String> bodyParams, Map<String, String> queryParams, ContentType bodyContentType, Protocols protocol) {
        RequestSpecBuilder builder = createRequestSpecBuilder(baseUrl, basePath, contentType, headers);

        Optional.ofNullable(bodyParams).ifPresent(params -> builder.setBody(params).setContentType(bodyContentType));
        Optional.ofNullable(queryParams).ifPresent(builder::addQueryParams);

        return given().relaxedHTTPSValidation(protocol.toString()).spec(builder.build());
    }

    public static RequestSpecification buildRequest(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers, Map<String, String> bodyParams) {
        return buildRequest(baseUrl, basePath, contentType, headers, bodyParams, null, null);
    }

    public static RequestSpecification buildRequest(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers) {
        return buildRequest(baseUrl, basePath, contentType, headers, null, null, null);
    }

    public static RequestSpecification buildRequest(String baseUrl, String basePath, Map<String, String> requestParams) {
        return buildRequest(baseUrl, basePath, ContentType.URLENC, null, requestParams, null, null);
    }
}