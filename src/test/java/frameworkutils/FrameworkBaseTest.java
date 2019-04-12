package frameworkutils;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class FrameworkBaseTest {
    private ReportManager report = ReportManager.getInstance();
    private LogManager log = LogManager.getInstance();
    private ExcelManager excel = ExcelManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();

    @BeforeSuite
    public void setup(ITestContext testContext) {

        log.setup();
        excel.openInputStream();
        report.moveToArchive();

        // launch browser before test execution for sequential execution
        if (getParallel(testContext).equals("none"))
            browser.launchBrowser();
    }

    @BeforeMethod
    public void startReporter(ITestContext testContext, ITestResult result) {

        report.startTest(result.getMethod().getMethodName());

        // launch browser before each test execution for parallel execution
        if (!getParallel(testContext).equals("none"))
            browser.launchBrowser();
    }

    @AfterMethod
    public void endReporter(ITestContext testContext) {

        report.endTest();

        // close browser after each test execution for parallel execution
        if (!getParallel(testContext).equals("none"))
            browser.quit();
    }

    @AfterSuite
    public void teardown(ITestContext testContext) {

        excel.closeInputStream();

        // close browser after complete test execution for sequential execution
        if (getParallel(testContext).equals("none")) {
            browser.quit();
        }
    }

    private String getParallel(ITestContext testContext) {
        return testContext.getSuite().getXmlSuite().getParallel().toString();
    }
}
