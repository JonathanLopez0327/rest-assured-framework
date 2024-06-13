package org.framework;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.framework.config.TokenParams;
import org.framework.specifications.SpecificationsConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class SomeTest {

    private static RequestSpecification requestSpec;
    private static String token;

    static TokenParams tokenParams = TokenParams.builder()
            .grantType("password")
            .username("dev-1")
            .password("20032718")
            .clientId("budget-service-client")
            .clientSecret("f0B2Ykh6wPjyhUkOtt0cfBQvXqqp3ucL")
            .scope("openid profile email")
            .build();

    private static Map<String, String> requestParams = SpecificationsConfig.buildRequestParams(tokenParams);

    @BeforeClass
    static void createRequestSpecification() {
        final String BASE_URL = "http://localhost:8080";
        final String TOKEN_PATH = "/realms/dev-budget-realm/protocol/openid-connect/token";
        final String ACCOUNTS_PATH = "/account";

        SpecificationsConfig specificationsConfig = new SpecificationsConfig();

        RequestSpecification tokenSpecifications = specificationsConfig
                .buildRequestWithUrlEncodedParams(BASE_URL, TOKEN_PATH, requestParams)
                .build();

        token = specificationsConfig.generateToken(tokenSpecifications);

        requestSpec = specificationsConfig
                .buildRequestWithBearerToken("http://localhost:8000", ACCOUNTS_PATH, ContentType.JSON, token)
                .build();
    }

    @Test
    void request_to_get_token() {
        given()
                .spec(requestSpec)
                .when()
                .get()
                .then()
                .statusCode(200);
    }
}
