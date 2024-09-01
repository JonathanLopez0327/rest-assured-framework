package utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthModel {
    private String clientId;
    private String clientSecret;
    private String endpoint;
    private Map<String, String> params;
}
