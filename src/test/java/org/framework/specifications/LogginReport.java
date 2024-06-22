package org.framework.specifications;

import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.framework.report.ExtentReportManager;

public class LogginReport {

    private static final String RESPONSE_STATUS_IS = "Response status is: ";
    private static final String RESPONSE_BODY_IS = "Response body is: ";

    public static void printRequestLogInReport(RequestSpecification requestSpecification) {
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.query(requestSpecification);
        ExtentReportManager.logInfoDetails("Endpoint is: " + "<span id='uniqueValue'>" + queryableRequestSpecification.getBaseUri() + queryableRequestSpecification.getBasePath() + "</span>");
        ExtentReportManager.logInfoDetails("Method is: " + "<span id='uniqueValue'>" + queryableRequestSpecification.getMethod()+ "</span>");

        StringBuilder headersInfo = new StringBuilder("Request Headers: <br>");
        queryableRequestSpecification.getHeaders().asList().forEach(header -> {
            headersInfo.append("<span id='uniqueName'>").append(header.getName()).append("</span>: <span id='uniqueValue'>").append(header.getValue()).append("</span><br>");
        });
        ExtentReportManager.logInfoDetails(headersInfo.toString());

        if (queryableRequestSpecification.getBody() != null) {
            ExtentReportManager.logInfoDetails("Request body is: ");
            ExtentReportManager.logJson(queryableRequestSpecification.getBody());
        }
    }

    public static void printResponseLogInReport(Response response) {
        ExtentReportManager.logInfoDetails(RESPONSE_STATUS_IS + response.getStatusCode());

        StringBuilder headersInfo = new StringBuilder("Response Headers: <br>");
        response.getHeaders().asList().forEach(header -> {
            headersInfo.append("<span id='uniqueName'>").append(header.getName()).append("</span>: <span id='uniqueValue'>").append(header.getValue()).append("</span><br>");
        });
        ExtentReportManager.logInfoDetails(headersInfo.toString());

        ExtentReportManager.logInfoDetails(RESPONSE_BODY_IS);
        ExtentReportManager.logJson(response.getBody().prettyPrint());
    }
}
