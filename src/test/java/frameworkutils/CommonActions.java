package frameworkutils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Class contains common actions
 *
 * @author anuj gupta
 */
public class CommonActions {

    private static CommonActions commonActions;
    private LogManager log = LogManager.getInstance();
    private PropertyManager prop = PropertyManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();
    private ReportManager report = ReportManager.getInstance();
    private int explicitWait = Integer.parseInt(prop.getProperty("config", "explicitWait"));

    public static CommonActions getInstance() {

        if (commonActions == null)
            commonActions = new CommonActions();
        return commonActions;
    }

    /**
     * Checks if alert is present or not
     *
     * @return true if alert is present otherwise false
     */
    public boolean isAlertPresent() {

        try {
            // Returns true if alert is found
            browser.getDriver().switchTo().alert();
            return true;

        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    /**
     * Accepts an alert
     */
    public void acceptAlert() {

        try {
            if (isAlertPresent()) {
                browser.getDriver().switchTo().alert().accept();
            }

        } catch (Exception e) {
            report.fail("Unable to handle alert");
        }
    }

    /**
     * Accepts an alert
     */
    public void dismissAlert() {

        try {
            if (isAlertPresent()) {
                browser.getDriver().switchTo().alert().dismiss();
            }

        } catch (Exception e) {
            report.fail("Unable to handle alert");
        }
    }

    /**
     * Selects a value from dropdown using the value attribute passed as argument
     *
     * @param locator Locator of the select tag
     * @param value   Value attribute of the dropdown option to select
     */
    public void selectByValue(By locator, String value) {

        Select select;
        WebElement element;

        try {
            element = browser.getDriver().findElement(locator);
            element.click();

            select = new Select(element);
            select.selectByValue(value);

        } catch (Exception e) {
            report.fail("Unable to select by value");
        }
    }

    /**
     * Selects a value from dropdown using the value attribute passed as argument
     *
     * @param locator Locator of the select tag
     * @param text    Text of the dropdown option to select
     */
    public void selectByVisibleText(By locator, String text) {

        Select select;
        WebElement element;

        try {
            element = browser.getDriver().findElement(locator);
            element.click();

            select = new Select(element);
            select.selectByVisibleText(text);

        } catch (Exception e) {
            report.fail("Unable to select by visible text");
        }
    }

    /**
     * Selects a value from dropdown using the value attribute passed as argument
     *
     * @param locator Locator of the select tag
     * @param index   Index of the dropdown option to select.(Index starts from zero)
     */
    public void selectByIndex(By locator, int index) {

        Select select;
        WebElement element;

        try {
            element = browser.getDriver().findElement(locator);
            element.click();

            select = new Select(element);
            select.selectByIndex(index);

        } catch (Exception e) {
            report.fail("Unable to select by index");
        }
    }

    /**
     * Deselects all the values from the dropdown
     */
    public void deselectAll(By locator) {

        Select select;
        WebElement element;

        try {
            element = browser.getDriver().findElement(locator);
            element.click();

            select = new Select(element);
            select.deselectAll();

        } catch (Exception e) {
            report.fail("Unable to deselect all");
        }
    }

    /**
     * Checks if an element is displayed or not
     *
     * @param locator Element to be checked
     * @return Boolean
     */
    public boolean isDisplayed(By locator) {

        WebElement webElement;

        try {
            webElement = browser.getDriver().findElement(locator);

            // Returns true if element is displayed otherwise returns false
            return webElement.isDisplayed();

        } catch (Exception e) {

            report.fail("Unable to check if element " + locator + " is displayed");
        }
        return false;
    }

    /**
     * Checks if an element is enabled or not
     *
     * @param locator Locator of the element to be checked
     * @return Boolean
     */
    public boolean isEnabled(By locator) {

        WebElement webElement;

        try {
            webElement = browser.getDriver().findElement(locator);

            // Returns true if element is enabled otherwise returns false
            return webElement.isEnabled();
        } catch (Exception e) {
            report.fail("Invalid locator " + locator);
        }
        return false;
    }

    /**
     * Checks if an element is selected or not
     *
     * @param locator Element to be checked
     * @return Boolean
     */
    public boolean isSelected(By locator) {

        WebElement webElement;

        try {
            webElement = browser.getDriver().findElement(locator);

            // Returns true if the element is currently selected or checked otherwise returns false
            return webElement.isSelected();

        } catch (Exception e) {
            report.fail("Unable to check if " + locator + " is selected");
        }
        return false;
    }

    /**
     * Locate elements within DOM
     *
     * @param locator Locator to find element
     * @return WebElement
     */
    public WebElement findElement(By locator) {

        WebElement webElement;

        try {
            webElement = browser.getDriver().findElement(locator);

            // returns element found on the page
            return webElement;

        } catch (Exception e) {
            report.fail("Invalid locator " + locator);
            return null;
        }
    }

    /**
     * Locate list of web elements with locator provided
     *
     * @param locator Locator of the list of elements
     * @return List of Web Elements
     */
    public List<WebElement> findElements(By locator) {

        try {
            // Returns list of elements with locator received as argument
            return browser.getDriver().findElements(locator);

        } catch (Exception e) {

            report.fail("Unable to find list of elements with locator " + locator);
            return Collections.emptyList();
        }
    }

    /**
     * Checks if an element is present on the page or not
     *
     * @param locator Locator whose presence is being checked
     * @return boolean
     */
    public boolean isElementPresent(By locator) {

        try {
            if (browser.getDriver().findElement(locator).isDisplayed()) {
                browser.getDriver().findElement(locator);
                return true;
            } else
                return false;

        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns inner text of the element
     *
     * @param locator Element whose inner text needs to be found.
     * @return String
     */
    public String getText(By locator) {

        WebElement webElement;

        try {
            // Finds the element on the page
            webElement = browser.getDriver().findElement(locator);

            // Stores inner text of the element
            String text = webElement.getAttribute("innerText");
            text = text.trim();
            return text;

        } catch (Exception e) {
            report.fail("Unable to get text of element " + locator);
        }
        return null;
    }

    /**
     * Finds and returns background-color
     *
     * @param locator Whose background-color needs to be found
     * @return Hexadecimal of background color
     */
    public String getBackgroundColor(By locator) {

        WebElement webElement;

        try {
            // Finds the element on the page
            webElement = browser.getDriver().findElement(locator);

            // Stores the required attribute of the element.
            String css = webElement.getCssValue("background-color");

            // Stores hexadecimal code of the color
            return Color.fromString(css).asHex();

        } catch (Exception e) {

            report.fail("Unable to find background-color of element " + locator);
        }
        return null;
    }

    /**
     * Returns attribute of an element
     *
     * @param locator   element whose attribute needs to be found
     * @param attribute attribute to be found
     * @return value of the requied attribute
     */
    public String getAttribute(By locator, String attribute) {

        try {
            // Stores the required attribute of the element.
            String attributeValue = browser.getDriver().findElement(locator).getAttribute(attribute);

            // Returns empty string if attribute is not found
            if (attributeValue == null)
                return "";
            else
                return attributeValue;
        } catch (Exception e) {
            report.fail("Unable to find attribute " + attribute + " of element " + locator);
        }
        return null;
    }

    /**
     * Exits current test
     */
    public void exitTest() {

        try {
            Assert.fail();

        } catch (Exception e) {
            report.fail("Exited the test");
        }
    }

    /**
     * Uploads a file using Robot class
     *
     * @param fileName path of the file to be uploaded
     */
    public void uploadFile(String fileName) {

        fileName = Constants.UPLOAD_FILE_PATH + fileName;

        // Stores file path into a transferable string and copies that path to clip board
        StringSelection filePath = new StringSelection(fileName);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filePath, null);

        try {
            Robot robot = new Robot();

            // Press enter key
            robot.delay(1000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            // Press control+V
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            // Release control+V
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            // Press enter key
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (Exception e) {
            report.fail("Unable to upload the file " + fileName);
        }
    }

    /**
     * Enters text in an input element
     *
     * @param locator Locator of the Input element
     * @param text    Text to be entered
     */
    public void sendKeys(By locator, String text) {

        WebElement element;

        try {
            // Finds the element on the page
            element = browser.getDriver().findElement(locator);

            // Clears the text field
            element.clear();

            // Enters text
            element.sendKeys(text);

        } catch (Exception e) {
            report.fail("Unable to enter text");
        }
    }

    /**
     * Clears the input field
     *
     * @param locator Locator of the input field
     */
    public void clear(By locator) {

        WebElement element;

        try {
            element = browser.getDriver().findElement(locator);
            element.clear();

        } catch (Exception e) {
            report.fail("Failed to clear text in " + locator);
        }
    }

    /**
     * Clears existing data from the element
     *
     * @param locator locator of the element
     */
    public void clearAll(By locator) {

        try {
            WebElement element;
            element = browser.getDriver().findElement(locator);
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            element.sendKeys(Keys.BACK_SPACE);

        } catch (Exception e) {
            report.fail("Failed to clear text in " + locator);
        }
    }

    /**
     * Press keys like Enter, Tab, etc.
     *
     * @param keys Keys to be pressed
     */
    public void sendKeys(Keys... keys) {

        try {
            Actions actions = new Actions(browser.getDriver());
            Action action = actions.sendKeys(keys).build();
            action.perform();

        } catch (Exception e) {
            report.fail("Error while sending input from keyboard");
        }
    }

    /**
     * Clicks on the element
     *
     * @param locator Locator of the webElement to be clicked
     */
    public void click(By locator) {

        WebElement element;

        try {
            WebDriverWait wait = new WebDriverWait(browser.getDriver(), explicitWait);
            wait.until(ExpectedConditions.elementToBeClickable(locator));

            element = browser.getDriver().findElement(locator);
            element.click();

        } catch (Exception e) {
            report.fail("Unable to click on the element " + locator);
        }
    }

    /**
     * Double clicks on a certain element
     *
     * @param locator Locator of the element
     */
    public void doubleClick(By locator) {

        WebElement element;

        try {
            WebDriverWait wait = new WebDriverWait(browser.getDriver(), explicitWait);
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));

            element = browser.getDriver().findElement(locator);

            Actions action = new Actions(browser.getDriver());
            action.doubleClick(element).build().perform();

        } catch (Exception e) {
            report.fail("Unable to double click on the element " + locator);
        }
    }

    /**
     * Right clicks on the given element
     *
     * @param locator Element to be clicked
     */
    public void rightClick(By locator) {

        WebElement element;

        try {
            element = browser.getDriver().findElement(locator);
            Actions action = new Actions(browser.getDriver());
            action.contextClick(element).build().perform();

        } catch (Exception e) {
            report.fail("Unable to right click on the element " + locator);
        }
    }

    /**
     * Clicks on an element using javascript
     *
     * @param locator locator of the element to be clicked
     */
    public void clickByJavaScript(By locator) {

        WebElement element;

        try {
            JavascriptExecutor jse = (JavascriptExecutor) browser.getDriver();
            element = browser.getDriver().findElement(locator);
            jse.executeScript("arguments[0].click();", element);

        } catch (Exception e) {
            report.fail("Unable to scroll the page");
        }
    }

    /**
     * Hovers mouse over a certain element
     *
     * @param locator Locator of the element
     */
    public void hover(By locator) {

        WebElement element;

        try {
            WebDriverWait wait = new WebDriverWait(browser.getDriver(), explicitWait);
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));

            element = browser.getDriver().findElement(locator);

            Actions action = new Actions(browser.getDriver());
            action.moveToElement(element).build().perform();

        } catch (Exception e) {
            report.fail("Unable to hover over the element " + locator);
        }
    }

    /**
     * A method that performs click-and-hold at the location of the source element, moves by a given offset, then releases the mouse
     *
     * @param locator Locator of the source element
     * @param x       horizontal move offset
     * @param y       vertical move offset
     */
    public void dragAndDrop(By locator, int x, int y) {

        WebElement element;

        try {
            element = browser.getDriver().findElement(locator);
            Actions action = new Actions(browser.getDriver());
            action.dragAndDropBy(element, x, y);

        } catch (Exception e) {
            report.fail("Unable to move the slider");
        }
    }

    /**
     * Scrolls up or down the page
     *
     * @param yAxis Positive value of yAxis indicates upward scroll, negative indicate downward scroll
     */
    public void scrollByJavaScript(int yAxis) {

        try {
            JavascriptExecutor jse = (JavascriptExecutor) browser.getDriver();
            jse.executeScript("window.scroll(0," + yAxis + ")");

        } catch (Exception e) {
            report.fail("Unable to scroll the page");
        }
    }

    /**
     * Navigate to the page
     */
    public void navigate(String url) {

        try {
            browser.getDriver().navigate().to(url);

        } catch (Exception e) {
            report.fail("Error while navigating to the page");
        }
    }

    /**
     * Navigate to the previous page
     */
    public void back() {

        try {
            browser.getDriver().navigate().back();

        } catch (Exception e) {
            report.fail("Error while navigating backward");
        }
    }

    /**
     * Move a single "item" forward in the browser's history. Does nothing if we are on the latest page viewed.
     */
    public void forward() {

        try {
            browser.getDriver().navigate().forward();

        } catch (Exception e) {
            report.fail("Error while navigating forward");
        }
    }

    /**
     * Refresh the current page
     */
    public void reloadPage() {

        try {
            browser.getDriver().navigate().refresh();

        } catch (Exception e) {
            report.fail("Unable to reload the page");
        }
    }

    /**
     * Returns the current URL
     *
     * @return current URL
     */
    public String getCurrentUrl() {

        try {
            return browser.getDriver().getCurrentUrl();

        } catch (Exception e) {
            report.fail("Unable to fetch the current url");
            return null;
        }
    }

    /**
     * Switches to tab by its title
     *
     * @param title Contains title of tab
     */
    public void switchToTab(String title) {
        try {

            // flag variable to check if tab is present or not
            boolean isTabPresent = false;

            // Stores default tab before switching to any tab
            String defaultWindowId = browser.getDriver().getWindowHandle();

            // Set of all tabs
            Set<String> allWindows = browser.getDriver().getWindowHandles();

            // Iterate through all tabs and search for the given tab
            for (String currentWindowId : allWindows) {
                if (browser.getDriver().switchTo().window(currentWindowId).getTitle().contains(title)) {
                    isTabPresent = true;
                    break;
                }
            }
            // If tab with given title is not present then switch to default tab
            if (!isTabPresent) {
                report.fail("No tab present with title " + title);
                browser.getDriver().switchTo().window(defaultWindowId);
            }
        } catch (Exception e) {
            report.fail("Unable to switch to window " + title);
        }
    }

    /**
     * Closes tab with given title
     *
     * @param title Contains title of tab
     */
    public void closeTab(String title) {
        try {
            boolean isTabPresent = false;

            // Stores default tab before switching to any tab
            String defaultWindowId = browser.getDriver().getWindowHandle();

            // Set of all tabs
            Set<String> allWindows = browser.getDriver().getWindowHandles();

            // Iterate through all tabs and search for the given tab and close it
            for (String currentWindowId : allWindows) {
                if (browser.getDriver().switchTo().window(currentWindowId).getTitle().contains(title)) {
                    browser.getDriver().close();
                    isTabPresent = true;
                    break;
                }
            }

            // If tab with given title is not present then switch to default tab
            if (!isTabPresent) {
                report.fail("No window available with title " + title);
                browser.getDriver().switchTo().window(defaultWindowId);
            }
        } catch (Exception e) {
            report.fail("Unable to switch to window " + title);
        }
    }

    /**
     * Wait for element for a specific period of time
     *
     * @param locator  Locator of webElement
     * @param waitType Type of wait
     * @return : boolean
     */
    public boolean waitForElement(By locator, WaitType waitType) {

        try {
            WebDriverWait wait = new WebDriverWait(browser.getDriver(), explicitWait);
            switch (waitType) {

                case VISIBILITY_OF_ELEMENT_LOCATED:
                    wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    return true;

                case ABSENCE_OF_ALL_ELEMENTS:
                    wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(locator)));
                    return true;

                case ELEMENT_TO_BE_CLICKABLE:
                    wait.until(ExpectedConditions.elementToBeClickable(locator));
                    return true;

                case ELEMENT_TO_BE_SELECTED:
                    wait.until(ExpectedConditions.elementToBeSelected(locator));
                    return true;

                case INVISIBILITY_OF_ELEMENT_LOCATED:
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
                    return true;

                case PRESENCE_OF_ELEMENT_LOCATED:
                    wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    return true;

                default:
                    log.warn("Entered WaitType '" + waitType + "'is invalid");
                    return false;
            }
        } catch (Exception e) {
            report.fail("Exception occurred in waitType " + waitType + " for " + locator);
            return false;
        }
    }

    /**
     * Pause the screen for 'N' seconds
     *
     * @param timeInMilliSeconds time to wait
     */
    public void pause(int timeInMilliSeconds) {

        try {
            Thread.sleep(timeInMilliSeconds);

        } catch (Exception e) {
            report.fail("Unable to wait for " + timeInMilliSeconds);
        }
    }

    /**
     * Switches to frame with given id
     *
     * @param FrameId Id of the frame to switch
     */
    public void switchToFrame(int FrameId) {
        browser.getDriver().switchTo().frame(FrameId);
    }

    /**
     * Switches to frame with given id or name
     *
     * @param FrameIdOrName id or name of the frame
     */
    public void switchToFrame(String FrameIdOrName) {
        browser.getDriver().switchTo().frame(FrameIdOrName);
    }

    /**
     * Wait for presence of element with polling time
     *
     * @param locatorToWait         Contains locator which presence needs to check
     * @param pollingTimeInMilliSec Contains polling time in milliseconds
     * @param timeoutInSeconds      Contains time out period in seconds
     * @return True if element is found within given time , otherwise false
     */
    public boolean waitForPresenceWithPolling(By locatorToWait, int pollingTimeInMilliSec, long timeoutInSeconds) {

        try {
            WebDriverWait wait = new WebDriverWait(browser.getDriver(), explicitWait);

            // Keep polling with the interval of 'pollingTimeInMilliSec' milliseconds
            wait.pollingEvery(Duration.ofMillis(pollingTimeInMilliSec));

            // Wait until loader is present in DOM
            wait.until(ExpectedConditions.presenceOfElementLocated(locatorToWait));

            // Returns true if element is present
            return true;

        } catch (Exception e) {
            // False if timeout or other exception occurs
            return false;
        }
    }

    // Different type of 'By' waits
    public enum WaitType {
        VISIBILITY_OF_ELEMENT_LOCATED, ABSENCE_OF_ALL_ELEMENTS, ELEMENT_TO_BE_CLICKABLE, ELEMENT_TO_BE_SELECTED, INVISIBILITY_OF_ELEMENT_LOCATED, PRESENCE_OF_ELEMENT_LOCATED
    }
}
