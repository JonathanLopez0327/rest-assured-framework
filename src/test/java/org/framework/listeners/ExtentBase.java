package org.framework.listeners;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class ExtentBase {
    @BeforeMethod
    public void beforeConfig(Method method) {
        ExtentManager.createInstance(method.getName());
    }

//    @AfterMethod
//    public void afterConfig() {
//        ExtentManager.extentReports.flush();
//    }
}
