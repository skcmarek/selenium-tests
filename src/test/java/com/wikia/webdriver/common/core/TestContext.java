package com.wikia.webdriver.common.core;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

public class TestContext {
  private static String methodName;
  private static Method testMethod;
  private static boolean isFirstLoad = false;
  @Setter
  @Getter
  private static boolean isFirstRequest = false;

  public static void writeMethodName(Method method) {
    methodName =
        method.getDeclaringClass().getSimpleName() + StringUtils.capitalize(method.getName());

    testMethod = method;
    isFirstLoad = true;
    isFirstRequest = true;
  }

  public static String getCurrentMethodName() {
    return methodName;
  }

  public static Method getCurrentTestMethod(){
    return testMethod;
  }

  public static void setFirstLoad(boolean value){
    isFirstLoad = value;
  }

  public static boolean isFirstLoad(){
    return isFirstLoad;
  }
}
