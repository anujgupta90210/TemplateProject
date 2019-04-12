package productutils;

import frameworkutils.LogManager;
import frameworkutils.PropertyManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private static LoginPage login;
    private LogManager log = LogManager.getInstance();
    private PropertyManager prop = PropertyManager.getInstance();
    private BaseTest baseTest = BaseTest.getInstance();

    public static LoginPage getInstance() {

        if (login == null)
            login = new LoginPage();
        return login;
    }

    @Step
    public void loginOM(String userName, String password) {
        try {
            baseTest.navigate(prop.getProperty("config", "baseURL"));
            baseTest.pause(2000);
            WebElement usrname = baseTest.findElement(By.cssSelector("#txtusername"));
            usrname.sendKeys(userName);
            WebElement psword = baseTest.findElement(By.cssSelector("#txtpassword"));
            psword.sendKeys(password);
            baseTest.waitForElement(By.cssSelector("#btnlogin-real"), BaseTest.WaitType.VISIBILITY_OF_ELEMENT_LOCATED);
            baseTest.clickOnElement(By.cssSelector("#btnlogin-real"));
            baseTest.pause(2000);
            if (baseTest.isElementPresent(By.cssSelector("#btn16"))) {
                baseTest.clickOnElement(By.cssSelector("#btn16"));
                baseTest.pause(2000);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
