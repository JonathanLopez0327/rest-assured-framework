package org.framework.testing.config;

import java.util.Map;

public class Builders {
    public static Map<String, String> tokenParams() {
        return Map.of(
                "grant_type", "password",
                "username", "dev-1",
                "password", "20032718",
                "client_id", "budget-service-client",
                "client_secret", "f0B2Ykh6wPjyhUkOtt0cfBQvXqqp3ucL",
                "scope", "openid email profile"
        );
    }

    public static Map<String, String> headers(String token) {
        return Map.of(
                "Authorization", "Bearer " + token);
    }

    public static Map<String, String> accountParams(AccountParams accountParams) {
        return Map.of(
                "accountName", accountParams.getAccountName(),
                "accountDescription", accountParams.getAccountDescription(),
                "accountType", accountParams.getAccountType(),
                "totalAmount", String.valueOf(accountParams.getTotalAmount())
        );
    }
}
