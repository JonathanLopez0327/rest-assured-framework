package org.framework.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.http.Header;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExtentReportManager {
    public static ExtentReports extentReports;

    public static ExtentReports createInstance(String fileName, String reportName, String documentTitle) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);

        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle(documentTitle);
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setReportName(reportName);
        sparkReporter.config().setCss("#uniqueValue {color: #6495ED;}");

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("Release No", "22");
        extentReports.setSystemInfo("Environment", "SQA");
        extentReports.setSystemInfo("Build No", "B-12673");

        return extentReports;
    }

    public static String getReportNameWithTimeStamp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(localDateTime);
        String reportName = "Test-Report-" + timeStamp + ".html";
        return reportName;
    }

    public static void logPassDetails(String log) {
        ExtentListener.extentTest.get().pass(MarkupHelper.createLabel(log, ExtentColor.GREEN));
    }

    public static void logFailureDetails(String log) {
        ExtentListener.extentTest.get().fail(MarkupHelper.createLabel(log, ExtentColor.RED));
    }

    public static void logExceptionDetails(String log) {
        ExtentListener.extentTest.get().fail(log);
    }

    public static void logInfoDetails(String log) {
        ExtentListener.extentTest.get().info(log);
    }

    public static void logWarningDetails(String log) {
        ExtentListener.extentTest.get().warning(MarkupHelper.createLabel(log, ExtentColor.ORANGE));
    }

    public static void logJson(String json) {
        ExtentListener.extentTest.get().info(MarkupHelper.createCodeBlock(json, CodeLanguage.JSON));
    }

    public static void logHeaders(List<Header> headersList) {
        String[][] arrayHeaders = headersList.stream()
                .map(header -> new String[] {"<span id='uniqueName'>" + header.getName() + "</span>", "<span id='uniqueValue'>" + header.getValue() + "</span>"})
                .toArray(String[][]::new);
        ExtentListener.extentTest.get().info(MarkupHelper.createTable(arrayHeaders));
    }
}
