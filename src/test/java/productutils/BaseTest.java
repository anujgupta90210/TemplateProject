package productutils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.sterlite.qa.sterlock.core.util.FileUtil;
import com.sterlite.qa.sterlock.core.util.JsonUtil;
import frameworkutils.BrowserFactory;
import frameworkutils.ExcelManager;
import frameworkutils.ReportManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BaseTest {

    protected static Map<String, String> savedValues = new LinkedHashMap<>();
    private static BaseTest baseTest;
    private ExcelManager excel = ExcelManager.getInstance();
    protected Map<String, String> locators = excel.getColumnAsMap("locators", 0, 1);
    protected Map<String, String> aliases = excel.getColumnAsMap("aliases", 0, 1);
    private BrowserFactory browser = BrowserFactory.getInstance();
    private ReportManager report = ReportManager.getInstance();

    BaseTest() {
    }

    public static BaseTest getInstance() {

        if (baseTest == null)
            baseTest = new BaseTest();
        return baseTest;
    }

    public void scrollTo(WebElement element) {

        try {
            ((JavascriptExecutor) browser.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
            report.pass("Scrolled to element: " + element.toString());

        } catch (Exception e) {
            report.fail("Unable to scroll to element: " + element.toString());
        }
    }

    public Map<String, String> getSavedValues() {
        return savedValues;
    }

    public String getSavedValues(String key) {
        return savedValues.get(key);
    }

    public String getTextFromTableView(By tableView, int rowNum, int cellNum) {

        String response = "";

        try {
            rowNum = rowNum - 1;
            cellNum = cellNum - 1;
            Thread.sleep(500);
            WebElement table;
            // waitUntilVisible(By.cssSelector("#accountinfolistbox-rows"));
            if (tableView.toString().contains("searchaccountlistbox")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#searchaccountlistbox-rows"));
            } else if (tableView.toString().contains("billdateinfolistbox")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#billdateinfolistbox-rows"));
            } else if (tableView.toString().contains("searchresult")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#searchresult-rows"));
            } else if (tableView.toString().contains("entitybox428")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#entitybox428-rows"));
            } else if (tableView.toString().contains("fullbillviewgrid")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#fullbillviewgrid-cave"));
            } else {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#accountinfolistbox-rows"));
            }
            List<WebElement> trList = table.findElements(By.tagName("tr"));
            List<WebElement> tdList = trList.get(rowNum).findElements(By.tagName("td"));
            if (cellNum < tdList.size())
                response = tdList.get(cellNum).getText();
            // System.out.println("In table list view, click on:" + tdList.get(cellNum).getText());

            report.pass("Fetch text " + response + " from table view " + tableView.toString());

        } catch (NoSuchElementException e) {
            report.fail("Unable to fetch text from table view " + tableView.toString());
        } catch (InterruptedException e) {
            report.fail("Unable to fetch text from table view " + tableView.toString());
        }
        return response;
    }

    public void navigate(String url) {

        try {
            browser.getDriver().navigate().to(url);
            report.pass("Navigated to the url: " + url);

        } catch (Exception e) {
            report.fail("Unable to navigate to the url: " + url);
        }
    }

    public void selectindex1(String xPath, int indexNumber) {

        try {
            Select list = new Select(browser.getDriver().findElement(By.xpath(xPath)));
            list.selectByIndex(indexNumber);
            report.pass("Selected element using xpath: " + xPath + " and index: " + indexNumber);

        } catch (Exception e) {
            report.fail("Unable to select element using xpath: " + xPath + " and index: " + indexNumber);
        }
    }

    public void selectindex1(By by, int indexNumber) {

        try {
            Select list = new Select(browser.getDriver().findElement(by));
            list.selectByIndex(indexNumber);
            report.pass("Selected element using xpath: " + by + " and index: " + indexNumber);

        } catch (Exception e) {
            report.fail("Unable to select element using xpath: " + by + " and index: " + indexNumber);
        }
    }

    public void takeScreenShot(String fileName) {

        try {
            SimpleDateFormat tSdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String imageName = fileName + "_" + tSdf.format(timestamp) + ".jpg";
            String destfile = System.getProperty("user.dir") + "/fail-screenshots/";
            File dir = new File(destfile);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String filePath = destfile + imageName;
            File screenshotFile = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile, new File(destfile));
            report.pass("Captured screenshot");

        } catch (Exception e) {
            report.fail("Unable to capture screenshot");
        }
    }

    /**
     * Below method is to find webelement have retry logic
     *
     * @author Darshan Kasliwal
     */
    public WebElement findElementBy(By by, int... milliseconds) {

        int sleep = 200; // number of milliseconds to wait before retrying
        int numberOfRetries = 10; // default number of retries
        if (milliseconds.length > 0) {
            numberOfRetries = milliseconds[0] / sleep;
            if (numberOfRetries <= 0) {
                numberOfRetries = 1;
            }
        }
        ArrayList<WebElement> elements = new ArrayList<>();
        WebElement element = null;

        try {
            for (int i = 0; i < numberOfRetries; i++) {
                elements = (ArrayList<WebElement>) browser.getDriver().findElements(by);
                if (elements.size() > 0) {
                    element = elements.get(0);
                    return element;
                } else
                    Thread.sleep(sleep);

                report.pass("Find element using locator: " + by.toString());
            }
        } catch (Exception e) {
            report.fail("Unable to find element using the locator: " + by.toString());
        }
        return element;
    }

    /**
     * Writes data to csv file
     *
     * @param filePath Path where csv file is placed
     * @param data     Data to be entered in csv file in a particular cell
     * @param row      Row number in which data needs to be entered
     * @param col      Column number in which data needs to be entered
     * @author anuj gupta
     */
    public void writeToCSV(String filePath, String data, int row, int col) {

        try {
            File inputFile = new File(filePath);

            // Read existing file
            CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
            List<String[]> csvBody = reader.readAll();

            // get CSV row column  and replace with by using row and column
            csvBody.get(row)[col] = data;
            reader.close();

            // Write to CSV file which is open
            CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',');
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();
            report.pass("Wrote data " + data + " to csv file at path: " + filePath + " at row: " + row + " and column: " + col);

        } catch (Exception e) {
            report.fail(e.getMessage());
        }
    }

    /**
     * Generates a random number of desired length
     *
     * @param len Length of the random number reqbrowser.getDriver()red
     * @return random number of desired length
     * @author anuj gupta
     */
    public long createRandomNumber(double len) {

        long result = 0;
        try {
            long tLen = (long) Math.pow(10, len - 1) * 9;
            result = ((long) (Math.random() * tLen) + (long) Math.pow(10, len - 1));
            report.pass("Random number: " + result + " generated of lenght: " + len);

        } catch (Exception e) {
            report.fail("Unable to generate random number of length: " + len);
        }
        return result;
    }

    /**
     * Returns true if element is present, else returns false
     *
     * @param by Locator of the element to be found
     * @return boolean
     * @author anuj gupta
     */
    protected boolean isElementPresent(By by) {

        try {
            if (browser.getDriver().findElement(by).isDisplayed()) {
                browser.getDriver().findElement(by);
                return true;
            } else
                return false;

        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Selects value from radio button based on text provided
     *
     * @param text text to be selected
     * @author anuj gupta
     */
    public void selectRadioButton(String text) {

        try {
            By radioButton = By.xpath("(//span[input[@type='radio']][label[text()='" + text + "']])[1]");
            waitUntilVisible(radioButton);
            conditionwait(radioButton);
            report.pass("Selected radio button: " + text);

        } catch (Exception e) {
            report.fail(e.getMessage());
        }
    }

    /**
     * Uploads file using Robot class
     *
     * @param filePath Path where file to be uploaded is located
     * @author anuj gupta
     */
    public void uploadFileWithRobot(String filePath) {

        try {
            // Stores file path
            StringSelection stringSelection = new StringSelection(filePath);

            // Instantiate clipboard
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            // Performs 'ctrl + c'
            clipboard.setContents(stringSelection, null);

            Robot robot = new Robot();
            robot.delay(250);

            // Press 'enter' key
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            // Performs 'ctrl + v'
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            // Press 'enter' key
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(150);
            robot.keyRelease(KeyEvent.VK_ENTER);

            report.pass("Uploaded file using robot framework");

        } catch (Exception e) {
            report.fail("Unable to upload file using robot framework");
        }
    }

    /**
     * Generates random string of desired length
     *
     * @param length Length of the random string
     * @return Random String
     * @author anuj gupta
     */
    public String generateRandomString(int length) {

        String random = null;

        try {
            random = RandomStringUtils.randomAlphabetic(length);
            report.pass("Generated random string: " + random + " of length: " + length);

        } catch (Exception e) {
            report.fail("Unable to generate random string of length: " + length);
        }
        return random;
    }

    /**
     * Wait for element for a specific period of time
     *
     * @param locator  Locator of webElement
     * @param waitType Type of wait
     * @return boolean
     * @author anuj gupta
     */
    public boolean waitForElement(By locator, WaitType waitType) {

        WebDriverWait wait = new WebDriverWait(browser.getDriver(), 20);
        String logMessage = "waited for " + waitType + " for " + locator;
        try {
            switch (waitType) {

                // Waits until visibility of element is located
                case VISIBILITY_OF_ELEMENT_LOCATED:
                    wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    report.pass(logMessage);
                    return true;

                // Waits until absence of all elements in DOM
                case ABSENCE_OF_ALL_ELEMENTS:
                    wait = new WebDriverWait(browser.getDriver(), 25);
                    wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(locator)));
                    report.pass(logMessage);
                    return true;

                // Waits until element is clickable
                case ELEMENT_TO_BE_CLICKABLE:
                    wait.until(ExpectedConditions.elementToBeClickable(locator));
                    report.pass(logMessage);
                    return true;

                // Wait until element is selected
                case ELEMENT_TO_BE_SELECTED:
                    wait.until(ExpectedConditions.elementToBeSelected(locator));
                    report.pass(logMessage);
                    return true;

                // Wait until element is invisible
                case INVISIBILITY_OF_ELEMENT_LOCATED:
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
                    report.pass(logMessage);
                    return true;

                // Wait until presence of element located
                case PRESENCE_OF_ELEMENT_LOCATED:
                    wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    report.pass(logMessage);
                    return true;

                default:
                    report.fail("Entered WaitType '" + waitType + "'is invalid");
                    return false;
            }
        } catch (Exception e) {
            report.fail("Exception occurred while " + logMessage);
            return false;
        }
    }

    public void clickonElement1(String xpath) {

        try {
            waitUntilVisible(By.xpath(xpath));
            conditionwait(By.xpath(xpath));

            WebElement element = browser.getDriver().findElement(By.xpath(xpath));
            element.click();
            report.pass("Clicked on element with xpath: " + xpath);

        } catch (Exception e) {
            report.fail("Unable to click on element with xpath: " + xpath);
        }
    }

    public void clickonElement1(By by) {

        try {
            waitUntilVisible(by);
            conditionwait(by);

            WebElement element = browser.getDriver().findElement(by);
            element.click();
            report.pass("Clicked on element with xpath: " + by);

        } catch (Exception e) {
            report.fail("Unable to click on element with xpath: " + by);
        }
    }

    public void navigatTo(String url) {

        try {
            browser.getDriver().get(url);
            report.pass("Navigated to the url: " + url);

        } catch (Exception e) {
            report.fail("Unable to navigate to the url: " + url);
        }
    }

    public void switchTo(String iFrameName) {

        try {
            browser.getDriver().switchTo().frame(iFrameName);
            report.pass("Unable to switch to iframe: " + iFrameName);

        } catch (Exception e) {
            report.fail("Unable to switch to iframe: " + iFrameName);
        }
    }

    public boolean screenGrab(String prefix) {

        try {
            SimpleDateFormat tSdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String sdf = tSdf.format(new Date());
            byte[] scrFile = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.BYTES);
            Path outputFile = Paths.get(prefix + "-" + sdf + ".jpg");
            Files.write(outputFile, scrFile);
            System.out.println("test:::" + outputFile.toAbsolutePath());
            report.pass("Screenshot captured");
            return true;

        } catch (IOException e) {
            report.fail("Unable to capture screenshot");
            return false;
        }
    }

    public String getAlias(String key) {

        String alias = null;

        try {
            alias = aliases.get(key);
            report.pass("Fetched alias: " + alias + " using key: " + key);

        } catch (Exception e) {
            report.fail("Unable to fetch alias: " + alias + " using key: " + key);
        }
        return alias;
    }

    public String getLocator(String key) {

        String locator = null;

        try {
            locator = locators.get(key);
            report.pass("Fetched locator: " + key + " using key: " + key);

        } catch (Exception e) {
            report.fail("Unable to fetch alias: " + key + " using key: " + key);
        }
        return locator;
    }

    public void clickWithHighlight(WebElement element) {

        try {
            highlight(element);
            element.click();
            report.pass("Clicked on element: " + element.toString());

        } catch (Exception e) {
            report.fail("Unable to click on element: " + element.toString());
        }
    }

    public void highlight(WebElement element) {

        try {
            String style = element.getAttribute("style");
            JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
            js.executeScript("arguments[0].setAttribute('style','background:yellow; border: 1px solid red;');", element);
            pause(250);
            js.executeScript("arguments[0].setAttribute('style','" + style + "');", element);
            report.pass("Highlighted the element: " + element.toString());

        } catch (Exception e) {
            report.fail("Unable to highlight the element: " + element.toString());
        }
    }

    public void close() {

        try {
            browser.getDriver().close();
            browser.getDriver().quit();
            report.pass("Closed the broser");

        } catch (Exception e) {
            report.fail("Unable to close the browser");
        }
    }

    public void setDefaultFocus() {
        browser.getDriver().switchTo().defaultContent();
    }

    public List<WebElement> scrape() {

        String[] tags = {"input", "button", "a"};

        List<WebElement> elements = null;

        String opFolder = "intranet";
        elements = new ArrayList<>();
        List<String> frames = frames();
        if (frames.size() > 1) {
            for (String frame : frames) {
                browser.getDriver().switchTo().frame(frame);
                writePageSourceToFile(opFolder);
                for (String tag : tags) {
                    try {
                        elements.addAll(browser.getDriver().findElements(By.tagName(tag)));
                    } catch (Exception e) {
                    }
                }
                browser.getDriver().switchTo().defaultContent();
            }
        } else {
            writePageSourceToFile(opFolder);
            for (String tag : tags) {
                try {
                    elements.addAll(browser.getDriver().findElements(By.tagName(tag)));
                } catch (Exception e) {
                }
            }
        }
        return elements;
    }

    public List<String> frames() {

        List<String> retVal = null;

        try {
            retVal = new ArrayList<>();
            List<WebElement> frames = browser.getDriver().findElements(By.tagName("frame"));
            String idOrName;
            for (WebElement frame : frames) {
                idOrName = "".equalsIgnoreCase(frame.getAttribute("id")) ? frame.getAttribute("name") : frame.getAttribute("id");
                retVal.add(idOrName);
            }
            report.pass("Found list of frames: " + retVal);

        } catch (Exception e) {
            report.fail("Unable to find list of frame");
        }
        return retVal;
    }

    public WebElement findElement(String attributeName, List<WebElement> elements) {

        String[] tags = {"id", "name"};
        WebElement ele = null;

        try {
            for (WebElement element : elements) {
                for (String tag : tags) {
                    if (element.getAttribute(tag).equalsIgnoreCase(attributeName)) {
                        ele = element;
                        break;
                    }
                }
            }
            report.pass("Find element using attribute: " + attributeName + " using list of elements");

        } catch (Exception e) {
            report.fail("Unable to find element using attribute: " + attributeName + " using list of elements");
        }
        return ele;
    }

    public List<WebElement> findElement(By by, List<WebElement> elements) {

        List<WebElement> retVal = null;

        try {
            retVal = new ArrayList<>();
            for (WebElement element : elements) {
                String selector = by.toString();
                if (selector.equalsIgnoreCase("tagname")) {
                    if (element.getTagName().equalsIgnoreCase(selector)) {
                        retVal.add(element);
                    }
                }
            }
            report.pass("Found element " + by.toString() + "using list of elements");

        } catch (Exception e) {
            report.fail("Unable to find element " + by.toString() + "using list of elements");
        }
        return retVal;
    }

    public void findSubmitAndClick(List<WebElement> elements) {

        try {
            String[] tags = {"onclick"};
            for (WebElement element : elements) {
                for (String tag : tags) {
                    if (null != element.getAttribute(tag) && element.getAttribute(tag).contains("submit")) {
                        element.click();
                        break;
                    }
                }
            }
            report.pass("Clicked on submit button using list of elements");

        } catch (Exception e) {
            report.fail("Unable to click on submit button using list of elements");
        }
    }

    public List<WebElement> findElements(By by) {

        List<WebElement> elements = null;

        try {
            waitUntilVisible(by);
            elements = browser.getDriver().findElements(by);
            report.pass("Found list of elements using locator: " + by.toString());

        } catch (Exception e) {
            report.fail("Unable to find list of elements using locator: " + by.toString());
        }
        return elements;
    }

    public void hoverOverMenu(String menu) {

        try {
            pause(200);
            browser.getDriver().switchTo().defaultContent();
            hoverOverElement(menu, findElements(By.tagName("span")), false);
            report.pass("Hovered over menu: " + menu);

        } catch (Exception e) {
            report.fail("Unable to hover over menu: " + menu);
        }
    }

    public List<String> getMenuNames() {

        List<String> retVal = null;

        try {
            retVal = new ArrayList<>();
            browser.getDriver().switchTo().frame("leftFrame");
            for (WebElement element : findElements(By.tagName("div"))) {
                retVal.add(element.getText());
            }
            browser.getDriver().switchTo().defaultContent();
            report.pass("Fetched list of menu names: " + retVal.toString());

        } catch (Exception e) {
            report.fail("Unable to fetch list of menu names");
        }
        return retVal;
    }

    public List<String> getSubMenuNames() {

        List<String> retVal = null;

        try {
            retVal = new ArrayList<>();
            for (WebElement element : findElements(By.tagName("div"))) {
                retVal.add(element.getText());
            }
            browser.getDriver().switchTo().defaultContent();
            report.pass("Fetched list of sub menu names: " + retVal.toString());

        } catch (Exception e) {
            report.fail("Unable to fetch list of sub menu names");
        }
        return retVal;
    }

    public void hoverOverSubMenu(String menu, String subMenu) {

        try {
            hoverOverMenu(menu);
            hoverOverElement(subMenu, findElements(By.tagName("div")), false);
            report.pass("Hovered over sub menu: " + subMenu + " under menu: " + menu);

        } catch (Exception e) {
            report.fail("Unable to hover over sub menu: " + subMenu + " under menu: " + menu);
        }
    }

    public void clickOverMenu(String menuName) {

        try {
            browser.getDriver().switchTo().defaultContent();
            clickOnElement(menuName, findElements(By.tagName("span")), false);
            report.pass("Clicked over menu: " + menuName);

        } catch (Exception e) {
            report.fail("Unable to clicked on menu: " + menuName);
        }
    }

    public void clickOverSubMenu(String menu, String subMenu) {

        try {
            hoverOverMenu(menu);
            clickOnElement(subMenu, findElements(By.tagName("span")), false);
            report.pass("Clicked on sub menu: " + subMenu + " of menu: " + menu);

        } catch (Exception e) {
            report.fail("Unable to click on sub menu: " + subMenu + " of menu: " + menu);
        }
    }

    public void clickOverSubMenu(String menu, String subMenu, String subMenu2) {

        try {
            hoverOverMenu(menu);
            hoverOverMenu(subMenu);
            clickOnElement(subMenu2, findElements(By.tagName("span")), false);
            report.pass("Clicked on sub menu: " + subMenu2 + " under sub menu: " + subMenu + " under menu: " + menu);

        } catch (Exception e) {
            report.fail("Unable to click on sub menu: " + subMenu2 + " under sub menu: " + subMenu + " under menu: " + menu);
        }
    }

    public void hoverOverElement(String linkName, List<WebElement> links, boolean strict) {

        try {
            for (WebElement link : links) {
                //System.out.println(link.getText());
                if (strict) {
                    if (link.getText().equals(linkName)) {
                        Actions action = new Actions(browser.getDriver());
                        action.moveToElement(link).build().perform();
                        pause(250);
                        break;
                    }
                } else {
                    if (link.getText().trim().toLowerCase().contains(linkName.toLowerCase().trim())) {
                        Actions action = new Actions(browser.getDriver());
                        action.moveToElement(link).build().perform();
                        pause(250);
                        break;
                    }
                }
            }
            report.pass("Hovered over element: " + linkName + " from list of links. ");

        } catch (Exception e) {
            report.fail("Unable to perform mouse hover over element: " + linkName + " from list of links.");
        }
    }

    public void clickOnElement(String linkName, List<WebElement> links, boolean strict) {

        try {
            for (WebElement link : links) {
                // System.out.println(link.getText());
                if (strict) {
                    if (link.getText().equals(linkName)) {
                        link.click();
                        break;
                    }
                } else {
                    if (link.getText().trim().toLowerCase().contains(linkName.toLowerCase().trim())) {
                        link.click();
                        break;
                    }
                }
            }
            report.pass("Clicekd on element: " + linkName + " from list of links");

        } catch (Exception e) {
            report.fail("Unable to click on element: " + linkName + " from list of links");
        }
    }

    public void writePageSourceToFile(String opFolder) {

        try {
            File op = new File(opFolder);
            if (!op.exists())
                op.mkdirs();
            String title = op.getAbsolutePath() + File.separator + browser.getDriver().getTitle().toLowerCase() + "_" + System.currentTimeMillis() + ".html";
            String content = browser.getDriver().getPageSource();
            FileUtil.fastWrite(title, content);
            report.pass("Write page to source file: " + opFolder);

        } catch (Exception e) {
            report.fail("Unable to write page to source file: " + opFolder);
        }
    }

    public void clickByTextAndTag(String text, String tagName) {

        try {
            List<WebElement> list = browser.getDriver().findElements(By.tagName(tagName));
            for (WebElement link : list) {
                if (link.getText().toLowerCase().contains(text.toLowerCase())) {
                    scrollTo(link);
                    System.out.println("Clicking on " + link.getText());
                    link.click();
                    break;
                }
            }
            report.pass("Clicked by text: " + text + " and tag: " + tagName);

        } catch (Exception e) {
            report.fail("Unable to click by text: " + text + " and tag: " + tagName);
        }
    }

    /* Below method is to select value from drop down by Visible Text.
   Created by @Darshan Kasliwal
   Date - 06/02/2019
    */
    public void Select_DropDown_ByVisibleText(By by, String visibleText) {

        try {
            new Select(browser.getDriver().findElement(by)).selectByVisibleText(visibleText);
            report.pass("Selected " + visibleText + " from dropdown " + by.toString());

        } catch (Exception e) {
            report.fail("Unable to select value " + visibleText + " from the dropdown " + by.toString());
        }
    }

    /* Below method is to select value from drop down by Value.
Created by @Darshan Kasliwal
Date - 06/02/2019
*/
    public void Select_DropDown_ByValue(By by, String Value) {

        try {
            new Select(browser.getDriver().findElement(by)).selectByValue(Value);
            report.pass("Selected " + Value + " from dropdown " + by.toString());

        } catch (Exception e) {
            report.fail("Unable to select " + Value + " from the dropdown " + by.toString());
        }
    }

    public String getCurrentDate() {

        String strDate = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            strDate = dateFormat.format(new Date());
            report.pass("Current date is: " + strDate);

        } catch (Exception e) {
            report.fail("Unable to fetch current date.");
        }
        return strDate;
    }

    public void startVideoRecording() {
        /*

        TODO: @Sanjeev & @Hitesh POC on Groupon's Selenium Grid Extras :- https://github.com/groupon/Selenium-Grid-Extras
        Verify the highlight & unhighlighted methods pinched from Stack Overflow.

        SGE modules that are interesting

        selenium-download
        webdriver-http-sync
        artemisia
        poller

        */
    }

    public String stopVideoRecording() {
        /*

        TODO: @Sanjeev & @Hitesh POC on Groupon's Selenium Grid Extras :- https://github.com/groupon/Selenium-Grid-Extras

        SGE modules that are interesting

        selenium-download
        webdriver-http-sync
        artemisia
        poller

        */
        return null;
    }

    public Set<String> getWindowIds() {

        Set<String> winID = null;

        try {
            winID = browser.getDriver().getWindowHandles();
            report.pass("Get window handles successful");

        } catch (Exception e) {
            report.fail("Unable to fetch window handles.");
        }
        return winID;
    }

    public WebElement waitUntilVisible(By by) {

        WebElement element = null;

        try {
            WebDriverWait wait = new WebDriverWait(browser.getDriver(), 20);
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            report.pass("Waited for visibility of element " + by.toString());

        } catch (Exception e) {
            report.fail("Unable to wait for visibility of element " + by.toString());
        }
        return element;
    }

    private void sendPerfStatsToTimingsApi() {

        try {
            String data = getPerformanceTimingsFromBrowser();
            report.pass("Performance timings from browser: " + data);
//        ta.send("navtiming", data);

        } catch (Exception e) {
            report.fail("Unable to fetch performance timings for browser.");
        }
    }

    public void findInputByAttribute(String attributeName, String value, String text) {

        try {
            WebElement element = browser.getDriver().findElement(By.cssSelector("[" + attributeName + "=\"" + value + "\"]"));
            element.clear();
            element.sendKeys(text);
            report.pass("Send text " + text + " to element using attribute: " + attributeName + " with value: " + value);

        } catch (Exception e) {
            report.fail("Unable to send text " + text + " to element using attribute: " + attributeName + " with value: " + value);
        }
    }

    public void navigateTo(String url) {

        try {
            browser.getDriver().get(url);
            report.pass("Navigated to url: " + url);

        } catch (Exception e) {
            report.fail("Unable to navigate to the url: " + url);
        }

//        sendPerfStatsToTimingsApi();
    }

    public void clickOnElement(String cssSelector) {

        try {
            waitUntilVisible(By.cssSelector(cssSelector));
            conditionwait(By.cssSelector(cssSelector));

            List<WebElement> elements = browser.getDriver().findElements(By.cssSelector(cssSelector));
            elements.get(0).click();

            report.pass("Clicked on element with cssSelector: " + cssSelector);

        } catch (Exception e) {
            report.fail("Unable to click on the element with cssSelector: " + cssSelector);
        }
    }

    //Created by chetan bhoi
    //Date: 19/02/2019
    public String sendTextToTableView1(String tableID, int rowNum, int cellNum, String value) {

        String response = null;
        try {
            rowNum = rowNum - 1;
            cellNum = cellNum - 1;
            Thread.sleep(500);
            String tablemain = null;
            String tablerows = null;

            if (!tableID.contains("body")) {
                if (!tableID.contains("rows")) {
                    tablemain = tableID;
                    tablerows = tableID + "-rows";
                } else {
                    tablerows = tableID;
                    tablemain = tableID.split("-")[0];
                }
            } else {
                tablemain = tableID.split("-")[0];
                tablerows = tablemain + "-rows";
            }
            WebElement table = browser.getDriver().findElement(By.id(tablemain)).findElement(By.id(tablerows));
            List<WebElement> trList = table.findElements(By.tagName("tr"));
            List<WebElement> tdList = trList.get(rowNum).findElements(By.tagName("td"));
            System.out.println("In table list view, click on:" + tdList.get(cellNum).getText());
            tdList.get(cellNum).findElement(By.tagName("input")).sendKeys(value);

            report.pass("Entered text: " + value + " at row: " + rowNum + " and cell: " + cellNum);

        } catch (Exception e) {
            report.fail("Unable to enter input value: " + value + " at row: " + rowNum + " and cell: " + cellNum);
        }
        return response;
    }

    public void clickOnElement(By by) {

        try {
            waitUntilVisible(by);
            conditionwait(by);

            browser.getDriver().findElement(by).click();
            report.pass("Clicked on element: " + by.toString());

        } catch (NoSuchElementException e) {
            report.fail("Unable to click on the element: " + by.toString());
        }
    }

    public void clickOnLinkFromTableView(By tableView, int rowNum, int cellNum) {


        try {
            waitUntilVisible(tableView);

            rowNum = rowNum - 1;
            cellNum = cellNum - 1;
            Thread.sleep(500);
            WebElement table;
            // waitUntilVisible(By.cssSelector("#accountinfolistbox-rows"));
            if (tableView.toString().contains("searchaccountlistbox")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#searchaccountlistbox-rows"));
            } else if (tableView.toString().contains("billdateinfolistbox")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#billdateinfolistbox-rows"));
            } else if (tableView.toString().contains("searchresult")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#searchresult-rows"));
            } else if (tableView.toString().contains("entitybox428")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#entitybox428-rows"));
            } else if (tableView.toString().contains("fullbillviewgrid")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#fullbillviewgrid-cave"));
            } else if (tableView.toString().contains("badetaillistbox")) {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#badetaillistbox-cave"));
            } else {
                table = browser.getDriver().findElement(tableView).findElement(By.cssSelector("#accountinfolistbox-rows"));
            }

            List<WebElement> trList = table.findElements(By.tagName("tr"));
            List<WebElement> tdList = trList.get(rowNum).findElements(By.tagName("td"));
            tdList.get(cellNum).click();
            System.out.println("In table list view, click on:" + tdList.get(cellNum).getText());

        } catch (Exception e) {
            report.fail(e.getMessage());
        }
    }

    public void clickOnLinkFromTableView1(String tableID, int rowNum, int cellNum) {

        try {
            rowNum = rowNum - 1;
            cellNum = cellNum - 1;
            Thread.sleep(500);
            String tablemain = null;
            String tablerows = null;

            if (!tableID.contains("body")) {
                if (!tableID.contains("rows")) {
                    tablemain = tableID;
                    tablerows = tableID + "-rows";
                } else {
                    tablerows = tableID;
                    tablemain = tableID.split("-")[0];
                }
            } else {
                tablemain = tableID.split("-")[0];
                tablerows = tablemain + "-rows";
            }
            WebElement table = browser.getDriver().findElement(By.id(tablemain)).findElement(By.id(tablerows));
            List<WebElement> trList = table.findElements(By.tagName("tr"));
            List<WebElement> tdList = trList.get(rowNum).findElements(By.tagName("td"));
            tdList.get(cellNum).click();
            System.out.println("In table list view, click on:" + tdList.get(cellNum).getText());

            report.pass("Clicked on element in table view: " + tableID + " at rowNum: " + rowNum + " and columnNo: " + cellNum);

        } catch (Exception e) {
            report.fail("Unable to click on element in table view: " + tableID + " at rowNum: " + rowNum + " and columnNo: " + cellNum);

        }
    }

    //created by Chetan Bhoi
    public String getLinkTextFromTableView1(String tableID, int rowNum, int cellNum) {

        String response = null;

        try {
            rowNum = rowNum - 1;
            cellNum = cellNum - 1;
            Thread.sleep(500);
            String tablemain = null;
            String tablerows = null;

            if (!tableID.contains("body")) {
                if (!tableID.contains("rows")) {
                    tablemain = tableID;
                    tablerows = tableID + "-rows";
                } else {
                    tablerows = tableID;
                    tablemain = tableID.split("-")[0];
                }
            } else {
                tablemain = tableID.split("-")[0];
                tablerows = tablemain + "-rows";
            }
            Thread.sleep(1000);
            WebElement table = browser.getDriver().findElement(By.id(tablemain)).findElement(By.id(tablerows));
            List<WebElement> trList = table.findElements(By.tagName("tr"));
            Thread.sleep(200);
            List<WebElement> tdList = trList.get(rowNum).findElements(By.tagName("td"));
            Thread.sleep(200);
            System.out.println("In table list view, click on:" + tdList.get(cellNum).getText());
            response = tdList.get(cellNum).getText();

            report.pass("Sucessfully fetched text " + response + " from table view: " + tableID + " at row: " + rowNum + " and cell: " + cellNum);

        } catch (Exception e) {
            report.fail("Unable to fetch text from table view: " + tableID + " at row: " + rowNum + " and cell: " + cellNum);
        }
        return response;
    }

    public void clickOnRightMenu(String menuName, String subMenuName) {

        try {
            String menucss = "#" + menuName.toLowerCase();
            menucss = menucss.replaceAll(" ", "");
            System.out.println(menucss);
            //waitUntilVisible(By.cssSelector(menucss));
            Thread.sleep(1000);
            WebElement table = browser.getDriver().findElement(By.cssSelector(menucss));
            List<WebElement> divlist = table.findElements(By.tagName("div"));
            int i = 0;
            for (WebElement div : divlist) {
                String menu = div.getText().trim();
                if (menu.equals(menuName.trim())) {
                    div.findElement(By.tagName("i")).click();
                    System.out.println("click on menu:" + menu);
                    List<WebElement> alist = table.findElements(By.tagName("a"));
                    for (WebElement a : alist) {
                        pause(200);
                        String submenu = a.getText();
                        if (submenu.equalsIgnoreCase(subMenuName)) {
                            a.click();
                            System.out.println("click on submenu:" + submenu);
                            i = 1;
                            break;
                        }
                    }
                }
                if (i == 1)
                    break;
            }
            report.pass("Clicked on submenu link: " + subMenuName + " under menu: " + menuName);

        } catch (Exception e) {
            report.fail("Unable to click on submenu link: " + subMenuName + " under menu: " + menuName);

        }
    }

    public void selectValueFromDropDownBox(By openbtnElement, By listboxElement, String valueName) {

        try {
            System.out.println(listboxElement);
            clickOnElement(openbtnElement);
            Thread.sleep(500);
            WebElement listbox = browser.getDriver().findElement(listboxElement);
            List<WebElement> elements = listbox.findElements(By.tagName("span"));
            for (WebElement element : elements) {
                String value = element.getText();
                if (value.equalsIgnoreCase(valueName)) {
                    System.out.println("Selecting value from dropdwonbox:" + value);
                    element.click();
                    pause(500);
                    break;
                }
            }
            report.pass("Selected value: " + valueName + " from dropdown: " + listboxElement);

        } catch (Exception e) {
            report.fail("Unable to select value: " + valueName + " from dropdown: " + listboxElement);
        }
    }

    public void selectValueFromDropDownBox(By openbtnElement, By listboxElement, int index) {

        try {
            System.out.println(listboxElement);
            clickOnElement(openbtnElement);
            Thread.sleep(500);
            WebElement listbox = browser.getDriver().findElement(listboxElement);
            List<WebElement> elements = listbox.findElements(By.tagName("span"));
            List<WebElement> spanList = new ArrayList<>();
            for (WebElement ele : elements) {
                String text = ele.getText();
                System.out.println("Value:" + text);
                if (!text.equals("")) {
                    spanList.add(ele);
                }
            }
            spanList.get(index).click();
            report.pass("Selected value at index: " + index + " from dropdown " + listboxElement);

        } catch (Exception e) {
            report.fail("Unable to select value at index: " + index + " from dropdown " + listboxElement);
        }
    }

    public void selectValueFromDropDownBox(By listboxElement, String valueName) {

        try {
            WebElement listbox = browser.getDriver().findElement(listboxElement);
            List<WebElement> elements = listbox.findElements(By.tagName("span"));
            for (WebElement element : elements) {
                String value = element.getText();
                if (value.contains(valueName)) {
                    System.out.println("Selecting value from dropdwonbox:" + value);
                    element.click();
                    Thread.sleep(100);
                    break;
                }
            }
            report.pass("Selected value: " + valueName + " from dropdown: " + listboxElement.toString());

        } catch (Exception e) {
            report.fail("Unable to select value: " + valueName + " from dropdown: " + listboxElement.toString());
        }
    }

    public void clickOnRadioButton(String cssSelector, int number) {

        try {
            List<WebElement> elements = browser.getDriver().findElements(By.cssSelector(cssSelector));
            elements.get(number).click();
            report.pass("Clicked on radio button with cssSelector: " + cssSelector + " at index: " + number);

        } catch (Exception e) {
            report.fail("Unable to click on radio button with cssSelector: " + cssSelector + " at index: " + number);
        }
    }

    public void clickOnRadioButton(By by, int number) {

        try {
            waitUntilVisible(by);
            conditionwait(by);

            List<WebElement> elements = browser.getDriver().findElements(by);
            elements.get(number).click();
            report.pass("Clicked on radio button with cssSelector: " + by + " at index: " + number);

        } catch (Exception e) {
            report.fail("Unable to click on radio button with cssSelector: " + by + " at index: " + number);
        }
    }

    public void clickOnRadioButton1(String xpath, int number) {

        try {
            List<WebElement> elements = browser.getDriver().findElements(By.xpath(xpath));
            elements.get(number).click();
            report.pass("Clicekd on radio button using xpath: " + xpath + " at index: " + number);

        } catch (Exception e) {
            report.fail("Unable to click on radio button using xpath: " + xpath + " at index: " + number);
        }
    }

    public void clickOnRadioButton1(By by, int number) {

        try {
            List<WebElement> elements = browser.getDriver().findElements(by);
            elements.get(number).click();
            report.pass("Clicekd on radio button using xpath: " + by + " at index: " + number);

        } catch (Exception e) {
            report.fail("Unable to click on radio button using xpath: " + by + " at index: " + number);
        }
    }

    public void clickElement(String cssSelector) {

        try {
            WebElement element = browser.getDriver().findElement(By.cssSelector(cssSelector));
            element.click();
            report.pass("Clicked on element using cssSelector: " + cssSelector);

        } catch (Exception e) {
            report.fail("Unable to click on element using cssSelector: " + cssSelector);
        }
    }

    public void clickElement(By by) {

        try {
            waitUntilVisible(by);
            conditionwait(by);

            WebElement element = browser.getDriver().findElement(by);
            element.click();
            report.pass("Clicked on element using cssSelector: " + by);

        } catch (Exception e) {
            report.fail("Unable to click on element using cssSelector: " + by);
        }
    }

    public void switchToWindows(int windowNumber) {

        try {
            List<String> winlist = new ArrayList<>(browser.getDriver().getWindowHandles());
            browser.getDriver().switchTo().window(winlist.get(windowNumber));
            report.pass("Switch to window: " + windowNumber);

        } catch (Exception e) {
            report.fail("Unable to switch to window: " + windowNumber);
        }
    }

    public WebElement findElement2(String linkText) {

        WebElement element = null;
        try {
            element = browser.getDriver().findElement(By.linkText(linkText));
            report.pass("Found element using linkText: " + linkText);

        } catch (Exception e) {
            report.fail("Unable to find element using linkText: " + linkText);
        }
        return element;
    }

    public WebElement findElement1(String xpath) {

        WebElement element = null;

        try {
            element = browser.getDriver().findElement(By.xpath(xpath));
            report.pass("Found element using xpath: " + xpath);

        } catch (Exception e) {
            report.fail("Unable to find element using xpath: " + xpath);
        }
        return element;
    }

    public WebElement findElement1(By by) {

        WebElement element = null;

        try {
            element = browser.getDriver().findElement(by);
            report.pass("Found element using xpath: " + by);

        } catch (Exception e) {
            report.fail("Unable to find element using xpath: " + by);
        }
        return element;
    }

    public WebElement findElement3(String tagName) {

        WebElement element = null;
        try {
            element = browser.getDriver().findElement(By.tagName(tagName));
            report.pass("Found element browser.getDriver()sng tagName: " + tagName);

        } catch (Exception e) {
            report.fail("Unable to find element using tagName: " + tagName);
        }
        return element;
    }

    public void clickOnButton(String buttonText) {

        try {
            By locator = By.xpath("//button[text()='" + buttonText + "']");
            waitUntilVisible(locator);

            if (browser.getDriver().findElement(locator).isEnabled()) {
                conditionwait(locator);

                clickByTextAndTag(buttonText, "button");
                report.pass("Clicked on " + buttonText + " button.");
            } else
                report.fail("Button: '" + buttonText + "' is disabled");

        } catch (Exception e) {
            report.fail("Unable to click on " + buttonText + " button");

        }
    }

    public String getPerformanceTimingsFromBrowser() {

        String performanceTiming = null;
        try {
            JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
            performanceTiming = JsonUtil.toJsonString((Map) js.executeScript("return performance.timing"), false);
            report.pass("Successfully fetched performance timings: " + performanceTiming + " from the browser.");

        } catch (Exception e) {
            report.fail("Unable to fetch performance timings from the browser.");

        }
        return performanceTiming;
    }

    public void getwindowhandle() {

        try {
            String parentWindow = browser.getDriver().getWindowHandle();
            for (String childWindow : browser.getDriver().getWindowHandles()) {
                if (!parentWindow.equalsIgnoreCase(childWindow)) {
                    browser.getDriver().switchTo().window(parentWindow).close();
                    browser.getDriver().switchTo().window(childWindow);
                    report.pass("Switched to child window.");
                } else {
                    browser.getDriver().switchTo().window(parentWindow);
                    report.pass("Switched to parent window.");
                }
            }

        } catch (Exception e) {
            report.fail("Unable to switch window.");
        }
    }

    public void setFocusOnPopupWindow(String id) {

        try {
            Set<String> windowsIds = browser.getDriver().getWindowHandles();
            for (String windowId : windowsIds) {
                if (windowId.contains(id))
                    browser.getDriver().switchTo().window(windowId);
            }
            report.pass("Focus set on pop up window.");

        } catch (Exception e) {
            report.fail("Unable to set focus on pop up window.");
        }
    }

    public void selectFromReason(String locator, String text) {

        try {
            WebElement select = browser.getDriver().findElement(By.cssSelector(locator));
            select.sendKeys(text);
            report.pass("Entered text: " + text + " in input: " + locator);

        } catch (Exception e) {
            report.fail("Unable to enter text: " + text + " in input: " + locator);
        }
    }

    public String gettitle() {
        String title = "";
        try {
            title = browser.getDriver().getTitle();
            report.pass("Title of current window is: " + browser.getDriver().getTitle());

        } catch (Exception e) {
            report.fail("Unable to get title of the current window.");
        }
        return title;
    }

    public void switchBackToDefault(boolean withBase) {

        try {
            String baseHandle = browser.getDriver().getWindowHandle();
            if (withBase)
                browser.getDriver().switchTo().window(baseHandle);
            else
                browser.getDriver().switchTo().defaultContent();
            report.pass("Switched back to default window.");

        } catch (Exception e) {
            report.fail("Unable to switch back to the default window.");
        }
    }

    public WebElement findInputByPlaceHolder(String text) {

        WebElement element = null;

        try {
            element = browser.getDriver().findElement(By.cssSelector("input[placeholder=\"" + text + "\"]"));
            report.pass("Found element using placeholder: " + text);

        } catch (Exception e) {
            report.fail("Unable to found element using palce holder: " + text);
        }
        return element;
    }

    public void scrollToamount() {

        try {
            JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
            js.executeScript("scroll(0,250)");
            report.pass("Scrolled down");

        } catch (Exception e) {
            report.fail("Unable to scroll.");
        }
    }

    public void mousehover(WebElement target) {

        try {
            Actions act = new Actions(browser.getDriver());
            act.moveToElement(target).build().perform();
            report.pass("Mouse hovered over element: " + target.toString());

        } catch (Exception e) {
            report.fail("Unable to hover over element: " + target.toString());
        }
    }

    public void mousehover_singleclick(WebElement target) {
        try {
            Actions act = new Actions(browser.getDriver());
            act.moveToElement(target).click().build().perform();
            report.pass("Single clicked on element: " + target.toString());
        } catch (Exception e) {
            report.fail("Unable to  click on element: " + target.toString());
        }

    }

    public void mousehover_doubleclick(WebElement target) {

        try {
            Actions act = new Actions(browser.getDriver());
            act.doubleClick(target).build().perform();
            //act.moveToElement(target).bbrowser.getDriver()ld().perform();
            report.pass("Double clicked on element: " + target.toString());

        } catch (Exception e) {
            report.fail("Unable to double click on element: " + target.toString());
        }
    }

    public void pause(int millis) {

        try {
            Thread.sleep(millis);
            report.pass("Static wait of: " + millis + " seconds.");

        } catch (InterruptedException e) {
            report.fail("Unable to perfrom static wait");
        }
    }

    public void switchToFrame(String frameId) {

        try {
            browser.getDriver().switchTo().frame(frameId);
            report.pass("Switched to frame: " + frameId);

        } catch (Exception e) {
            report.fail("Unable to switch to frame: " + frameId);
        }
    }

    public void switchToFrame(WebElement element) {

        try {
            browser.getDriver().switchTo().frame(element);
            report.pass("Swithced to frame: " + element.toString());

        } catch (Exception e) {
            report.fail("Unable to switch to frame: " + element.toString());
        }
    }

    public void switchToFrame1(String tagName) {

        try {
            waitUntilVisible(By.tagName(tagName));
            conditionwait(By.tagName(tagName));
            browser.getDriver().switchTo().frame(browser.getDriver().findElement(By.tagName(tagName)));
            report.pass("Switched to frame using tagName: " + tagName);

        } catch (Exception e) {
            report.fail("Unable to switch to frame using tagName: " + tagName);
        }
    }

    public void switchToFramedefault() {

        try {

            browser.getDriver().switchTo().parentFrame();
            report.pass("Switched to Parent frame ");

        } catch (NoSuchFrameException e) {
            report.fail("Unable to switch to parent frame ");
        }
    }

    public void clickAndFocusOnPopup(String locator) {

        try {
            String baseHandle = browser.getDriver().getWindowHandle();
            clickByTextAndTag(locator, "button");
            pause(1500);
            for (String activeHandle : browser.getDriver().getWindowHandles()) {
                if (!activeHandle.equals(baseHandle))
                    browser.getDriver().switchTo().window(activeHandle);
            }
            report.pass("Clicked on set focus on popup using locator: " + locator);

        } catch (Exception e) {
            report.fail("Unable to click and focus on popup using locator: " + locator);
        }
    }

    /*    public void captureElement(WebElement element){

            String Capture=element.getText();
            String t[]=Capture.split(":");
            return ;
        }*/

    public String captureElement(String t, String regex) {

        String element = null;

        try {
            String[] Capture = t.split(regex);
            element = Capture[1].substring(2, Capture[1].length() - 1);
            report.pass("Captured screeshot");

        } catch (Exception e) {
            report.fail("Unable to capture screenshot");
        }
        return element;
    }

    public boolean isElementPresent(WebElement element) {

        try {
            element.isDisplayed();
            report.pass("Checked if element " + element.toString() + "is present or not");
            return true;

        } catch (Exception e) {
            report.fail("Unable to check if element " + element.toString() + "is present or not");
            return false;
        }
    }

    public boolean assertEquals(String expected, String actual) {

        boolean result = false;

        try {
            result = expected.equals(actual);
            report.pass("Check if " + expected + " equals " + actual);

        } catch (Exception e) {
            report.fail("Unable to check if " + expected + " equals " + actual);
        }
        return result;
    }

    public void sendTextToElement(By by, String text) {

        try {
            waitUntilVisible(by);
            browser.getDriver().findElement(by).clear();
            browser.getDriver().findElement(by).sendKeys(text);
            pause(200);

            report.pass("Send text " + text + "to " + by.toString());

        } catch (Exception e) {
            report.fail("Unable to send text " + text + "to " + by.toString());
        }
    }

    public void sendTextToElement(WebElement element, String text) {

        try {
            element.sendKeys(text);
            report.pass("Send text: " + text + " to element: " + element.toString());

        } catch (Exception e) {
            report.fail("Unable to send text: " + text + " to element: " + element.toString());
        }
    }

    public void sendKeysToElement(By by, Keys keys) {

        try {
            browser.getDriver().findElement(by).sendKeys(keys);
            report.pass("Press keys: " + keys.toString() + " in element: " + by.toString());

        } catch (Exception e) {
            report.fail("Unable to press keys: " + keys.toString() + " in element: " + by.toString());
        }
    }

    public void close_browser() {

        try {
            browser.getDriver().close();
            report.pass("Browser closed");

        } catch (Exception e) {
            report.fail("Unable to close the browser.");
        }
    }

    public List<WebElement> findElements(String cssSelectors) {

        List<WebElement> elements = null;

        try {
            elements = browser.getDriver().findElements(By.cssSelector(cssSelectors));
            report.pass("Found elements using cssSelector: " + cssSelectors);

        } catch (Exception e) {
            report.fail("Unable to found element using cssSelector: " + cssSelectors);
        }
        return elements;
    }

    public List<WebElement> findElements1(String xpath) {

        List<WebElement> elements = null;

        try {
            elements = browser.getDriver().findElements(By.xpath(xpath));
            report.pass("Found elements using xpath: " + xpath);

        } catch (Exception e) {
            report.fail("Unable to find element using xpath: " + xpath);
        }
        return elements;
    }

    public WebElement findElement(String cssSelectors) {

        WebElement element = null;

        try {
            element = browser.getDriver().findElement(By.cssSelector(cssSelectors));
            report.pass("Found element: " + element + " using cssSelector: " + cssSelectors);

        } catch (Exception e) {
            report.fail("Unable to find element: " + element + " using cssSelector: " + cssSelectors);
        }
        return element;
    }

    public WebElement findElement(By by) {

        WebElement element = null;

        try {
            element = browser.getDriver().findElement(by);
            report.pass("Found element: " + element + " using cssSelector: " + by);

        } catch (Exception e) {
            report.fail("Unable to find element: " + element + " using cssSelector: " + by);
        }
        return element;
    }

    public void refresh() {

        try {
            browser.getDriver().navigate().refresh();
            report.pass("Refreshed the browser window");

        } catch (Exception e) {
            report.fail("Unable to refresh the browser window.");
        }
    }

    public void implicitywailt() {

        try {
            browser.getDriver().manage().timeouts().implicitlyWait(15, SECONDS);
            report.pass("Applied implicit wait of 15 seconds");

        } catch (Exception e) {
            report.fail("Unable to apply implicit wait of 15 seconds");
        }
    }

    public void Actions(WebElement element, int x, int y) {

        try {
            Actions act = new Actions(browser.getDriver());
            act.clickAndHold(element).moveByOffset(x, y).release().build().perform();
            report.pass("Clicked on element: " + element.toString() + " at location: " + x + " " + y);

        } catch (Exception e) {
            report.fail("Unable to click on element: " + element.toString() + " at location: " + x + " " + y);
        }
    }

    public String generateRandomString() {

        String generatedString = null;

        try {
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);

            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            generatedString = buffer.toString();
            report.pass("Generated random string" + generatedString);

        } catch (Exception e) {
            report.fail("Unable to generate random String");
        }
        return generatedString;
    }

    public void clickOnFirstElement(String cssSelector) {

        try {
            List<WebElement> list = browser.getDriver().findElements(By.cssSelector(cssSelector));
            if (list.size() > 0)
                list.get(0).click();
            else
                System.out.print("Could not locate any element using " + cssSelector);

            report.pass("Clicked on element using cssSelector: " + cssSelector);

        } catch (Exception e) {
            report.fail("Unable to click on element using cssSelector: " + cssSelector);
        }
    }

    public void selectVisibleText(String cssSelector, String text) {

        try {
            Select list = new Select(browser.getDriver().findElement(By.cssSelector(cssSelector)));
            list.selectByVisibleText(text);
            report.pass("Selected value: " + text + " from dropdown with cssSelector: " + cssSelector);

        } catch (Exception e) {
            report.fail("Unable to select value: " + text + " from dropdown with cssSelector: " + cssSelector);
        }
    }

    public void selectVisibleText(By by, String text) {

        try {
            Select list = new Select(browser.getDriver().findElement(by));
            list.selectByVisibleText(text);
            report.pass("Selected value: " + text + " from dropdown with cssSelector: " + by);

        } catch (Exception e) {
            report.fail("Unable to select value: " + text + " from dropdown with cssSelector: " + by);
        }
    }

    public void selectindex(String cssSelector, int indexNumber) {

        try {
            Select list = new Select(browser.getDriver().findElement(By.cssSelector(cssSelector)));
            list.selectByIndex(indexNumber);
            report.pass("Selected value at index: " + indexNumber + " from dropdown with cssSelector: " + cssSelector);

        } catch (Exception e) {
            report.fail("Unable to select value at index: " + indexNumber + " from dropdown with cssSelector: " + cssSelector);
        }
    }

    public void selectindex(By by, int indexNumber) {

        try {
            Select list = new Select(browser.getDriver().findElement(by));
            list.selectByIndex(indexNumber);
            report.pass("Selected value at index: " + indexNumber + " from dropdown with cssSelector: " + by);

        } catch (Exception e) {
            report.fail("Unable to select value at index: " + indexNumber + " from dropdown with cssSelector: " + by);
        }
    }

    public String getTextFromElement(String text) {

        String elementText = null;

        try {
            waitUntilVisible(By.cssSelector(text));
            conditionwait(By.cssSelector(text));

            elementText = browser.getDriver().findElement(By.cssSelector(text)).getText();
            report.pass("Inline text of element: " + text + " is: " + elementText);

        } catch (Exception e) {
            report.fail("Unable to find inline text of element: " + text);
        }
        return elementText;
    }

    public String getTextFromElement(By by) {

        String elementText = "";

        try {
            elementText = browser.getDriver().findElement(by).getText();
            report.pass("Inline text of element: " + by + " is: " + elementText);

        } catch (Exception e) {
            report.fail("Unable to find inline text of element: " + by);
        }
        return elementText;
    }

    public void clickByLinkText(String text) {

        try {
            clickByTextAndTag(text, "a");
            report.pass("Clicked on element: " + text);

        } catch (Exception e) {
            report.fail("Unable to click on element: " + text);

        }
    }

    public void clickElement(WebElement element) {

        try {
            WebDriverWait wait = new WebDriverWait(browser.getDriver(), 20);
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));

            element.click();
            report.pass("Clicked on element: " + element.toString());

        } catch (Exception e) {
            report.fail("Unable to click on element: " + element.toString());
        }
    }

    public void sendKeysToInput(String text, WebElement element) {

        try {
            element.sendKeys(text);
            report.pass("Entered input: " + text + " in element: " + element.toString());

        } catch (Exception e) {
            report.fail("Unable to enter input: " + text + " in element: " + element.toString());
        }
    }

    public void conditionwait(By by) {

        try {
            WebDriverWait wait = new WebDriverWait(browser.getDriver(), 30);
            wait.until(ExpectedConditions.elementToBeClickable(by));
            report.pass("Waited 30 seconds for element to be clickable");

        } catch (Exception e) {
            report.fail("Unable to perform explicit wait for element to be clickable");
        }
    }

    public void documentLoad() {

        try {
            new WebDriverWait(browser.getDriver(), 10).until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            report.pass("Waited for document to get loaded.");

        } catch (Exception e) {
            report.fail("Unable to perform wait for document to get loaded.");
        }
    }

    // Different type of 'By' waits
    public enum WaitType {
        VISIBILITY_OF_ELEMENT_LOCATED, ABSENCE_OF_ALL_ELEMENTS, ELEMENT_TO_BE_CLICKABLE, ELEMENT_TO_BE_SELECTED, INVISIBILITY_OF_ELEMENT_LOCATED, PRESENCE_OF_ELEMENT_LOCATED, FRAME_TO_BE_AVAILABLE_AND_SWITCH_TO_IT
    }
}
