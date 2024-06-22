package org.framework.testing;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SecurityTesting {

    @Test(description = "Test if the WebSecurity class is correctly implemented")
    void testWebSecurity() {
        given()
                .auth().basic("dev-1", "20032718") // Provide credentials
                .when().post("http://localhost:8080/realms/dev-budget-realm").then().statusCode(200);
    }
}
