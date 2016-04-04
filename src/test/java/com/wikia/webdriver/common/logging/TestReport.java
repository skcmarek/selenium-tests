package com.wikia.webdriver.common.logging;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.joda.time.DateTime;

public class TestReport {

  @Getter
  static private List<Test> tests = new ArrayList<>();
  @Getter
  @Setter
  private DateTime date;

  private static Test getLastTest() {
    return tests.get(tests.size() - 1);
  }

  public static void addstep(Step step) {
    getLastTest().addStep(step);
  }

  public static void startTest() {
    tests.add(new Test());
  }

  public static void setTestName(String name) {
    getLastTest().setName(name);
  }
}
