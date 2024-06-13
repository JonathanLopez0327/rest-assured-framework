package org.framework.specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.framework.config.TokenParams;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


/**
 * This class provides methods to build different types of request specifications.
 */
public class SpecificationsConfig {

    /**
     * Builds a normal request specification with base URI and base path.
     *
     * @param basePath the base path of the request
     * @return a RequestSpecBuilder instance
     */
    public RequestSpecBuilder buildNormalRequestSpecBuilder(String baseUrl, String basePath, ContentType contentType) {
        return new RequestSpecBuilder().
                setBaseUri(baseUrl)
                .setContentType(contentType)
                .setBasePath(basePath);
    }

    /**
     * Builds a request specification with bearer token.
     *
     * @param baseUrl     the base URL of the request
     * @param basePath    the base path of the request
     * @param contentType the content type of the request
     * @param token       the bearer token
     * @return a RequestSpecBuilder instance
     */
    public RequestSpecBuilder buildRequestWithBearerToken(String baseUrl, String basePath, ContentType contentType, String token) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath(basePath)
                .setContentType(contentType)
                .addHeader("Authorization", "Bearer " + token);
    }

    /**
     * Builds a request specification with URL encoded parameters.
     *
     * @param baseUrl       the base URL of the request
     * @param basePath      the base path of the request
     * @param requestParams the request parameters
     * @return a RequestSpecBuilder instance
     */
    public RequestSpecBuilder buildRequestWithUrlEncodedParams(String baseUrl, String basePath, Map<String, String> requestParams) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath(basePath)
                .setContentType(ContentType.URLENC);

        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            builder.addFormParam(entry.getKey(), entry.getValue());
        }

        return builder;
    }

    /**
     * Builds a map of request parameters from a TokenParams object.
     *
     * @param tokenParams the TokenParams object
     * @return a map of request parameters
     */
    public static Map<String, String> buildRequestParams(TokenParams tokenParams) {
        Map<String, String> requestParams = new HashMap<>();

        if (tokenParams.getGrantType() != null) {
            requestParams.put("grant_type", tokenParams.getGrantType());
        }
        if (tokenParams.getUsername() != null) {
            requestParams.put("username", tokenParams.getUsername());
        }
        if (tokenParams.getPassword() != null) {
            requestParams.put("password", tokenParams.getPassword());
        }
        if (tokenParams.getClientId() != null) {
            requestParams.put("client_id", tokenParams.getClientId());
        }
        if (tokenParams.getClientSecret() != null) {
            requestParams.put("client_secret", tokenParams.getClientSecret());
        }
        if (tokenParams.getScope() != null) {
            requestParams.put("scope", tokenParams.getScope());
        }

        return requestParams;
    }

    /**
     * Generates a token from a given request specification.
     *
     * @param requestSpec the request specification
     * @return the generated token
     */
    public String generateToken(RequestSpecification requestSpec) {
        Response response = given()
                .spec(requestSpec)
                .when()
                .post();

        return response.jsonPath().getString("access_token");
    }
}
