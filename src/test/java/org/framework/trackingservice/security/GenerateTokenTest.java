package org.framework.trackingservice.security;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.framework.report.LogginReport;
import org.framework.Specifications.SpecificationsUtils;
import org.framework.utils.Token;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class GenerateTokenTest {

    private RequestSpecification requestSpecification;
    private Response response;
    private static final String BASE_URL = "http://localhost:8080";
    private static final String BASE_PATH = "/realms/dev-budget-realm/protocol/openid-connect/token";

    private Map<String, String> tokenParams = Map.of(
            Token.GRANT_TYPE, "password",
            Token.USERNAME, "dev-1",
            Token.PASSWORD, "20032718",
            Token.SCOPE, "openid email profile"
    );

    @Test(description = "Test if the WebSecurity class is correctly implemented")
    void testWebSecurity() {
        response = SpecificationsUtils.generateAuthToken(
                "budget-service-client",
                "f0B2Ykh6wPjyhUkOtt0cfBQvXqqp3ucL",
                BASE_URL + BASE_PATH,
                tokenParams
        );

        LogginReport.printResponseLogInReport(response);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200.");
    }
}
