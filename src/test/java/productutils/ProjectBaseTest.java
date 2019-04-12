package productutils;

import frameworkutils.ExcelManager;
import frameworkutils.FrameworkBaseTest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Map;

public class ProjectBaseTest extends FrameworkBaseTest {

    private LoginPage login = LoginPage.getInstance();
    private ExcelManager excel = ExcelManager.getInstance();
    Map<String, String> aliases = excel.getColumnAsMap("aliases", 0, 1);

    @BeforeSuite
    public void sequentialLogin(ITestContext testContext) {

        if (getParallel(testContext).equals("none")) {
            login.loginOM(aliases.get("loginpage.username"), aliases.get("loginpage.password"));
        }
    }

    @BeforeMethod
    public void parallelLogin(ITestContext testContext) {

        if (!getParallel(testContext).equals("none")) {
            login.loginOM(aliases.get("loginpage.username"), aliases.get("loginpage.password"));
        }
    }

    private String getParallel(ITestContext testContext) {
        return testContext.getSuite().getXmlSuite().getParallel().toString();
    }
}
