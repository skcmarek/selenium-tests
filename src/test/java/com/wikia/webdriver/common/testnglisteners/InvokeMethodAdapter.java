package com.wikia.webdriver.common.testnglisteners;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.annotations.DontRun;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.core.exceptions.MethodIsExcludedFromRunningOnEnv;
import com.wikia.webdriver.common.core.exceptions.TestFailedException;
import com.wikia.webdriver.common.logging.PageObjectLogging;

public class InvokeMethodAdapter implements IInvokedMethodListener {

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult result) {
    if (method.isTestMethod()) {
      List verificationFailures = Assertion.getVerificationFailures(result);
      // if there are verification failures...
      if (PageObjectLogging.getVerificationStack().contains(false)) {
        result.setStatus(ITestResult.FAILURE);
        result.setThrowable(new TestFailedException());
      }
      if (verificationFailures.size() > 0) {
        // set the test to failed
        result.setStatus(ITestResult.FAILURE);
        for (Object failure : verificationFailures) {
          result.setThrowable((Throwable) failure);
        }
      }
    }
  }

  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    if (isTestExcludedFromEnv(method.getTestMethod().getConstructorOrMethod().getMethod())) {
      throw new MethodIsExcludedFromRunningOnEnv("this test is not supported in this environment");
    }
  }

  private boolean isTestExcludedFromEnv(Method method) {
    if (method.isAnnotationPresent(DontRun.class)) {
      if (Arrays.asList(method.getAnnotation(DontRun.class).env())
          .contains(Configuration.getEnvType())) {
        return true;
      }
    }
    return false;
  }
}
