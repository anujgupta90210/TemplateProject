package frameworkutils;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

/**
 * Reporter class to generate extent report and capture logs
 *
 * @author Anuj Gupta
 */
public class ReportManager {

    private static final ThreadLocal<ExtentTest> test = ThreadLocal.withInitial(() -> new ExtentTest("", ""));
    private static ReportManager report;
    private BrowserFactory browser = BrowserFactory.getInstance();
    private LogManager log = LogManager.getInstance();
    private PropertyManager prop = PropertyManager.getInstance();
    private ExtentReports extent;
    private String reportPath = Constants.REPORTER_PATH;
    private String reportName;

    private ReportManager() {
    }

    /**
     * Moves old reports to archive folder. Initialize extent report
     */
    public static ReportManager getInstance() {

        if (report == null) {
            report = new ReportManager();
        }
        return report;
    }

    /**
     * Returns instance of webDriver
     *
     * @return Instance of WebDriver
     */
    private static ExtentTest getTest() {

        return test.get();
    }

    /**
     * Starts reporting and display current test under execution in console
     *
     * @param name Name of the test under execution
     */
    public void startTest(String name) {

        extent = init();
        test.set(extent.startTest(name, ""));

        // Display current test under execution in console
        log.info("#######  " + name + "  #######");
    }

    /**
     * Ends reporting and appends data into extent report
     */
    public void endTest() {

        extent.endTest(getTest());

        // Writes everything to document
        extent.flush();
    }

    /**
     * Initialize extent report reference variable
     *
     * @return Object of Extent Report
     */
    private ExtentReports init() {

        try {
            // Creates extent report if not done already
            if (extent == null) {
                // Stores current date
                Date date = new Date();

                // Append timeStamp in screenshot's name
                reportName = date.toString().replace(":", "_").replace(" ", "_");

                // Appends report name to report path
                reportPath = reportPath + reportName + "/";

                extent = new ExtentReports(reportPath + reportName + ".html", true, DisplayOrder.OLDEST_FIRST);
                extent.addSystemInfo("URL", prop.getProperty("config", "baseURL"));
                test.set(new ExtentTest("", ""));
            }
            return extent;

        } catch (Exception e) {
            log.error("Error in ExtentReports/init");
            return null;
        }
    }

    /**
     * Capture logs for passed scenarios
     *
     * @param description Logs for passed test
     */
    public void pass(String description) {

        try {
            getTest().log(LogStatus.PASS, description);
            log.info(description);

        } catch (Exception e) {
            log.error("Error in ExtentReporter.pass");
        }
    }

    /**
     * Prints error message, type of error & priority of error along with the screenshot in the report based on type of exception
     *
     * @param description       Error message to be shown in reports
     * @param captureScreenshot boolean value to capture screenshot or not
     */
    public void fail(String description, boolean captureScreenshot) {

        try {
            getTest().log(LogStatus.FAIL, description);
            if (captureScreenshot)
                captureScreenshot();
            log.error(description);

        } catch (Exception e) {
            log.error("Error in ExtentReporter.fail with screenshot");
        }
    }

    /**
     * Prints error message, type of error & priority of error along with the screenshot in the report based on type of exception
     *
     * @param description Error message to be shown in reports
     */
    public void fail(String description) {

        try {
            getTest().log(LogStatus.FAIL, description);
            log.error(description);

        } catch (Exception e) {
            log.error("Error in ExtentReporter.fail");
        }
    }

    /**
     * Capture logs for skipped scenarios
     *
     * @param description Log message
     */
    public void skip(String description) {

        try {
            getTest().log(LogStatus.SKIP, description);
            log.warn(description);

        } catch (Exception e) {
            log.error("Error in ExtentReporter.skip");
        }
    }

    /**
     * Captures screenshot and stores where reports are being generated
     */
    private void captureScreenshot() {

        try {
            // Stores current date
            Date date = new Date();

            // Appends timeStamp in screenshot's name
            String screenshotName = date.toString().replace(":", "_").replace(" ", "_") + ".jpg";

            // Captures screenshot and stores in a file.
            File file = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.FILE);

            // Stores the captured screenshot in given location
            FileUtils.copyFile(file, new File(Constants.REPORTER_PATH + screenshotName));

            // Append screenshot to extent report
            getTest().log(LogStatus.INFO, getTest().addScreenCapture(screenshotName));
            log.info("Screenshot captured");

        } catch (Exception e) {
            log.error("Error while capturing screenshot");
        }
    }

    /**
     * Move old reports to a separate folder
     */
    public void moveToArchive() {

        try {
            // get reports folder
            File reportsFolder = new File(Constants.REPORTER_PATH);

            // If there is any result folder available apart from archive before new execution then move the existing logs to archive folder
            if (reportsFolder.exists() && reportsFolder.listFiles().length >= 1) {

                // Generate the archive data
                File archiveFolder = new File(Constants.ARCHIVE_REPORT_PATH);

                // Loop if more then 1 report file
                for (File f : reportsFolder.listFiles()) {
                    try {
                        FileUtils.moveToDirectory(f, archiveFolder, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Unable to move the existing reports");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in Reporter.MoveToArchieve");
        }
    }
}
