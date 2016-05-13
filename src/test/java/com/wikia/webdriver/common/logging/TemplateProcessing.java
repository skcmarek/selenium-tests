package com.wikia.webdriver.common.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class TemplateProcessing {

  private static Configuration cfg = new Configuration();
  Map<String, Object> input = new HashMap<String, Object>();

  public TemplateProcessing setCfg() {
    cfg.setIncompatibleImprovements(new Version(2, 3, 20));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setLocale(Locale.US);
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    try {
      cfg.setDirectoryForTemplateLoading(
          new File(ClassLoader.getSystemResource("report").getPath()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return this;
  }

  public void processReport() throws IOException, TemplateException {
    input.put("tests", TestReport.getTests());
    Template template = null;
    try {
      template = cfg.getTemplate("report.ftl");
    } catch (IOException e) {
      e.printStackTrace();
    }

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
