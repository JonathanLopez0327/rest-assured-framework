package org.framework.trackingservice.accounts;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.framework.report.LogginReport;
import org.framework.Specifications.SpecificationsUtils;
import org.framework.trackingservice.AccountParams;
import org.framework.utils.Token;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class AccountServiceTest {
    private static String token;
    private RequestSpecification requestSpecification;
    private Response response;

    private Map<String, String> tokenParams = Map.of(
            Token.GRANT_TYPE, "password",
            Token.USERNAME, "dev-1",
            Token.PASSWORD, "20032718",
            Token.SCOPE, "openid email profile"
    );

    private Map<String, String> accountParams(AccountParams accountParams) {
        return Map.of(
                "accountName", accountParams.getAccountName(),
                "accountDescription", accountParams.getAccountDescription(),
                "accountType", accountParams.getAccountType(),
                "totalAmount", String.valueOf(accountParams.getTotalAmount())
        );
    }

    @BeforeMethod
    void generate_token() {
        response = SpecificationsUtils.generateAuthToken(
                "budget-service-client",
                "f0B2Ykh6wPjyhUkOtt0cfBQvXqqp3ucL",
                "http://localhost:8080" + "/realms/dev-budget-realm/protocol/openid-connect/token",
                tokenParams
        );
        token = response.jsonPath().getString("access_token");
    }

    @Test(description = "Get all accounts")
    void get_all_accounts() {
        requestSpecification = SpecificationsUtils
                .buildSecureRequest(token,"http://localhost:8000", "/account", ContentType.JSON, null, null);

        Response response = requestSpecification.get();

        LogginReport.printRequestLogInReport(requestSpecification);
        LogginReport.printResponseLogInReport(response);
        assertEquals(response.getStatusCode(), 200, "Expected status code is 200.");
    }

    @Test(description = "Adding new account")
    void add_new_account() {
        AccountParams accountParam = new AccountParams("Test Account 3", "Test Description 4", "CASH", 5000);

        requestSpecification = SpecificationsUtils
                .buildSecureRequest(token,"http://localhost:8000", "/account", ContentType.JSON, accountParams(accountParam), null);

        Response response = requestSpecification.post();

        LogginReport.printRequestLogInReport(requestSpecification);
        LogginReport.printResponseLogInReport(response);

        assertEquals(response.getStatusCode(), 201, "Expected status code is 201.");
    }

    @Test(description = "Update new account")
    void update_account() {
        long id = 52;
        AccountParams accountParam = new AccountParams("Updated Name", "Update Description", "CASH", 5500);

        requestSpecification = SpecificationsUtils
                .buildSecureRequest(token,"http://localhost:8000", "/account/" + id, ContentType.JSON, accountParams(accountParam), null);

        Response response = requestSpecification.put();

        LogginReport.printRequestLogInReport(requestSpecification);
        LogginReport.printResponseLogInReport(response);

        assertEquals(response.getStatusCode(), 200, "Expected status code is 200.");
    }

    @Test(description = "Delete new account")
    void delete_account() {
        long id = 52;
        requestSpecification = SpecificationsUtils
                .buildSecureRequest(token,"http://localhost:8000", "/account/" + id, ContentType.JSON, null, null);

        Response response = requestSpecification.delete();

        LogginReport.printRequestLogInReport(requestSpecification);
        LogginReport.printResponseLogInReport(response);

        assertEquals(response.getStatusCode(), 200, "Expected status code is 200.");
    }
}
