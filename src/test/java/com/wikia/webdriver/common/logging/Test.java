package com.wikia.webdriver.common.logging;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Test {

  @Getter
  private List<Step> steps = new ArrayList<>();

  @Getter
  @Setter
  private String name;

  public void addStep(Step step){
    steps.add(step);
  }
}
