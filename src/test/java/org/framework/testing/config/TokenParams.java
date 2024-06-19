package org.framework.testing.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenParams {
    private String grantType;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String scope;
}
