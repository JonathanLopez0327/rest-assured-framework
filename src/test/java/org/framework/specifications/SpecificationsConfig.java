package org.framework.specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SpecificationsConfig {

    public RequestSpecBuilder buildNormalRequestSpecBuilder(String basePath) {
        return new RequestSpecBuilder().
                setBaseUri("http://localhost:8080").
                setBasePath(basePath);
    }

    public RequestSpecBuilder buildRequestWithBearerToken(String baseUrl, String basePath, String token) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath(basePath)
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

    public static Map<String, String> buildRequestParams(
            String grantType, String username,
            String password, String clientId,
            String clientSecret, String scope) {

        Map<String, String> requestParams = new HashMap<>();

        if (grantType != null) {
            requestParams.put("grant_type", grantType);
        }
        if (username != null) {
            requestParams.put("username", username);
        }
        if (password != null) {
            requestParams.put("password", password);
        }
        if (clientId != null) {
            requestParams.put("client_id", clientId);
        }
        if (clientSecret != null) {
            requestParams.put("client_secret", clientSecret);
        }
        if (scope != null) {
            requestParams.put("scope", scope);
        }

        return requestParams;
    }

    public String getToken(RequestSpecification requestSpec) {
        Response response = given()
                .spec(requestSpec)
                .when()
                .post();

        return response.jsonPath().getString("access_token");
    }
}
