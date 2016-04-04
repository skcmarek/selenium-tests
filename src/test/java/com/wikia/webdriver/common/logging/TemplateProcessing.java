package com.wikia.webdriver.common.logging;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TemplateProcessing {

  private static Configuration cfg = new Configuration();
  Map<String, Object> input = new HashMap<String, Object>();

  public TemplateProcessing(){

  }

  public TemplateProcessing setCfg() {
    cfg.setIncompatibleImprovements(new Version(2, 3, 20));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setLocale(Locale.US);
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    try {
      cfg.setDirectoryForTemplateLoading(new File(ClassLoader.getSystemResource("report").getPath()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return this;
  }

  public void processReport(ArrayList<Step> logs) throws IOException, TemplateException {

    List<Test> tests = TestReport.getTests();

    input.put("logs", logs);

    // 2.2. Get the template

    Template template = null;
    try {
      template = cfg.getTemplate("report.ftl");
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 2.3. Generate the output

    // Write output to the console
    Writer consoleWriter = new OutputStreamWriter(System.out);
    template.process(input, consoleWriter);

    // For the sake of example, also write output into a file:
    Writer fileWriter = new FileWriter(new File("logs/output.html"));
    try {
      template.process(input, fileWriter);
    } catch (TemplateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      fileWriter.close();
    }
  }
}
