package frameworkutils;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Browser factory to handle browser related common operations for project startup and termination
 *
 * @author anuj gupta
 */
public class BrowserFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static PropertyManager prop = PropertyManager.getInstance();
    private static BrowserFactory browser;
    private LogManager log = LogManager.getInstance();
    private String propertyFileName = "config";
    private String osName = System.getProperty("os.name");
    private String browserName = prop.getProperty(propertyFileName, "browser");

    private BrowserFactory() {
    }

    public static BrowserFactory getInstance() {

        if (browser == null)
            browser = new BrowserFactory();
        return browser;
    }

    /**
     * Checks browser name in property file and launches browser accordingly
     */
    void launchBrowser() {

        switch (browserName) {
            case "chrome":
                launchChrome();
                break;
            case "firefox":
                launchFirefox();
                break;
            default:
                log.error("Invalid browser");
        }
        int wait = Integer.parseInt(prop.getProperty(propertyFileName, "implicitWait"));
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(wait, TimeUnit.MILLISECONDS);
    }

    /**
     * Launches chrome driver
     */
    private void launchChrome() {

        if (osName.equals("Linux"))
            System.setProperty("webdriver.chrome.driver", Constants.DRIVERS_PATH + "chromedriver");
        else
            System.setProperty("webdriver.chrome.driver", Constants.DRIVERS_PATH + "chromedriver.exe");

        // Set chrome driver options for testing
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");

        // launch browser before each test execution for parallel execution
        if (prop.getProperty(propertyFileName, "grid").equals("false")) {
            driver.set(new ChromeDriver(options));
        } else {
            try {
                // Initialize reference variable for desiredCapabilities class
                DesiredCapabilities cap = DesiredCapabilities.chrome();

                // Disable information bar when chrome is launched
                options.addArguments("disable-infobars");

                // Apply property set via option reference variable to chrome
                cap.setCapability(ChromeOptions.CAPABILITY, options);

                // Set browser to chrome
                cap.setBrowserName(browserName);
                cap.setPlatform(Platform.ANY);

                driver.set(new RemoteWebDriver(new URL("http://10.121.21.89:4444/wd/hub"), cap));
            } catch (MalformedURLException e) {
                log.info(e.getMessage());
            }
        }
    }

    /**
     * Launches firefox driver
     */
    private void launchFirefox() {

        if (osName.equals("Linux")) {
            System.setProperty("webdriver.gecko.driver", Constants.DRIVERS_PATH + "geckodriver");
        } else {
            System.setProperty("webdriver.gecko.driver", Constants.DRIVERS_PATH + "geckodriver.exe");
        }
        // launch browser before each test execution for parallel execution
        if (prop.getProperty(propertyFileName, "grid").equals("false")) {
            driver.set(new FirefoxDriver());
        } else {
            try {
                // Initialize reference variable for DesiredCapabilities class
                DesiredCapabilities cap = DesiredCapabilities.firefox();

                // Initialize reference variable FirefoxProfile class
                FirefoxProfile fp = new FirefoxProfile();

                // Disable notifications in firefox
                fp.setPreference("dom.webnotifications.enabled", false);

                // Apply preferences to firefox
                cap.setCapability(ChromeOptions.CAPABILITY, fp);

                // Set browser to firefox
                cap.setBrowserName(browserName);

                // Set platform to any
                cap.setPlatform(Platform.ANY);

                driver.set(new RemoteWebDriver(new URL(prop.getProperty(propertyFileName, "hubURL")), cap));
            } catch (MalformedURLException e) {
                log.info(e.getMessage());
            }
        }
    }

    /**
     * Returns instance of webDriver
     *
     * @return Instance of WebDriver
     */
    public WebDriver getDriver() {

        return driver.get();
    }

    /**
     * Closes the browser window
     */
    void quit() {

        getDriver().quit();
    }
}
