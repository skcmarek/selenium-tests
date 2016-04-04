package com.wikia.webdriver.common.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Step {
  private String command;
  private String description;
  private PageObjectLogging.LogType logType;

  public String getResult(){
    return logType.getLogClass();
  }
}
