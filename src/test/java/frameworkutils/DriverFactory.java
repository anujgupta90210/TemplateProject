package frameworkutils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    private static ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

    public static synchronized void newDriver() {
        System.setProperty("webdriver.chrome.driver", Constants.DRIVERS_PATH + "chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        drivers.set(new ChromeDriver(options));
    }

    public static synchronized WebDriver getDriver() {
        return drivers.get();
    }
}
