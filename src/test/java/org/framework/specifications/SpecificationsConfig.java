package org.framework.specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.framework.config.TokenParams;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SpecificationsConfig {

    public RequestSpecBuilder buildNormalRequestSpecBuilder(String basePath) {
        return new RequestSpecBuilder().
                setBaseUri("http://localhost:8080").
                setBasePath(basePath);
    }

    public RequestSpecBuilder buildRequestWithBearerToken(String baseUrl, String basePath, ContentType contentType, String token) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath(basePath)
                .setContentType(contentType)
                .addHeader("Authorization", "Bearer " + token);
    }

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

    public String generateToken(RequestSpecification requestSpec) {
        Response response = given()
                .spec(requestSpec)
                .when()
                .post();

        return response.jsonPath().getString("access_token");
    }
}
