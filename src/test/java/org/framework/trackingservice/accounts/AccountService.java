package org.framework.trackingservice.accounts;

import io.restassured.http.ContentType;
import utils.AuthModel;
import utils.RequestModel;

import java.util.Map;

public class AccountService {

    private static final String TOKEN_URL = "https://bhd-bhdcrm-stg3.pegacloud.net/prweb/PRRestService/oauth2/v1/token";
    private static final String CLIENT_ID = "13074170966534969417";
    private static final String CLIENT_SECRET = "20061992026DF337CCA5E77D444017EE";

    private static final String GET_CLIENT_BASE_URL = "https://bhd-bhdcrm-stg3.pegacloud.net";
    private static final String GET_CLIENT_BASE_PATH = "/prweb/PRRestService/BHDSOR/v1/MapContactDetails";

    private static AccountService instance = null;

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    private AccountService() {
        // private constructor
    }

    public AuthModel generateAuth() {
        return AuthModel.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .endpoint(TOKEN_URL)
                .params(Map.of("grant_type", "client_credentials"))
                .build();
    }

    public RequestModel generateRequest(String token) {
        return RequestModel.builder()
                .baseUrl(GET_CLIENT_BASE_URL)
                .basePath(GET_CLIENT_BASE_PATH)
                .queryParams(Map.of("PersonalChannelID", "00100000181"))
                .token(token)
                .build();
    }
}
