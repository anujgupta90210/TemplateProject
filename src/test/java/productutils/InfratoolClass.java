package productutils;

import frameworkutils.BrowserFactory;
import frameworkutils.LogManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class InfratoolClass extends BaseTest {

    private LogManager log = LogManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();

    @Step
    public void selectServicesToRun(String processName, String subProcessName) {
        try {
            clickOverSubMenu("InfraTools", "Service Management", "Service Configuration");
            Thread.sleep(500);
            switchToFrame1("iframe");
            clickByTextAndTag("Service Configuration", "a");
            String processCss = "#panel_" + processName.replaceAll(" ", "").toLowerCase();
            String processHeadCss = processCss + "-head";
            String processBodyCss = processCss + "-body";
            WebElement serviceList = waitUntilVisible(By.cssSelector(processCss));
            serviceList.findElement(By.cssSelector(processHeadCss)).findElement(By.tagName("i")).click();
            Thread.sleep(500);
            log.info("Select process name:" + processName);
            WebElement searchbox = serviceList.findElement(By.cssSelector(processBodyCss)).findElement(By.cssSelector("#searchresultlstbox-rows"));
            List<WebElement> trList = searchbox.findElements(By.tagName("tr"));
            for (WebElement tr : trList) {
                int flag = 0;
                List<WebElement> tdlist = tr.findElements(By.tagName("td"));
                for (WebElement td : tdlist) {
                    String tdtext = td.getText();
                    if (tdtext.equalsIgnoreCase(subProcessName)) {
                        tr.click();
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Step
    public void runBillGenerationService() {
        try {
            selectServicesToRun("Billing Process", "Bill Generation Service");
            clickOnButton("Manually Run");
            WebElement masterEntity = waitUntilVisible(By.cssSelector("#masterentitywin"));
            masterEntity.findElement(By.cssSelector("#btnmanuallyrun")).click();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    //Created by Chetan Bhoi
    @Step
    public void runPrepaidStatementGenerationService(String runDate) {
        try {
            selectServicesToRun("Caam", "Prepaid Statement Generation Service");
            pause(1000);
            clickOnButton("Manually Run");
            WebElement masterEntity = waitUntilVisible(By.cssSelector("#masterentitywin"));
            CommonUtils.sendDate(By.xpath("//span[@title='Statement Run Date']/input"), runDate);
            masterEntity.findElement(By.cssSelector("#btnmanuallyrun")).click();
            WebElement msgbox = waitUntilVisible(By.xpath(".//div[@class='z-messagebox z-div']"));
            String message = msgbox.getText();
            log.info(message);
            if (!message.equalsIgnoreCase("Service run manually executed successfully.")) {
                Assert.fail("Prepaid statement generation service is not run successfully.");
            }
        } catch (Exception e) {
            Assert.fail("Prepaid statement generation service is not run successfully.");
        }
    }

    /**
     * It will run dunning unit collector service
     *
     * @param creditClass
     * @return It will return response message
     * @author Chetan Bhoi
     */
    @Step
    public String runDunningUnitCollectorService(String creditClass) {
        String response = null;
        try {
            selectServicesToRun("Dunning", "Dunning Unit Collector Service");
            pause(1000);
            scrollToamount();
            clickOnButton("Manually Run");

            if (!creditClass.equalsIgnoreCase("")) {
                browser.getDriver().findElement(By.xpath(".//span[@title='Credit Class']")).click();
                WebElement listdiv = browser.getDriver().findElement(By.xpath(".//div[contains(@class,'z-combobox-shadow')]"));
                List<WebElement> listli = listdiv.findElements(By.tagName("li"));
                for (WebElement li : listli) {
                    String liText = li.getText();
                    if (liText.equalsIgnoreCase(creditClass)) {
                        li.click();
                        break;
                    }
                }
            }
            pause(1000);
            WebElement masterEntity = waitUntilVisible(By.cssSelector("#masterentitywin"));
            masterEntity.findElement(By.cssSelector("#btnmanuallyrun")).click();
            pause(500);
            pause(200);
            response = getTextFromElement(By.xpath(".//*[contains(@class,'z-messagebox-window')]//div[@class='z-window-content']"));
        } catch (Exception e) {
            log.error(e.getMessage());
            response = "runDunningUnitCollectorService is not run";
        }
        return response;
    }


    /**
     * It will run Entry Queue Service
     *
     * @return It will return response message
     * @author Chetan Bhoi
     */
    @Step
    public String runEntryQueueService() {
        String response = null;
        try {
            selectServicesToRun("Dunning", "Entry Queue Service");
            pause(1000);
            scrollToamount();
            clickOnButton("Manually Run");
            WebElement masterEntity = waitUntilVisible(By.cssSelector("#masterentitywin"));
            masterEntity.findElement(By.cssSelector("#btnmanuallyrun")).click();
            pause(200);
            response = getTextFromElement(By.xpath(".//*[contains(@class,'z-messagebox-window')]//div[@class='z-window-content']"));
        } catch (Exception e) {
            log.error(e.getMessage());
            response = "runEntryQueueService is not run.";
        }
        return response;
    }

    /**
     * It will run process queue service
     *
     * @return It will return response message
     * @author Chetan Bhoi
     */
    @Step
    public String runProcessQueue() {
        String response = null;
        try {
            selectServicesToRun("Dunning", "Process Queue");
            pause(1000);
            scrollToamount();
            clickOnButton("Manually Run");
            WebElement masterEntity = waitUntilVisible(By.cssSelector("#masterentitywin"));
            masterEntity.findElement(By.cssSelector("#btnmanuallyrun")).click();
            pause(200);
            response = getTextFromElement(By.xpath(".//*[contains(@class,'z-messagebox-window')]//div[@class='z-window-content']"));
        } catch (Exception e) {
            log.error(e.getMessage());
            response = "Run process queue service is not run.";
        }
        return response;
    }

    /**
     * @param module
     * @param ServiceName
     * @author Chetan Bhoi
     */
    @Step
    private void searchRunService(String module, String ServiceName) {
        try {
            clickOverSubMenu("InfraTools", "Service Management", "Service Configuration");
            Thread.sleep(500);
            switchToFrame1("iframe");
            selectValueFromDropDownBox(By.cssSelector("#cmbmodule-btn"), By.cssSelector("#cmbmodule-pp"), module);
            selectValueFromDropDownBox(By.cssSelector("#servicetype-btn"), By.cssSelector("#servicetype-pp"), ServiceName);
            clickOnElement(By.cssSelector("#btnsearch"));
            String status = getLinkTextFromTableView1("searchresult", 1, 9);
            while (status.equals("Running")) {
                Thread.sleep(200);
                clickOnElement(By.cssSelector("#btnsearch"));
                status = getLinkTextFromTableView1("searchresult", 1, 9);
            }
        } catch (Exception e) {
            log.info("fail");
        }
    }

    /**
     * @return
     * @author Chetan Bhoi
     */
    @Step
    public String verifyPrepaidStatementGenerationService() {
        String status = null;
        try {
            searchRunService("Caam", "Prepaid Statement Generation Service");
            status = getLinkTextFromTableView1("searchresult", 1, 9);
            log.info(status);
            String serviceName = getLinkTextFromTableView1("searchresult", 1, 3);
            String successStatus = getLinkTextFromTableView1("searchresult", 1, 6);
            if (serviceName.equals("Prepaid Statement Generation Service")) {
                if (!successStatus.equals("0")) {
                    log.info("Prepaid statement is successfully generated.");
                } else {
                    log.info("Prepaid statement is not successfully generated.");
                }
            }
        } catch (Exception e) {
            log.info("Fail to generate prepaid statement.");
        }
        return status;
    }

    /**
     * Performs bulk inventory operation and click on next button. Helper method for upload batch file method
     *
     * @param groupAction Group action dropdown value
     * @param bulkAction  Bulk Action dropdown value
     * @param adapter     Adapter dropdown value
     * @author anuj gupta
     */
    @Step
    private void bulkInventoryOperation(String groupAction, String bulkAction, String adapter) {

        try {
            // Open batch management page
            pause(500);
            clickOverSubMenu("InfraTools", "Bulk Operations", "Batch Management");

            // Switch to iFrame
            pause(500);
            switchToFrame(browser.getDriver().findElement(By.cssSelector(locators.get("split_charge_frame_class"))));

            // Switch to generate upload batch tab
            clickElement("#uploadtab-cave");

            // Enter mandatory details
            selectValueFromDropDownBox(By.cssSelector(locators.get("groupActionDrpdwnBtn")), By.cssSelector(locators.get("groupActionDrpdwnBox")), groupAction);
            selectValueFromDropDownBox(By.cssSelector(locators.get("bulkActionDrpdwnBtn")), By.cssSelector(locators.get("bulkActionDrpdwnBox")), bulkAction);
            selectValueFromDropDownBox(By.cssSelector(locators.get("adapterDrpdwnBtn")), By.cssSelector(locators.get("adapterDrpdwnBox")), adapter);

            clickOnButton("Next");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Uploads the csv batch file
     *
     * @param filePath    Path of the file to be uploaded
     * @param groupAction Group action dropdown value
     * @param bulkAction  Bulk Action dropdown value
     * @param adapter     Adapter dropdown value
     * @author anuj gupta
     */
    @Step
    public String uploadBatchFile(String filePath, String groupAction, String bulkAction, String adapter) {

        String message = "";
        try {
            bulkInventoryOperation(groupAction, bulkAction, adapter);

            clickElement("#btnuploadfile");

            pause(500);
            uploadFileWithRobot(filePath);

            pause(500);
            clickOnElement("#btnsecondstepupload");

            pause(500);
            clickOnElement("#btnfinalstepupload");

            pause(500);
            WebElement msgbox = waitUntilVisible(By.xpath("//span[contains(text(),'File uploaded successfully')]"));
            message = msgbox.getText();

            clickOnButton("OK");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return message;
    }

    /**
     * Processes the batch file uploaded above
     *
     * @param groupAction to select group action from the dropdown
     * @param bulkAction  to select the bulk action from the dropdown
     * @return message for assertion
     * @author anuj gupta
     */
    @Step
    public String processBatch(String groupAction, String bulkAction) {

        String message = "";
        try {
            // Open batch management page
            pause(500);
            clickOverSubMenu("InfraTools", "Bulk Operations", "Batch Management");

            // Switch to iFrame
            pause(500);
            switchToFrame(browser.getDriver().findElement(By.cssSelector(locators.get("split_charge_frame_class"))));

            pause(500);
            // Enter mandatory details
            selectValueFromDropDownBox(By.cssSelector(locators.get("searchGroupActionDrpdwnBtn")), By.cssSelector(locators.get("searchGroupActionDrpdwnBox")), groupAction);
            selectValueFromDropDownBox(By.cssSelector(locators.get("searchBulkActionDrpdwnBtn")), By.cssSelector(locators.get("searchBulkActionDrpdwnBox")), bulkAction);

            // Click search button
            clickOnButton("Search");

            // Select first radio button
            clickOnElement(By.xpath("(//tbody[@id='searchresult-rows']//span)[1]"));

            // Click on process batch button
            clickOnButton("Process Batch");

            WebElement msgbox = waitUntilVisible(By.xpath("//span[contains(text(),'Process is running in background')]"));
            message = msgbox.getText();

            // click on ok button
            clickOnButton("OK");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return message;
    }
}
