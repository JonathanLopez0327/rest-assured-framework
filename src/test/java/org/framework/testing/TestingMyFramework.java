package org.framework.testing;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.framework.specifications.SpecificationsUtils;
import org.framework.testing.config.AccountParams;
import org.framework.testing.config.Builders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class TestingMyFramework {
    private static String token;
    private RequestSpecification requestSpecification;

    @BeforeMethod
    static void generate_token() {
        Response response = SpecificationsUtils
                .buildRequest("http://localhost:8080", "/realms/dev-budget-realm/protocol/openid-connect/token", Builders.tokenParams())
                .post();
        token = response.jsonPath().getString("access_token");
    }

    @Test(description = "Get all accounts")
    void get_all_accounts() {
        requestSpecification = SpecificationsUtils
                .buildRequest("http://localhost:8000", "/account", ContentType.JSON, Builders.headers(token));
        Response response = requestSpecification.get();
        SpecificationsUtils.printRequestLogInReport(requestSpecification);
        SpecificationsUtils.printResponseLogInReport(response);
        assertEquals(response.getStatusCode(), 200, "Expected status code is 200.");
    }

    @Test(description = "Adding new account")
    void add_new_account() {

        AccountParams accountParams = new AccountParams("Test Account 2", "Test Description 2", "CASH", 1000);

        requestSpecification =
                SpecificationsUtils
                        .buildRequest("http://localhost:8000", "/account", ContentType.JSON, Builders.headers(token), Builders.accountParams(accountParams));

        Response response = requestSpecification.post();

        SpecificationsUtils.printRequestLogInReport(requestSpecification);
        SpecificationsUtils.printResponseLogInReport(response);

        assertEquals(response.getStatusCode(), 201, "Expected status code is 200.");
    }
}
