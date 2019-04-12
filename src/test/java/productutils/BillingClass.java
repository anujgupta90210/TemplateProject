package productutils;

import frameworkutils.BrowserFactory;
import frameworkutils.LogManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import productutils.CommonUtils;

import java.util.List;

public class BillingClass extends BaseTest {

    public static String Bill_Cycle_Name;
    public static String billingdate, ServiceInstanceNumber, CustomerAccountNumber, BillingAccountNumber, ServiceAccountNumber;
    public static String jobstatus = "Completed";
    CAAMClass caam = new CAAMClass();
    private LogManager log = LogManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();
    private BaseTest baseTest = BaseTest.getInstance();

    //Created by: Anju
    //Method for Create Bill Cycle
    @Step
    public String createBillCycle() {
        try {

            baseTest.pause(5000);

            clickOverSubMenu("Billing", "Billing Configuration", "Billing Cycle Management");
            log.info("Clicked on Billing Cycle Management ");

            baseTest.pause(500);
            WebElement iframe = findElement1(locators.get("iframe"));
            switchToFrame(iframe);
            baseTest.pause(5000);

            clickonElement1("//*[@id=\"btncreatebillcycle\"][text()='Create']");
            log.info("Clicked on final create button");
            baseTest.pause(5000);

            Bill_Cycle_Name = getAlias("txt_bill_cycle_name") + generateRandomString();
            WebElement BillCycle_Name = findElement(locators.get("txt_bill_cycle_name"));
            sendTextToElement(BillCycle_Name, Bill_Cycle_Name);
            log.info("Entered bill cycle Name");


            String BillCycle_Alias = generateRandomString().toUpperCase();
            WebElement Bill_Cycle_Alias = findElement(locators.get("txt_bill_cycle_alias"));
            sendTextToElement(Bill_Cycle_Alias, BillCycle_Alias);
            log.info("Entered bill cycle Alias");

            clickElement("#cmbbilldateshift-real");

            String date = getAlias("drp_down_bill_date_shift");

            List<WebElement> Bill_Date = findElements1(locators.get("drp_down_bill_date_shift"));

            for (int i = 0; i < Bill_Date.size(); i++) {
                String date1 = Bill_Date.get(i).getText();

                if (date1.equals(date)) {
                    Bill_Date.get(i).click();
                    log.info("Selected Bill Date Shift");
                    break;
                }
            }


            baseTest.pause(2000);
            clickElement("#cmbbasemonth-btn");

            String basemonth = getAlias("Drp_down_BaseMonth");
            List<WebElement> BaseMonth = findElements1(locators.get("Drp_down_BaseMonth"));

            for (int i = 0; i < BaseMonth.size(); i++) {
                String date1 = BaseMonth.get(i).getText();

                if (date1.equals(basemonth)) {
                    BaseMonth.get(i).click();
                    log.info("Selected Base Month");
                    break;
                }
            }

            clickElement("#cmbbillperiod-btn");

            String Billing_frequency = getAlias("Billing_frequency");
            List<WebElement> Billingfrequency = findElements1(locators.get("Drp_Down_BF"));

            for (int i = 0; i < Billingfrequency.size(); i++) {

                String Bill_frequency = Billingfrequency.get(i).getText();
                if (Bill_frequency.equals(Billing_frequency)) {
                    Billingfrequency.get(i).click();
                    log.info("Selected Billing frequency");
                    break;
                }
            }


            baseTest.pause(2000);
            clickElement("#dtstartdate-btn");
            baseTest.pause(2000);
            String dateinput = getAlias("Start_Date");

            List<WebElement> StartDate = findElements1(locators.get("Start_Date"));

            for (int i = 0; i < StartDate.size(); i++) {
                String Sdate1 = StartDate.get(i).getText();
                if (Sdate1.equals(dateinput)) {
                    StartDate.get(i).click();
                    log.info("Selected Start Date");
                    break;
                }
            }

            clickElement("#btncreatebillcycle");
            log.info("Clciked on final create button");

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return Bill_Cycle_Name;
    }

    /**
     * It will run the bill generation service to generate the bill
     *
     * @param accountNumber
     * @return It will return response message
     * @Author Darshan kasliwala
     * @UpdatedBy Chetan bhoi (change method name, return response message
     */
    @Step
    public String runBillGenerationService(String accountNumber) {
        String response = null;
        try {
            //Search customer of whoom we have to generate bill
            caam.searchCustomer(accountNumber);
            baseTest.pause(3000);
            //Click on plan name
            clickOnElement(By.cssSelector(locators.get("tab_ServiceInstance")));
            //Expand the billing section
            baseTest.pause(3000);
            ServiceInstanceNumber = getTextFromElement(By.xpath(locators.get("serviceinstancenumber")));
            CustomerAccountNumber = getTextFromElement(By.xpath(locators.get("customeraccountnumbe")));
            BillingAccountNumber = getTextFromElement(By.xpath(locators.get("billingaccountnumber")));
            ServiceAccountNumber = getTextFromElement(By.xpath(locators.get("srvaccnumber")));

            clickOnRightMenu("Billing", "Bill Date Information");

            //Get next billing date
            billingdate = getTextFromTableView(By.cssSelector("#billdateinfolistbox"), 1, 6);
            log.info(billingdate);

            //Click on submenu Service Configuration
            clickOverSubMenu("InfraTools", "Service Management", "Service Configuration");
            baseTest.pause(2000);
            //Switch to default frame on application
            switchToFrame1("iframe");
            // Click on service config tab
            clickOnElement(By.xpath(locators.get("tab_serviceconfiguration")));
            baseTest.pause(2000);
            //Expand the Billing process section
            clickOnElement(By.xpath(locators.get("exp_panelBillingExpand")));
            baseTest.pause(2000);
            //Select Bill generation service
            clickOnElement(By.xpath(locators.get("BillGenrationService")));

            waitUntilVisible(By.xpath(locators.get("Manuallyrunbtn")));
            //Click on manually run button
            clickOnElement(By.xpath(locators.get("Manuallyrunbtn")));
            /* It will create Runtime Xpath by using string  Bill_Cycle_Name which will get us after runnging createBillCycle method.
                createBillCycle method return string Bill_Cycle_Name.
            */
            String xp_str1 = ("//*[text()=");
            String xp_str2 = ("'");
            String xp_str3 = "BillCycle_01";//BillingClass.Bill_Cycle_Name;
            String xp_str4 = ("'");
            String xp_str5 = ("]");
            String str_elem = xp_str1 + xp_str2 + xp_str3 + xp_str4 + xp_str5;

            //log.info(str_elem);
            //  select bill cycle from available bill cycle
            clickOnElement(By.xpath(str_elem));
            // Click on add to selected bill cycle
            clickOnElement(By.xpath(locators.get("selectbillingcyclebtn")));
            // Below to set next bill date selecting from calneder
            //Cal.setDate(billingdate);


            // Below code is to remove readonly from bill run date text are and then we send billing date in that text field.
            List<WebElement> inputs = browser.getDriver().findElements(By.xpath(locators.get("billrundateinput")));

            for (WebElement input : inputs) {
                ((JavascriptExecutor) browser.getDriver()).executeScript(
                        "arguments[0].removeAttribute('readonly','readonly')", input);
            }
            browser.getDriver().findElement(By.xpath(locators.get("billrundateinput"))).clear();
            browser.getDriver().findElement(By.xpath(locators.get("billrundateinput"))).sendKeys(billingdate);


            baseTest.pause(3000);
            // Click on manually run button
            browser.getDriver().findElement(By.xpath(locators.get("Manuallyrunbtn1"))).click();
            baseTest.pause(5000);
            // Click on OK button on pop which will display after click on manually run button.
            waitUntilVisible(By.xpath(locators.get("okbtn")));
            browser.getDriver().findElement(By.xpath(locators.get("okbtn"))).click();

            response = getTextFromElement(By.xpath(".//*[contains(@class,'z-messagebox-window')]//div[@class='z-window-content']"));
        } catch (Exception e) {
            log.info(e.getMessage());
            response = "Bill is not generated.";
        }
        return response;
    }

    @Step
    public void verifyBillgenerationService() {
        try {
            //Click on submenu Service Configuration
            clickOverSubMenu("InfraTools", "Service Management", "Service Configuration");
            baseTest.pause(2000);
            //Switch to default frame on application
            switchToFrame1("iframe");

            // Select billing process from drop down
            selectValueFromDropDownBox(By.xpath(locators.get("Moduleddbtn")), By.xpath(locators.get("divmoduledd")), "Billing Process");
            baseTest.pause(2000);
            //selectValueFromDropDownBox(By.xpath(locators.get("Servicenamebtn")),By.xpath(locators.get("divservicedd")),"Bill Generation Service");
            //baseTest.pause(2000);


            // Click on service name drop down button
            browser.getDriver().findElement(By.xpath(locators.get("Servicenamebtn"))).click();
            pause(500);

            // Select bill generation service  from drop down
            browser.getDriver().findElement(By.xpath(locators.get("billgenerationdd"))).click();
            pause(500);

            // Click on search button
            browser.getDriver().findElement(By.xpath(locators.get("searchbtn"))).click();
            pause(500);

            // Get total entites executed in job
            String totalcount = getTextFromTableView(By.cssSelector("#searchresult"), 1, 4);
            log.info(totalcount);

            // Get failed entites executed in job
            String failedcount = getTextFromTableView(By.cssSelector("#searchresult"), 1, 6);

            int failedcount1 = Integer.parseInt(failedcount);
            log.info(failedcount);
            // Get skipped entites executed in job

            String skiipedcount = getTextFromTableView(By.cssSelector("#searchresult"), 1, 7);
            int skiipedcount1 = Integer.parseInt(skiipedcount);
            log.info(skiipedcount);

            // Get status of job executed
            jobstatus = getTextFromTableView(By.cssSelector("#searchresult"), 1, 9);
            log.info(jobstatus);

            // below condition to check if failed and skipped count is greater than zero then then billing acoount is displaying in failed or skipped link at process detial tab
            if (jobstatus.equalsIgnoreCase("Completed")) {

                if (failedcount1 > 0 || skiipedcount1 > 0) {

                    clickOnLinkFromTableView(By.cssSelector("#searchresult"), 1, 12);
                    browser.getDriver().findElement(By.xpath(locators.get("processdetailtab"))).click();

                    if (failedcount1 > 0) {
                        browser.getDriver().findElement(By.xpath(locators.get("faillink"))).click();
                        browser.getDriver().findElement(By.xpath(locators.get("entityid"))).sendKeys(BillingAccountNumber);//BillingAccountNumber

                        browser.getDriver().findElement(By.xpath(locators.get("searchentityidbtn"))).click();

                        if (isElementPresent(By.cssSelector("entitybox428-empty"))) {
                            String EntityidF = getTextFromTableView(By.cssSelector("#entitybox428"), 1, 2);
                            log.info(EntityidF);
                            if (EntityidF.equals(BillingAccountNumber))
                                log.info("Bill Process Job Run failed for " + BillingAccountNumber);
                        }

                        browser.getDriver().findElement(By.xpath(locators.get("entitydetailclsoebtn"))).click();
                    }

                    if (skiipedcount1 > 0) {
                        browser.getDriver().findElement(By.xpath(locators.get("skiplink"))).click();
                        browser.getDriver().findElement(By.xpath(locators.get("entityid"))).sendKeys(BillingAccountNumber);//

                        browser.getDriver().findElement(By.xpath(locators.get("searchentityidbtn"))).click();

                        if (isElementPresent(By.cssSelector("entitybox428-empty"))) {
                            String EntityidS = getTextFromTableView(By.cssSelector("#entitybox428"), 1, 2);
                            log.info(EntityidS);
                            if (EntityidS.equals(BillingAccountNumber))
                                log.info("Bill Process Job Run Skipped for " + BillingAccountNumber);
                        }
                        browser.getDriver().findElement(By.xpath(locators.get("entitydetailclsoebtn"))).click();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Below method is to check bill is generated for customer after checking job status is completed.
     Created by @Darshan Kasliwal
     Date - 08/02/2019
     updated by: chetan bhoi <make parameterize method>
     date: 19/02/2019
     */
    @Step
    public String check_invoicae_generated_at_billingaccount(String CustAccountNumber) {
        String BD = "";
        try {
            //Below method is executed after job status is completed.
            if (jobstatus.equalsIgnoreCase("Completed")) {
                //Search the customer for whoom job is run
                caam.searchCustomer(CustAccountNumber); //CustomerAccountNumber
                baseTest.pause(2000);

                // Click on billing acoount link
                browser.getDriver().findElement(By.xpath(locators.get("billingaccountlink"))).click();
                clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 4);
                //Click to expand financial info
                clickOnRightMenu("Financial Information", "Financial Details");

                // Veirify that billing date is equal to bill date
                String BN = getTextFromTableView(By.cssSelector("#fullbillviewgrid"), 1, 2);
                log.info(BN);
                if (BN.contains("REG")) {
                    BD = getTextFromTableView(By.cssSelector("#fullbillviewgrid"), 1, 6);
                    log.info(BD);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BD;

    }

    /**
     * Checks if sales invoice amount displayed is same as mentioned in dispatched bill page
     *
     * @param customerAccountNumber account number of the customer
     * @return boolean
     * @author anuj gupta
     */
    @Step
    public boolean verifySalesInvoice(String customerAccountNumber) {

        // stores result
        boolean result = false;
        try {
            // search the customer
            caam.searchCustomer(customerAccountNumber);

            // Click on billing account number
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox-body"), 1, 4);

            // Clicks on Financial details under financial information
            clickOnRightMenu("Financial Information", "Financial Details");

            // Stores sales invoice displayed
            String amountBilled = getTextFromElement(By.xpath("(//span[text()='Sales Invoice']/ancestor::tr//span)[2]")).split(" ")[1];

            // Clicks on link to view sales invoice dispatched bill
            clickonElement1("//span[text()='Sales Invoice']/ancestor::tr//a");

            // Switch to new window
            switchToWindows(1);

            // Stores amount displayed in dispatched bill
            String amountPayable = getTextFromElement(By.xpath("//span[contains(text(),'Total Payable Amount')]/parent::td/following-sibling::td/span"));
            close_browser();
            baseTest.pause(500);

            // switch back to default window
            switchToWindows(0);

            // checks if amount displayed is same as amount in dispatched bill
            if (amountBilled.equalsIgnoreCase(amountPayable))
                result = true;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * Checks if regular invoice amount displayed is same as mentioned in dispatched bill page
     *
     * @param customerAccountNumber account number of the customer
     * @return boolean
     * @author anuj gupta
     */
    @Step
    public boolean verifyRegularInvoice(String customerAccountNumber) {

        // stores result
        boolean result = false;
        try {
            // search the customer
            caam.searchCustomer(customerAccountNumber);

            // Click on billing account number
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox-body"), 1, 4);

            // Clicks on Financial details under financial information
            clickOnRightMenu("Financial Information", "Financial Details");

            // Stores sales invoice displayed
            clickonElement1("//span[text()='Regular Invoice']/ancestor::tr//a");

            // Stores sales invoice displayed
            String amountBilled = getTextFromElement(By.xpath("(//span[text()='Regular Invoice']/ancestor::tr//span)[2]")).split(" ")[1];

            // Switch to new window
            switchToWindows(1);

            // Stores amount displayed in dispatched bill
            String amountPayable = getTextFromElement(By.xpath("//span[contains(text(),'Total Payable Amount')]/parent::td/following-sibling::td/span"));
            close_browser();
            baseTest.pause(500);

            // switch back to default window
            switchToWindows(0);

            // checks if amount displayed is same as amount in dispatched bill
            if (amountBilled.equalsIgnoreCase(amountPayable))
                result = true;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * It will return response message of stop bill is done or not
     *
     * @param customerAccountNumber
     * @param stopDate
     * @Author chetan bhoi
     * @Date 12/03/2019
     */
    @Step
    public String markStopBill(String customerAccountNumber, String stopDate) {
        String response = null;
        try {
            caam.searchCustomer(customerAccountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 3);

            pause(500);
            clickOnRightMenu("Billing", "Start/Stop Billing");

            pause(1000);
            boolean isPresent = isElementPresent(By.cssSelector("#stopbillingfromdateradio-real"));

            if (isPresent) {
                clickOnElement("#stopbillingfromdateradio-real");
                CommonUtils.sendDate(By.cssSelector("#dtstopfromdate-real"), stopDate);

                sendTextToElement(By.cssSelector("#billingreasontext"), "Test Remark");
                clickOnElement("#btnupdate");

                WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
                String message = msgbox.getText().split("OK")[0].trim();

                log.info(message);
                response = message;

                msgbox.findElement(By.cssSelector("#btnok")).click();
                pause(2000);

            } else {
                response = "Customer is not valid for stop billing.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Customer is not valid for stop billing.";
        }
        return response;
    }

    /**
     * It will return response message of start bill is done or not
     *
     * @param customerAccountNumber
     * @param startDate
     * @Author chetan bhoi
     * @Date 12/03/2019
     */
    @Step
    public String markStartBill(String customerAccountNumber, String startDate) {
        String response = null;
        try {
            boolean isElePresent = isElementPresent(By.cssSelector("#startbillingradio-real"));
            if (!isElePresent) {
                caam.searchCustomer(customerAccountNumber);
                clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 3);
                pause(500);
                clickOnRightMenu("Billing", "Start/Stop Billing");
            }
            clickOnElement(By.cssSelector("#startbillingradio-real"));
            clickOnElement(By.cssSelector("#startbillingfromdateradio-real"));
            CommonUtils.sendDate(By.cssSelector("#dtstartfromdate-real"), startDate);

            sendTextToElement(By.cssSelector("#billingreasontext"), "Test Remark");
            clickOnElement(By.cssSelector("#btnupdate"));
            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText().split("OK")[0].trim();
            log.info(message);
            response = message;

        } catch (Exception e) {
            e.printStackTrace();
            response = "Customer is not valid for start billing.";
        }
        return response;
    }
}
