package utils;

import io.restassured.http.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestModel {
    private String baseUrl;
    private String basePath;
    private ContentType contentType;
    private Map<String, String> headers;
    private Map<String, String> bodyParams;
    private Map<String, String> queryParams;
    private Protocols protocol;
    private String token;
    private int timeout;
}
