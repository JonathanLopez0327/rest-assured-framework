package org.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

public class ExtentListener implements ITestListener {

    private static ExtentReports extentReports;
    public  static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public void onStart(ITestContext context) {
        String fileName = ExtentReportManager.getReportNameWithTimeStamp();
        String fullReportPath = System.getProperty("user.dir") + "\\Reports\\" + fileName;
        extentReports = ExtentReportManager.createInstance(fullReportPath, "Name", "Title");
    }

    public void onTestStart(ITestResult result) {
        ExtentTest test = extentReports
                .createTest("Test case " + result.getTestClass().getName() + " :: " + result.getMethod().getMethodName());
        extentTest.set(test);
    }

    public void onTestFailure(ITestResult result) {
        ExtentReportManager.logFailureDetails(result.getThrowable().getMessage());
        String stackTrace = Arrays.toString(result.getThrowable().getStackTrace());
        stackTrace = stackTrace.replaceAll(",", "<br>");
        String formmatedTrace = "<details>\n" +
                "    <summary>Click Here To See Exception Logs</summary>\n" +
                "    " + stackTrace + "\n" +
                "</details>\n";
        ExtentReportManager.logExceptionDetails(formmatedTrace);
    }

    public void onFinish(ITestContext context) {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
