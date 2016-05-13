package com.wikia.webdriver.common.logging;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import freemarker.template.TemplateException;
import lombok.Getter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.openqa.selenium.WebDriver;

import com.wikia.webdriver.common.core.CommonUtils;
import com.wikia.webdriver.common.core.annotations.RelatedIssue;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.core.imageutilities.Shooter;
import com.wikia.webdriver.common.core.url.UrlBuilder;
import com.wikia.webdriver.common.driverprovider.DriverProvider;

public class PageObjectLogging {

  private static int imageCounter = 0;
  private static String reportPath = "." + File.separator + "logs" + File.separator;
  private static String screenDirPath = reportPath + "screenshots" + File.separator;
  private static String screenPath = screenDirPath + "screenshot";
  private static String logFileName = "log.html";
  private static String logPath = reportPath + logFileName;
  private static String jiraPath = "https://wikia-inc.atlassian.net/browse/";
  private static ArrayList<Boolean> logsResults = new ArrayList<>();
  private static ArrayList<Step> logs = new ArrayList<>();
  private static TemplateProcessing templateProcessing = new TemplateProcessing().setCfg();
  @Getter
  private static boolean testStarted = false;

  private static String getPageSource(WebDriver driver) {
    return driver.getPageSource().replaceAll("<script", "<textarea style=\"display: none\"><script")
        .replaceAll("</script", "</script></textarea");
  }

  private static void log(LogType type, String command, String description,
      boolean makeScreenshot) {
    logsResults.add(type.getSuccess());
    String escapedDescription = "";
    String urlFormat = "<a href='%s'>%s</a>";
    try {
      new URL(description).toURI();
      escapedDescription = String.format(urlFormat, description, description);
    } catch (URISyntaxException | MalformedURLException e) {
      escapedDescription = escapeHtml(description);
    }

    CommonUtils.appendTextToFile(logPath,
        logRow(type, command, escapedDescription, makeScreenshot));
  }

  public static void log(String command, String description, boolean success, WebDriver driver) {
    LogType logResult = success ? LogType.SUCCESS : LogType.ERROR;
    log(logResult, command, description, true);
  }

  public static void log(String command, Throwable e, boolean success, WebDriver driver) {
    log(command, e.getMessage(), success, driver);
  }

  public static void log(String command, String description, boolean success) {
    LogType logResult = success ? LogType.SUCCESS : LogType.ERROR;
    log(logResult, command, description, false);
  }

  public static void log(String command, Throwable e, boolean success) {
    log(command, e.getMessage(), success);
  }

  public static void log(String command, String descriptionOnSuccess, String descriptionOnFail,
      boolean success) {
    String description = descriptionOnFail;
    if (success) {
      description = descriptionOnSuccess;
    }
    log(command, description, success);
  }

  /**
   * This method will log error to log file
   */
  public static void logError(String command, String description) {
    log(LogType.ERROR, command, description, true);
  }

  public static void logError(String command, Exception exception) {
    logError(command, exception.getMessage());
  }

  /**
   * This method will log warning to log file (line in yellow color)
   */
  public static void logWarning(String command, String description) {
    log(LogType.WARNING, command, description, false);
  }

  public static void logWarning(String command, Exception exception) {
    logWarning(command, exception.getMessage());
  }

  /**
   * This method will log info to log file (line in blue color)
   */
  public static void logInfo(String command, String description) {
    log(LogType.INFO, command, description, false);
  }

  public static void logInfo(String description) {
    log(LogType.INFO, "INFO", description, false);
  }

  public static void logInfo(Exception exception) {
    logInfo(exception.getMessage());
  }

  public static void logSuccess(String command, String description) {
    log(LogType.SUCCESS, command, description, false);
  }

  public static void logImage(String command, File image, boolean success) {
    byte[] bytes = new byte[0];
    try {
      bytes = new Base64().encode(FileUtils.readFileToByteArray(image));
    } catch (IOException e) {
      log("logImage", e.getMessage(), false);
    }
    logImage(command, new String(bytes, StandardCharsets.UTF_8), success);
  }

  public static void logImage(String command, String imageAsBase64, boolean success) {
    imageAsBase64 = "<img src=\"data:image/png;base64," + imageAsBase64 + "\">";
    String className = success ? "success" : "error";
    CommonUtils.appendTextToFile(logPath, ("<tr class=\"" + className + "\"><td>" + command
        + "</td><td>" + imageAsBase64 + "</td><td> <br/> &nbsp;</td></tr>"));
  }

  public static List<Boolean> getVerificationStack() {
    return logsResults;
  }

  public static void start(Method testMethod) {
    StringBuilder builder = new StringBuilder();
    String testName = testMethod.getName();
    String className = testMethod.getDeclaringClass().getCanonicalName();

    builder.append("<table>" + "<h1>Class: <em>" + className + "." + testName + " </em></h1>");
    if (testMethod.isAnnotationPresent(RelatedIssue.class)) {
      String issueID = testMethod.getAnnotation(RelatedIssue.class).issueID();
      String jiraUrl = jiraPath + issueID;
      builder.append("<tr class=\"step\"><td>Known failure</td><td><h1><em>" + testName + " - "
          + "<a href=\"" + jiraUrl + "\">" + issueID + "</a> "
          + testMethod.getAnnotation(RelatedIssue.class).comment()
          + "</em></h1></td><td> <br/> &nbsp;</td></tr>");
    } else {
      builder.append("<tr class=\"step\"><td>&nbsp</td><td><h1><em>" + testName
          + "</em></h1></td><td> <br/> &nbsp;</td></tr>");
    }
    CommonUtils.appendTextToFile(logPath, builder.toString());
    testStarted = true;
  }

  public static void stopLogging() {
    StringBuilder builder = new StringBuilder();
    builder.append("<tr class=\"step\">" + "<td>&nbsp</td><td>STOP LOGGING METHOD  "
        + "<div style=\"text-align:center\">" + "<a href=\"#toc\" style=\"color:blue\">"
        + "<b>BACK TO MENU</b></a></div> </td><td> <br/> &nbsp;</td></tr>" + "</table>");
    CommonUtils.appendTextToFile(logPath, builder.toString());
    testStarted = false;
    logsResults.clear();
  }

  public static void startReport() {
    CommonUtils.createDirectory(screenDirPath);
    imageCounter = 0;

    StringBuilder builder = new StringBuilder();
    builder.append("<html><style>"
        + "table {margin:0 auto;}td:first-child {width:200px;}td:nth-child(2) {width:660px;}td:nth-child(3) "
        + "{width:100px;}tr.success{color:black;background-color:#CCFFCC;}"
        + "tr.warning{color:black;background-color:#FEE01E;}"
        + "tr.error{color:black;background-color:#FFCCCC;}"
        + "tr.info{color:white;background-color:#78a1c0}" + "tr.step{color:white;background:grey}"
        + "</style><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">"
        + "<style>td { border-top: 1px solid grey; } </style></head><body>"
        + "<script type=\"text/javascript\" src=\"http://code.jquery.com/jquery-1.8.2.min.js\"></script>"
        + "<p>Date: "
        + DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ").print(DateTime.now(DateTimeZone.UTC))
        + "</p>" + "<p>Polish Time: "
        + DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss ZZ")
            .print(DateTime.now()
                .withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Warsaw"))))
        + "</p>" + "<p>Browser: " + Configuration.getBrowser() + "</p>" + "<p>OS: "
        + System.getProperty("os.name") + "</p>" + "<p>Testing environment: "
        + new UrlBuilder().getUrlForWiki(Configuration.getWikiName()) + "</p>"
        + "<p>Testing environment: " + Configuration.getEnv() + "</p>" + "<p>Tested version: "
        + "TO DO: GET WIKI VERSION HERE" + "</p>" + "<div id='toc'></div>");
    CommonUtils.appendTextToFile(logPath, builder.toString());
    try {
      FileInputStream input = new FileInputStream("./src/test/resources/script.txt");
      String content = IOUtils.toString(input);
      CommonUtils.appendTextToFile(logPath, content);
    } catch (IOException e) {
      System.out.println("no script.txt file available");
    }
  }

  public static void FinishReport() {
    CommonUtils.appendTextToFile(logPath, "</body></html>");
  }

  private static String logRow(LogType logType, String command, String description,
      boolean screenshot) {
    String additionalInfo = "";
    if (screenshot) {
      additionalInfo = additionalInfo();
    }
    String rowFormat = "<tr class=\"%s\"><td>%s</td><td>%s</td><td>%s</td></tr>";

    TestReport.addstep(new Step(command, description, logType));

    logs.add(new Step(command, description, logType));
    try {
      new TemplateProcessing().processReport();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TemplateException e) {
      e.printStackTrace();
    }

    return String.format(rowFormat, logType.getLogClass(), command, description, additionalInfo);
  }

  private static String additionalInfo() {
    imageCounter += 1;
    new Shooter().savePageScreenshot(screenPath + imageCounter, DriverProvider.getActiveDriver());
    CommonUtils.appendTextToFile(screenPath + imageCounter + ".html",
        getPageSource(DriverProvider.getActiveDriver()));
    String format =
        "<br/><a href='screenshots/screenshot%d.png'>Screenshot</a><br/><a href='screenshots/screenshot%d.html'>HTML Source</a>";

    return String.format(format, imageCounter, imageCounter);
  }

  public enum LogType {
    INFO("info", true), ERROR("error", false), WARNING("warning", true), SUCCESS("success", true);

    private String logClass;
    private boolean success;

    LogType(String logClass, boolean success) {
      this.logClass = logClass;
      this.success = success;
    }

    public String getLogClass() {
      return logClass;
    }

    public boolean getSuccess() {
      return success;
    }
  }
}
