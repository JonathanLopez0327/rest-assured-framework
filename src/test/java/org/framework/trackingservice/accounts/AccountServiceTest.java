package org.framework.trackingservice.accounts;

import SpecConfig.SpecificationsUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class AccountServiceTest {
    private static String token;
    private Response response;

    @BeforeMethod
    void generate_token() {
        token = SpecificationsUtils
                .generateAuthToken(AccountService.getInstance().generateAuth());
    }

    private Map<String, String> responseParams() {
        return Map.of(
                "Addresses[0].Building", "2",
                "Addresses[0].CityCode", "01",
                "Addresses[0].pyStreet", "AV. INDEPENDENCIA"
        );
    }

    @Test(description = "Get all accounts")
    void get_info_client() {
        response = SpecificationsUtils
                .sendGetRequest(
                        AccountService.getInstance().generateRequest(token),
                        true, false);

        Assert.assertNotNull(SpecificationsUtils.assertStatusCode(response, 200));
        Assert.assertNotNull(SpecificationsUtils.assertResponseBody(response, "Addresses", "Building"));
        Assert.assertNotNull(SpecificationsUtils.assertResponseBodyContains(response, responseParams()));
    }


}
