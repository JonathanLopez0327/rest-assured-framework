package SpecConfig;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import report.LogginReport;
import utils.AuthModel;
import utils.RequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * This class provides methods to build different types of request specifications.
 */
public class SpecificationsUtils {

    private static SpecificationsUtils instance = null;

    public static SpecificationsUtils getInstance() {
        if (instance == null) {
            instance = new SpecificationsUtils();
        }
        return instance;
    }

    private SpecificationsUtils() {
    }

    private static HttpClientConfig restAssuredConfig(int timeout) {
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .setSocketTimeout(timeout)
                    .build();

            return HttpClientConfig.httpClientConfig()
                    .httpClientFactory(() -> HttpClientBuilder.create()
                            .setDefaultRequestConfig(requestConfig)
                            .build());
        } catch (Exception e) {
            System.out.println("Error while setting up the HttpClientConfig: " + e.getMessage());
            return null;
        }
    }

    private static RequestSpecBuilder createRequestSpecBuilder(RequestModel request) {
        try {
            RequestSpecBuilder builder = new RequestSpecBuilder()
                    .setBaseUri(request.getBaseUrl())
                    .setBasePath(request.getBasePath());

            if (request.getContentType() != null) {
                builder.setContentType(ContentType.JSON);
            }

            if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
                builder.addHeaders(request.getHeaders());
            }

            if (request.getToken() != null) {
                builder.setAuth(RestAssured.oauth2(request.getToken()));
            }

            if (request.getTimeout() != 0) {
                builder.setConfig(RestAssuredConfig.config().httpClient(restAssuredConfig(request.getTimeout())));
            }

            return builder;
        } catch (Exception e) {
            System.out.println("Error while creating the RequestSpecBuilder: " + e.getMessage());
            return null;
        }
    }

    public static RequestSpecification buildRequest(RequestModel request) {
        if (request == null) {
            throw new NullPointerException("Request parameter cannot be null");
        }

        try {
            RequestSpecBuilder builder = createRequestSpecBuilder(request);
            if (builder == null) {
                throw new IllegalArgumentException("Error creating RequestSpecBuilder");
            }

            Optional.ofNullable(request.getBodyParams()).ifPresent(params -> builder.setBody(params).setContentType(request.getContentType()));
            Optional.ofNullable(request.getQueryParams()).ifPresent(builder::addQueryParams);

            RequestSpecification requestSpecification;
            if (request.getToken() != null && request.getProtocol() != null) {
                requestSpecification = given().auth().oauth2(request.getToken())
                        .relaxedHTTPSValidation(request.getProtocol().toString()).spec(builder.build());
            } else if (request.getToken() != null) {
                requestSpecification = given().auth().oauth2(request.getToken()).spec(builder.build());
            } else if (request.getProtocol() != null) {
                requestSpecification = given().relaxedHTTPSValidation(request.getProtocol().toString()).spec(builder.build());
            } else {
                requestSpecification = given().spec(builder.build());
            }

            return requestSpecification;
        } catch (Exception e) {
            System.out.println("Error while building the request: " + e.getMessage());
            return null;
        }
    }


    public static String generateAuthToken(AuthModel auth) {
        try {
            Response response = given()
                    .auth()
                    .preemptive()
                    .basic(auth.getClientId(), auth.getClientSecret())
                    .contentType(ContentType.URLENC)
                    .formParams(auth.getParams())
                    .post(auth.getEndpoint());

            return response.jsonPath().getString("access_token");
        } catch (Exception e) {
            System.out.println("Error while generating the auth token: " + e.getMessage());
            return null;
        }
    }

    private static Response sendRequest(RequestModel request, Method method, boolean logRequest, boolean logResponse) {
        RequestSpecification requestSpec = buildRequest(request);
        if (requestSpec == null) {
            System.out.println("Error while building the request specification");
            return null;
        }

        Response response;
        switch (method) {
            case POST:
                response = requestSpec.post();
                break;
            case GET:
                response = requestSpec.get();
                break;
            case PUT:
                response = requestSpec.put();
                break;
            case DELETE:
                response = requestSpec.delete();
                break;
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }

        if (logRequest) {
            LogginReport.printRequestLogInReport(requestSpec);
        }

        if (logResponse) {
            LogginReport.printResponseLogInReport(response);
        }

        return response;
    }

    public static Response sendPostRequest(RequestModel request, boolean logRequest, boolean logResponse) {
        return sendRequest(request, Method.POST, logRequest, logResponse);
    }

    public static Response sendGetRequest(RequestModel request, boolean logRequest, boolean logResponse) {
        return sendRequest(request, Method.GET, logRequest, logResponse);
    }

    public static Response sendPutRequest(RequestModel request, boolean logRequest, boolean logResponse) {
        return sendRequest(request, Method.PUT, logRequest, logResponse);
    }

    public static Response sendDeleteRequest(RequestModel request, boolean logRequest, boolean logResponse) {
        return sendRequest(request, Method.DELETE, logRequest, logResponse);
    }

    // Assertions


    public static ValidatableResponse assertStatusCode(Response response, int expectedStatusCode) {
        return response.then().assertThat().statusCode(expectedStatusCode);
    }

    public static ValidatableResponse assertResponseBodyContains(Response response, String property, String expectedValue) {
        return response.then().assertThat().body(property, equalTo(expectedValue));
    }

    public static ValidatableResponse assertResponseBody(Response response, String property) {
        return response.then().assertThat().body("$", hasKey(property));
    }

    public static ValidatableResponse assertResponseBody(Response response, List<String> properties) {
        ValidatableResponse validatableResponse = response.then().assertThat();
        for (String property : properties) {
            validatableResponse.body("$", hasItem(hasKey(property)));
        }
        return validatableResponse;
    }

    public static ValidatableResponse assertResponseBody(Response response, String path, String property) {
        return response.then().assertThat().body(path, hasItem(hasKey(property)));
    }

    public static ValidatableResponse assertResponseBody(Response response, String path, List<String> properties) {
        ValidatableResponse validatableResponse = response.then().assertThat();
        for (String property : properties) {
            validatableResponse.body(path, hasItem(hasKey(property)));
        }
        return validatableResponse;
    }

    public static ValidatableResponse assertResponseBodyContains(Response response, Map<String, String> expectedValues) {
        ValidatableResponse validatableResponse = response.then().assertThat();
        for (Map.Entry<String, String> entry : expectedValues.entrySet()) {
            validatableResponse.body(entry.getKey(), equalTo(entry.getValue()));
        }
        return validatableResponse;
    }

    public static ValidatableResponse assertResponseCookiesContains(Response response, String property, String expectedValue) {
        return response.then().assertThat().cookie(property, equalTo(expectedValue));
    }

    public static ValidatableResponse assertResponseCookies(Response response, String property) {
        return response.then().assertThat().cookie(property);
    }

    public static ValidatableResponse assertResponseCookies(Response response, String[] properties) {
        ValidatableResponse validatableResponse = response.then().assertThat();
        for (String property : properties) {
            validatableResponse.cookie(property);
        }
        return validatableResponse;
    }
}