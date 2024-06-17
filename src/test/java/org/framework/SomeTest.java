package org.framework;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.framework.config.TokenParams;
import org.framework.listeners.ExtentReportManager;
import org.framework.specifications.SpecificationsUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class SomeTest {

    private RequestSpecification requestSpec;

    TokenParams tokenParams = TokenParams.builder()
            .grantType("password")
            .username("dev-1")
            .password("20032718")
            .clientId("budget-service-client")
            .clientSecret("f0B2Ykh6wPjyhUkOtt0cfBQvXqqp3ucL")
            .scope("openid email profile")
            .build();

    private Map<String, String> requestParams = SpecificationsUtils.buildRequestParams(tokenParams);

    @BeforeMethod
    void createRequestSpec() {
        requestSpec = SpecificationsUtils
                .buildRequestWithUrlEncodedParams("http://localhost:8080", "/realms/dev-budget-realm/protocol/openid-connect/token", requestParams).build();
    }

    @Test
    void generate_token_test() {
        Response response = given()
                .spec(requestSpec)
                .contentType(ContentType.URLENC)
                .when()
                .post();

        SpecificationsUtils.printRequestLogInReport(requestSpec);
        SpecificationsUtils.printResponseLogInReport(response);

        assertEquals(response.getStatusCode(), 200, "Expected response code is 200");
    }
}
