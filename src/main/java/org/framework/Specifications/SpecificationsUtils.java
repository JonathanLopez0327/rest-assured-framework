package org.framework.Specifications;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.framework.utils.Protocols;

import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;

/**
 * This class provides methods to build different types of request specifications.
 */
public class SpecificationsUtils {

    private static SpecificationsUtils instance = null;

    public static SpecificationsUtils getInstance() {
        if (instance == null) {
            instance = new SpecificationsUtils();
        }
        return instance;
    }

    private SpecificationsUtils() {
    }


    /**
     * Generates an authentication token using the provided client credentials and form parameters.
     *
     * @param clientId     the client ID
     * @param clientSecret the client secret
     * @param endpoint     the token endpoint URL
     * @param formParams   the form parameters for the token request
     * @return the response containing the authentication token
     */
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

    private static RequestSpecBuilder createRequestSpecBuilder(String baseUrl, String basePath, ContentType contentType, String token) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath(basePath)
                .setContentType(contentType)
                .setAuth(RestAssured.oauth2(token));
    }


    /**
     * Builds a request specification with the provided parameters.
     *
     * @param baseUrl     the base URL
     * @param basePath    the base path
     * @param contentType the content type
     * @param headers     the headers
     * @param bodyParams  the body parameters
     * @param queryParams the query parameters
     * @return the request specification
     */
    public static RequestSpecification buildRequest(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers, Map<String, String> bodyParams, Map<String, String> queryParams) {
        RequestSpecBuilder builder = createRequestSpecBuilder(baseUrl, basePath, contentType, headers);

        Optional.ofNullable(bodyParams).ifPresent(params -> builder.setBody(params).setContentType(contentType));
        Optional.ofNullable(queryParams).ifPresent(builder::addQueryParams);

        return given().spec(builder.build());
    }

    /**
     * Builds a secure request specification with the provided parameters and access token.
     *
     * @param accessToken the access token
     * @param baseUrl     the base URL
     * @param basePath    the base path
     * @param contentType the content type
     * @param bodyParams  the body parameters
     * @param queryParams the query parameters
     * @return the secure request specification
     */
    public static RequestSpecification buildSecureRequest(String accessToken, String baseUrl, String basePath, ContentType contentType, Map<String, String> bodyParams, Map<String, String> queryParams) {
        RequestSpecBuilder builder = createRequestSpecBuilder(baseUrl, basePath, contentType, accessToken);

        Optional.ofNullable(bodyParams).ifPresent(params -> builder.setBody(params).setContentType(contentType));
        Optional.ofNullable(queryParams).ifPresent(builder::addQueryParams);

        return given().auth().oauth2(accessToken).spec(builder.build());
    }

    /**
     * Builds a request specification with relaxed HTTPS validation.
     *
     * @param baseUrl     the base URL
     * @param basePath    the base path
     * @param contentType the content type
     * @param headers     the headers
     * @param bodyParams  the body parameters
     * @param queryParams the query parameters
     * @param protocol    the protocol for HTTPS validation
     * @return the request specification with relaxed HTTPS validation
     */
    public static RequestSpecification buildRequestWithRelaxedHttpsValidation(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers, Map<String, String> bodyParams, Map<String, String> queryParams, Protocols protocol) {
        RequestSpecBuilder builder = createRequestSpecBuilder(baseUrl, basePath, contentType, headers);

        Optional.ofNullable(bodyParams).ifPresent(params -> builder.setBody(params).setContentType(contentType));
        Optional.ofNullable(queryParams).ifPresent(builder::addQueryParams);

        return given().relaxedHTTPSValidation(protocol.toString()).spec(builder.build());
    }

    /**
     * Builds a request specification with the provided parameters.
     *
     * @param baseUrl     the base URL
     * @param basePath    the base path
     * @param contentType the content type
     * @param headers     the headers
     * @param bodyParams  the body parameters
     * @return the request specification
     */
    public static RequestSpecification buildRequest(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers, Map<String, String> bodyParams) {
        return buildRequest(baseUrl, basePath, contentType, headers, bodyParams, null);
    }

    /**
     * Builds a request specification with the provided parameters.
     *
     * @param baseUrl     the base URL
     * @param basePath    the base path
     * @param contentType the content type
     * @param headers     the headers
     * @return the request specification
     */
    public static RequestSpecification buildRequest(String baseUrl, String basePath, ContentType contentType, Map<String, String> headers) {
        return buildRequest(baseUrl, basePath, contentType, headers, null, null);
    }

    /**
     * Builds a request specification with the provided parameters.
     *
     * @param baseUrl       the base URL
     * @param basePath      the base path
     * @param requestParams the request parameters
     * @return the request specification
     */
    public static RequestSpecification buildRequest(String baseUrl, String basePath, Map<String, String> requestParams) {
        return buildRequest(baseUrl, basePath, ContentType.URLENC, null, requestParams, null);
    }
}