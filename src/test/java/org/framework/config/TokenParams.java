package org.framework.config;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenParams {
    private String grantType;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String scope;
}
