package productutils;

import frameworkutils.BrowserFactory;
import frameworkutils.LogManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SystemClass extends BaseTest {

    private LogManager log = LogManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();

    //Created by Anju
    //Method for AccessGroup Creation
    @Step
    public void createAccessGroup() {
        try {

            clickOverSubMenu("Staff & Access Group", "Access Group Management");
            pause(3000);
            WebElement iframe = findElement1(locators.get("iframe"));
            switchToFrame(iframe);

            clickElement(locators.get("btn_accessgroup"));
            log.info("Clicked on create button");

            String groupName = getAlias("nameaccessgrop") + generateRandomString();

            pause(2000);

            browser.getDriver().findElement(By.xpath(".//*[contains(@id,'txtaccessgroupname')][@title='Name']")).sendKeys(groupName);

            log.info("Entered Name of Access Group");

            clickElement("#chkselectall-real");
            log.info("Select all the modules");

            scrollToamount();

            pause(2000);
            clickElement("#btncreateaccessgroup");
            log.info("Clicked on final create button");

            clickAndFocusOnPopup(".//*[@class='z-messagebox z-div']");

            String message = getTextFromElement(By.xpath(".//*[@class='z-messagebox z-div']"));

            if (message.contains("Access Group is successful")) {
                log.info("Pass");
            }
        } catch (Exception e) {
            log.error(e.getMessage());

        }

    }

    //Created by Anju
    //Method for  Notification template Creation
    @Step
    public void createNotificationTemplate() {
        try {

            pause(2000);
            clickOverSubMenu("System", "Document Template Management", "Notification Template");
            log.info("Clicked on Notification Template");
            pause(5000);

            WebElement iframe = findElement1(locators.get("iframe"));
            switchToFrame(iframe);


            String templateName = getAlias("txt_Template_Name") + generateRandomString();
            browser.getDriver().findElement(By.xpath("//*[@title='Name'][@type=\"text\"]")).sendKeys(templateName);

            clickElement("#combonotificationchannel-btn");
            String notificationchannel = getAlias("drp_dwn_notificationchannel");
            pause(2000);

            List<WebElement> notificationChannel = findElements1(locators.get("drp_dwn_notificationchannel"));

            for (int i = 0; i < notificationChannel.size(); i++) {
                String nc = notificationChannel.get(i).getText();
                if (nc.contains(notificationchannel)) {
                    notificationChannel.get(i).click();
                    log.info("Selected notification channel");
                    break;
                }
            }

            pause(1000);
            clickElement("#comboformatcategory-btn");
            pause(2000);
            String formatCategory = getAlias("Drp_dwn_formatcategory");
            List<WebElement> formatCategory1 = findElements1(locators.get("Drp_dwn_formatcategory"));
            for (int i = 0; i < formatCategory1.size(); i++) {
                String fc = formatCategory1.get(i).getText();
                if (fc.contains(formatCategory)) {
                    formatCategory1.get(i).click();
                    log.info("Selected Format Category");
                    break;
                }
            }

            pause(2000);
            clickElement("#comboformattype-btn");
            String formatType = getAlias("Drp_dwn_format_type");
            pause(2000);
            List<WebElement> formatType2 = findElements1(locators.get("Drp_dwn_format_type"));
            for (int i = 0; i < formatType2.size(); i++) {
                String ft = formatType2.get(i).getText();
                if (ft.contains(formatType)) {
                    formatType2.get(i).click();
                    log.info("Selected Format Type");
                    break;
                }
            }

            pause(2000);
            clickElement("#combomessagecategory-btn");
            String messageCategory = getAlias("Drp_Dwn_MessageCategory");
            pause(2000);
            List<WebElement> messageCategoryLocator = findElements1(locators.get("Drp_Dwn_MessageCategory"));
            for (int i = 0; i < messageCategoryLocator.size(); i++) {
                String mc = messageCategoryLocator.get(i).getText();
                if (mc.contains(messageCategory)) {
                    messageCategoryLocator.get(i).click();
                    log.info("Selected Message Category");
                    break;
                }
            }


            pause(2000);
            WebElement messageContent = findElement1(locators.get("Message_Content"));
            String messageSend = getAlias("Message_Content");
            pause(2000);
            sendTextToElement(messageContent, messageSend);

            pause(1000);
            clickElement("#btncreatetemplate");

            String message = findElement1("//*[@class='z-messagebox z-div']/span").getText();
            log.info(message);
            if (message.contains("Template is successful.")) {
                log.info("Pass");
            }
        } catch (Exception e) {
            log.info("Creation of System Template FAILED.");

        }
    }

    /**
     * Create currency
     *
     * @param isoCode ISO Code
     * @author chetan bhoi
     * @date 26 feb 2019
     */
    @Step
    public String createCurrency(String isoCode) {
        String successMessage = null;
        try {
            clickOverSubMenu("System", "Currency Management", "Currency Configuration");

            pause(500);
            switchToFrame1("iframe");
            clickOnElement(By.cssSelector("#createtab-cnt"));
            sendTextToElement(By.cssSelector("#nametxt"), isoCode);
            sendTextToElement(By.cssSelector("#desctxt"), "testDescription for " + isoCode);

            clickOnElement(By.cssSelector("#overridechk-real"));

            pause(500);
            sendTextToElement(By.cssSelector("#txtisocode"), isoCode);
            sendTextToElement(By.cssSelector("#txtminorunit"), "2");
            sendTextToElement(By.cssSelector("#txtlangcode"), "eg");
            sendTextToElement(By.cssSelector("#txtmajorsy"), "Rs.");
            sendTextToElement(By.cssSelector("#txtcountrycode"), "IN");
            sendTextToElement(By.cssSelector("#txtdecseparator"), ".");
            sendTextToElement(By.cssSelector("#txtgrpseparator"), ",");
            sendTextToElement(By.cssSelector("#txtlocalfrmt"), "¤#,##0.00");
            String rnum = String.valueOf(CommonUtils.getRandomNumber());
            sendTextToElement(By.cssSelector("#numericintbox"), rnum);

            clickOnButton("Create");

            WebElement msgbox = waitUntilVisible(By.xpath("(.//*[@class='z-window-content'])[3]"));
            successMessage = msgbox.getText().split("OK")[0];
            log.info(successMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return successMessage;
    }

    /**
     * Create exchange rate
     *
     * @param fromCurrencyCode from date
     * @param toCurrencyCode   to date
     * @author chetan bhoi
     * @date 26 feb 2019
     */
    @Step
    public String createExchangeRate(String fromCurrencyCode, String toCurrencyCode) {
        String successMessage = "";
        try {
            clickOverSubMenu("System", "Currency Management", "Currency Exchange Rates");
            pause(500);
            switchToFrame1("iframe");
            clickOnElement(By.cssSelector("#createtab-cnt"));
            selectValueFromDropDownBox(By.cssSelector("#from_add_currency_code-btn"), By.cssSelector("#from_add_currency_code-pp"), fromCurrencyCode);
            selectValueFromDropDownBox(By.cssSelector("#to_add_currency_code-btn"), By.cssSelector("#to_add_currency_code-pp"), toCurrencyCode);
            sendTextToElement(By.cssSelector("#exchangerate"), "70");
            sendTextToElement(By.cssSelector("#buyrate"), "75");
            sendTextToElement(By.cssSelector("#sellrate"), "70");
            clickOnButton("Add");

            WebElement msgbox = waitUntilVisible(By.xpath("(.//*[@class='z-window-content'])[3]"));
            successMessage = msgbox.getText().split("OK")[0];
            log.info(successMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return successMessage;
    }

    @Step
    public boolean reloadCache(String cacheID) {

        boolean isReloadSuccessful = false;
        try {
            clickOverSubMenu("System", "Server Administration");

            pause(1000);
            switchToFrame1("iframe");
            sendTextToElement(By.cssSelector("#txtcacheid"), cacheID);

            pause(1000);
            clickOnButton("Reload All");

            if (isElementPresent(By.xpath("//span[text()='All Cache has been Reloaded successfully']")))
                isReloadSuccessful = true;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return isReloadSuccessful;
    }
}
