package com.wikia.webdriver.common.core.exceptions;

import org.testng.SkipException;

public class MethodIsExcludedFromRunningOnEnv extends SkipException{
  public MethodIsExcludedFromRunningOnEnv(String skipMessage){
    super(skipMessage);
  }
}
