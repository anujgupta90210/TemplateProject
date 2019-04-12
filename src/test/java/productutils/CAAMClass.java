package productutils;

import frameworkutils.BrowserFactory;
import frameworkutils.LogManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import productutils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CAAMClass extends BaseTest {

    private LogManager log = LogManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();
    private BaseTest baseTest = BaseTest.getInstance();

    //Created by chetan bhoi
    @Step
    public String createCustomer(String fname, String planType, String planName, String billCycle, String creditClass) {
        String CANumber = null;
        try {
            pause(500);
            clickOverSubMenu("Account", "Create Customer Account");
            pause(700);
            waitUntilVisible(By.tagName("iframe"));
            switchToFrame1("iframe");
            sendTextToElement(By.cssSelector(locators.get("caam.customerFname.txtbox.css")), fname);
            String lname = "Test_" + System.currentTimeMillis();
            sendTextToElement(By.cssSelector(locators.get("caam.customerLname.txtbox.css")), lname);
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerBillingArea.ddb.css")), By.cssSelector("#billingareacombo-pp"), "Default");
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerCategory.ddb.css")), By.cssSelector("#customercategorycombo-pp"), "Default");
            sendTextToElement(By.cssSelector(locators.get("caam.customerAdd1.txtarea.css")), "test");
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerCity.ddb.css")), By.cssSelector("#citycustomercombo-pp"), aliases.get("caam.customerCity"));
            waitUntilVisible(By.cssSelector("#btnconfigureserviceinstancefront"));
            clickOnButton("Configure Service Details >>");

            waitUntilVisible(By.cssSelector(locators.get("caam.customerLevel.ddb.css")));
            String levlValue = aliases.get("caam.customerLevelname");
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerLevel.ddb.css")), By.cssSelector("#business_hierarchy_combo_1-pp"), levlValue);
            if (levlValue.equals("Broadband")) {
                selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerLevel2.ddb.css")), By.cssSelector("#business_hierarchy_combo_2-pp"), planType);
            }
            waitUntilVisible(By.cssSelector("#accountcurrencycombo-btn"));
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerCurrency.ddb.css")), By.cssSelector("#accountcurrencycombo-pp"), aliases.get("caam.currency"));
            waitUntilVisible(By.cssSelector("#postpaidcustomerradio-cnt"));
            clickByTextAndTag(planType, "label");
            pause(500);
            waitUntilVisible(By.cssSelector(locators.get("caam.customerPlanname.ddb.css")));
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerPlanname.ddb.css")), By.cssSelector("#selectplancombo-pp"), planName);
            pause(500);
            clickOnButton("Configure Billing Details >>");

            pause(500);
            if (planType.equalsIgnoreCase("Postpaid")) {
                waitUntilVisible(By.cssSelector(locators.get("caam.customerBillCycle.ddb.css")));
                selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerBillCycle.ddb.css")), By.cssSelector("#billingcyclecombo-pp"), billCycle);
                selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerBillDelmode.ddb.css")), By.cssSelector("#billdeliverymodecombo-pp"), "E-Mail");
                if (!creditClass.equals("")) {
                    selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerCreditClass.ddb.css")), By.cssSelector("#creditclasstype-pp"), creditClass);
                }
                selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerBillFormtype.ddb.css")), By.cssSelector("#cmbbillformattype-pp"), "Detailed");
            }
            clickOnButton("Create Customer");

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            log.info(message.split("OK")[0]);

            WebElement custAccNumber = msgbox.findElement(By.tagName("a"));
            CANumber = custAccNumber.getText();
            custAccNumber.click();
        } catch (Exception e) {
            log.error(e.getMessage());
            takeScreenShot("test");
        }
        return CANumber;
    }

    //Created by chetan bhoi
    @Step
    public String changeStatus(String custAccountNumber, String status, String... temp) {
        String message = null;
        try {
            searchCustomer(custAccountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox-body"), 1, 3);
            clickOnRightMenu("Account", "Change Status");
            selectValueFromDropDownBox(By.cssSelector("#cbnewstatus-btn"), By.cssSelector("#cbnewstatus-pp"), status);
            sendTextToElement(By.cssSelector("#tbchangereason"), "test");

            if (temp.length > 0) {
                CommonUtils.sendDate(By.id("dteffectivedate-real"), "01-02-2019");
            }
            clickOnButton("Change Status");

            if (isElementPresent(By.cssSelector("#btn1")))
                clickOnElement("#btn1");

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            message = msgbox.getText();
            message = message.split("OK")[0].trim();

            clickOnButton("OK");

        } catch (Exception e) {
            log.error(e.getMessage());
            message = "Status is not changed.";
        }
        return message;
    }

    //Created by chetan bhoi
    @Step
    public String getServiceStatus(String customerAccountNumber) {
        String serviceStatus = null;
        try {
            searchCustomer(customerAccountNumber);
            String status = getTextFromTableView(By.cssSelector("#accountinfolistbox-body"), 1, 6);
            serviceStatus = status;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return serviceStatus;
    }

    //Created by chetan bhoi
    @Step
    public String verifySalesInvoice(String custAccountNumber) {
        String response = null;
        try {
            searchCustomer(custAccountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox-body"), 1, 4);
            clickOnRightMenu("Financial Information", "Financial Details");

            WebElement tblbox = browser.getDriver().findElement(By.cssSelector("#fullbillviewgrid-body"));
            List<WebElement> listTr = tblbox.findElements(By.tagName("tr"));
            int i = 0;
            for (WebElement tr : listTr) {
                List<WebElement> listTD = tr.findElements(By.tagName("td"));
                String salesInvoice = null;
                for (WebElement td : listTD) {
                    String tdvalue = td.getText();
                    if (tdvalue.contains("SIT") && !tdvalue.contains("\n")) {
                        salesInvoice = tdvalue;
                    }
                    if (tdvalue.equals("Sales Invoice")) {
                        response = salesInvoice;
                        i = 1;
                        break;
                    }
                }
                if (i == 1) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    /**
     * Verifying regular invoice of customer
     *
     * @param custAccountNumber
     * @return It will return regular invoice number if paas else it will return null value
     * @Author Chetan Bhoi
     */
    @Step
    public String verifyRegularInvoice(String custAccountNumber) {
        String response = null;
        try {
            searchCustomer(custAccountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox-body"), 1, 4);
            clickOnRightMenu("Financial Information", "Financial Details");
            pause(1000);

            WebElement tblbox = browser.getDriver().findElement(By.cssSelector("#fullbillviewgrid-body"));
            List<WebElement> listTr = tblbox.findElements(By.tagName("tr"));
            int i = 0;
            for (WebElement tr : listTr) {
                List<WebElement> listTD = tr.findElements(By.tagName("td"));
                String salesInvoice = null;
                for (WebElement td : listTD) {
                    String tdvalue = td.getText();
                    if (tdvalue.contains("REG") && !tdvalue.contains("\n")) {
                        salesInvoice = tdvalue;
                    }
                    if (tdvalue.equals("Regular Invoice")) {
                        response = salesInvoice;
                        i = 1;
                        break;
                    }
                }
                if (i == 1) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    /**
     * @param custAccountNumber
     * @return It will return invoice status like "Paid", "Unpaid"
     * @Author Chetan bhoi
     * @Date: 19/02/2019
     */
    @Step
    public String verifyInvoiceStatus(String custAccountNumber) {
        String status = null;
        try {
            verifyRegularInvoice(custAccountNumber);
            WebElement tblbox = browser.getDriver().findElement(By.xpath(".//div[@id='fullbillviewgrid-body']//tr[@id='parent_0']"));
            List<WebElement> listTd = tblbox.findElements(By.tagName("td"));
            listTd.get(6).click();
            status = browser.getDriver().findElement(By.xpath(".//div[@id='fullbillviewgrid-body']//tr[@id='child_0']//tr[1]//td[2]")).getText();
            log.info(status);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return status;
    }

    /**
     * Here we check customer's invoice is in dunning status or not.
     *
     * @param custAccountNumber
     * @return It will return status "Yes" or "No"
     * @Author Chetan bhoi
     * Date: 19/02/2019
     */
    @Step
    public String checkInvoiceInDunningStatus(String custAccountNumber) {
        String status = null;
        try {
            verifyRegularInvoice(custAccountNumber);
            pause(1000);
            WebElement tblbox = browser.getDriver().findElement(By.xpath(".//div[@id='fullbillviewgrid-body']//tr[@id='parent_0']"));
            List<WebElement> listTd = tblbox.findElements(By.tagName("td"));
            listTd.get(6).click();
            pause(1000);
            scrollToamount();
            pause(1000);
            status = browser.getDriver().findElement(By.xpath(".//div[@id='fullbillviewgrid-body']//tr[@id='child_0']//tr[2]//td[4]")).getText();
            pause(1000);
            log.info(status);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return status;
    }

    /**
     * Customer request for promise to pay
     *
     * @param customerAccountNumber
     * @param dueDate
     * @return It will return message
     * Date: 19/02/2019
     * @Author Chetan Bhoi
     */
    @Step
    public String promiseToPayRequest(String customerAccountNumber, String dueDate) {
        String response = null;
        try {
            searchCustomer(customerAccountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 4);
            clickOnRightMenu("Financial Information", "Promise To Pay Request");
            clickOnElement(By.xpath(".//*[@id='raiseptprequesttab-cnt']"));
            clickOnLinkFromTableView1("raiseptprequestlistbox", 1, 1);
            String outStandingAmt = getLinkTextFromTableView1("raiseptprequestlistbox", 1, 5);
            sendTextToTableView1("raiseptprequestlistbox", 1, 7, outStandingAmt);

            //send promise due date
            CommonUtils.sendDate(By.xpath(".//*[@id='promiseduedate-real']"), dueDate);
            sendTextToElement(By.xpath(".//*[@id='remark']"), "Test PTP");
            clickOnButton("Submit");
            pause(500);
            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            log.info(message.split("OK")[0]);
            response = message;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    //Created by chetan bhoi
    @Step
    public String createBillingAccount(String accountNumber, String accountType, String creditClass) {
        String response = null;
        try {
            searchCustomer(accountNumber);
            clickOnRightMenu("Account", "Create Billing Account");
            sendTextToElement(By.cssSelector(locators.get("caam.createBAFirstname.txtbox.css")), "NewBAFname");
            sendTextToElement(By.cssSelector(locators.get("caam.createBALastname.txtbox.css")), "NewBALname");
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.createBAPattern.ddb.css")), By.cssSelector("#chargingpatterncombo-pp"), accountType);
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.createBABillCycle.ddb.css")), By.cssSelector("#billingcyclecombo-pp"), aliases.get("caam.billingCyclename"));
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.createBABillDelmode.ddb.css")), By.cssSelector("#billdeliverymodecombo-pp"), aliases.get("caam.billDeleMode"));
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.createBACurrency.ddb.css")), By.cssSelector("#accountcurrencycombo-pp"), aliases.get("caam.currency"));
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.createBABillFormtype.ddb.css")), By.cssSelector("#cmbbillformattype-pp"), aliases.get("caam.billFormType"));


            if (!creditClass.equalsIgnoreCase("")) {
                selectValueFromDropDownBox(By.cssSelector(locators.get("caam.createBACreditClass.ddb.css")), By.cssSelector("#creditclasstype-pp"), creditClass);
            }

            sendTextToElement(By.cssSelector(locators.get("caam.createBAAdd1.txtarea.css")), "test");
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.createBACity.ddb.css")), By.cssSelector("#citybillingcombo-pp"), aliases.get("caam.customerCity"));
            waitUntilVisible(By.cssSelector(locators.get("caam.createBACreateBA.btn.css")));
            clickOnButton("Create Billing Account");

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            response = message.split("OK")[0];
            log.info(response);

            clickOnButton("OK");
/*

            WebElement banumber = msgbox.findElement(By.tagName("a"));
            savedValues.put("postpaid_NewBA", banumber.getText());
            log.info(getSavedValues("postpaid_NewBA"));
            banumber.click();
*/

        } catch (Exception e) {
            response = null;
            log.error(e.getMessage());
        }
        return response;
    }

    //Created by chetan bhoi
    @Step
    public String createServiceAccount(String accountNumber) {
        String response = null;
        try {
            searchCustomer(accountNumber);
            clickOnRightMenu("Account", "Create Service Account");
            sendTextToElement(By.cssSelector(locators.get("caam.createSAFirstname.txtbox.css")), "NewBAFname");
            sendTextToElement(By.cssSelector(locators.get("caam.createSALastname.txtbox.css")), "NewBALname");

            sendTextToElement(By.cssSelector(locators.get("caam.createSAAdd1.txtarea.css")), "test");
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.createSACity.ddb.css")), By.cssSelector("#citysicombo-pp"), aliases.get("caam.customerCity"));
            waitUntilVisible(By.cssSelector("#btncreateserviceaccount"));
            clickOnButton("Create Service Account");

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            response = message.split("OK")[0];
        } catch (Exception e) {
            response = null;
            log.error(e.getMessage());
        }
        return response;
    }

    //Created by chetan bhoi
    @Step
    public String addServiceInstance(String accountNumber, String billingAccountNumber, String planType, String planName) {
        String response = null;
        try {
            searchCustomer(accountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 5);
            clickOnRightMenu("Package", "Add Service Instance");

            browser.getDriver().findElement(By.xpath("//a[starts-with(@id,'zk_comp') and contains(@id,'-btn')]")).click();
            log.info(aliases.get("caam.customerLevelname"));
            selectValueFromDropDownBox(By.xpath("//div[starts-with(@id,'zk_comp') and contains(@id,'-pp')]"), aliases.get("caam.customerLevelname"));
            clickByTextAndTag(planType, "label");
            pause(500);
            clickOnElement(By.cssSelector("#existingbillingaccountpopup"));
            pause(500);

            clickOnLinkFromTableView1("badetaillistbox-rows", 4, 2);
            pause(500);
            selectValueFromDropDownBox(By.cssSelector("#selectplancombo-btn"), By.cssSelector("#selectplancombo-pp"), planName);
            clickOnButton("Add Service Instance");

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            message = message.split("OK")[0];
            response = message;
            log.info(response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    //Created by chetan bhoi
    @Step
    public String transferServiceInstance(String accountNumber, String transferrerAccountNumber, Boolean sameHierarchy) {
        String response = null;
        try {
            searchCustomer(accountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 3);
            pause(500);

            clickOnRightMenu("Billing", "Transfer Service Instance");
            if (sameHierarchy == false) {
                clickByTextAndTag("Other", "label");
                waitUntilVisible(By.cssSelector("#customeraccountimage"));
                clickOnElement(By.cssSelector("#customeraccountimage"));
                switchToWindows(1);
                searchValueFromBrowserWindow("", transferrerAccountNumber);
                switchToWindows(0);
            }
            switchToFrame1("iframe");
            selectValueFromDropDownBox(By.cssSelector("#billingaccountcombo-btn"), By.cssSelector("#billingaccountcombo-pp"), 1);
            selectValueFromDropDownBox(By.cssSelector("#serviceaccountcombo-btn"), By.cssSelector("#serviceaccountcombo-pp"), 0);
            sendTextToElement(By.cssSelector("#reasontextbox"), "test");
            clickOnButton("Transfer Service");

            pause(1000);
            WebElement msgbox = waitUntilVisible(By.cssSelector("#messagediv"));
            response = msgbox.getText();
            log.info("Response message:-" + response);
        } catch (Exception e) {
            response = null;
            log.error(e.getMessage());
        }
        return response;
    }

    //Created by chetan bhoi
    @Step
    public String getBillingAccountNumber1(String customerAccountNumber) {
        String response = null;
        try {
            searchCustomer(customerAccountNumber);
            pause(100);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 3);
            pause(300);
            String BillAcc = getTextFromElement(By.cssSelector("#billingaccnumber"));
            log.info(BillAcc);
            response = BillAcc;
        } catch (Exception e) {
            log.info("Not getting BA number");
        }
        return response;
    }

    //Created by chetan bhoi
    @Step
    public void searchValueFromBrowserWindow(String accountName, String accountNumber) {
        try {
            if (!accountName.equalsIgnoreCase("")) {
                sendTextToElement(By.cssSelector("#txtaccountname"), accountName);
            }
            if (!accountNumber.equalsIgnoreCase("")) {
                sendTextToElement(By.cssSelector("#txtentireaccnumber"), accountNumber);
            }
            clickOnButton("Search");
            clickOnLinkFromTableView1("searchaccountlistbox", 1, 1);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    //Created by chetan bhoi
    @Step
    public String rechargeByPlan(String customerAccountNumber, String rechargePkgName) {

        String responseMessage = null;
        try {
            searchCustomer(customerAccountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 3);
            clickOnRightMenu("Package", "Recharge Account");
            //switchToFrame1("iframe");
            clickOnElement(By.cssSelector("#planrechargeradio-real"));
            pause(1000);
            selectValueFromDropDownBox(By.cssSelector("#rechargepackagecombo-btn"), By.cssSelector("#rechargepackagecombo-pp"), rechargePkgName);
            clickOnElement("#btnplanrecharge");
            pause(1000);

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            log.info(message.split("OK")[0]);
            responseMessage = message;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return responseMessage;
    }

    //Created by chetan bhoi
    @Step
    public String rechargeByVoucher(String customerAccountNumber, String voucherPin) {
        String responseMessage = null;
        try {
            searchCustomer(customerAccountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 3);
            clickOnRightMenu("Package", "Recharge Account");
            //switchToFrame1("iframe");
            sendTextToElement(By.cssSelector("#txtvoucherpin"), voucherPin);
            pause(200);
            clickOnButton("Recharge");
            pause(200);
            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            log.info(message.split("OK")[0]);
            responseMessage = message;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return responseMessage;
    }

    //Created by anju ashok
    @Step
    public void searchCustomer(String accountNumber) {

        pause(500);

        clickOverSubMenu("Account", "Search Customer Account");
        pause(500);
        log.info("Search Customer Account is selected");

        switchToFrame1("iframe");

        // BaseTest.sendTextToElement(By.cssSelector("#txtaccountname"),"Adisha");
        browser.getDriver().findElement(By.cssSelector("#txtentireaccnumber")).sendKeys(accountNumber);
        log.info("Entered account number for search");

        clickElement("#btnsearch");
        pause(1000);

        clickElement("#name01");
        pause(500);
        log.info("Entered into Customer Account");
    }

    /**
     * Returns billing account number of the customer using the customer account number
     *
     * @param custAccountNumber account number of the customer whose billing account number needs to be fetched
     * @return billing account number of the customer
     * @author anuj gupta
     */
    @Step
    public String getBillingAccountNumber(String custAccountNumber) {

        String billingAccountNumber = null;
        try {
            searchCustomer(custAccountNumber);
            pause(500);
            clickElement(locators.get("BA_number"));
            pause(500);
            billingAccountNumber = getTextFromElement(locators.get("billingAccountNo"));

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return billingAccountNumber;
    }

    /**
     * Returns username of the customer using the customer account number
     *
     * @param custAccountNumber account number of the customer whose billing account number needs to be fetched
     * @return username of the customer
     * @author anuj gupta
     */
    @Step
    public String getUserName(String custAccountNumber) {

        String userName = null;
        try {
            searchCustomer(custAccountNumber);
            pause(500);
            clickElement("#serviceinstancename0");
            pause(500);
            userName = getTextFromElement("#servicename");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return userName;
    }

    //Created by anju ashok
    @Step
    public void changePlan(String PlanType, String New_Plan) {
        try {

            pause(4000);

            clickElement("#serviceinstancename0");
            log.info("Plan is selected from CA");
            pause(2000);

            WebElement RHN_Package = findElement(locators.get("RHN_Pacakge_menu"));
            clickElement(RHN_Package);
            pause(2000);

            String Package_submenu = getAlias("Package_SubMenu");
            List<WebElement> SubMenu_Package = findElements1(locators.get("Package_SubMenu"));
            for (int i = 0; i < SubMenu_Package.size(); i++) {
                String SubMenu = SubMenu_Package.get(i).getText();
                if (SubMenu.contains(Package_submenu)) {
                    SubMenu_Package.get(i).click();
                    log.info("Selected SubMenu Package");
                    break;
                }
            }

            pause(2000);
            clickElement("#overridehierarchycheckbox-real");
            log.info("Selected Override Hierarchy");

            clickElement("#business_hierarchy_combo_2-btn");
            log.info("Selected Level 2");

            pause(2000);
            List<WebElement> drp_dwn_leavel2 = findElements1(locators.get("drp_dwn_level2"));
            for (int i = 0; i < drp_dwn_leavel2.size(); i++) {
                String Level2 = drp_dwn_leavel2.get(i).getText();
                if (Level2.equals(PlanType)) {
                    drp_dwn_leavel2.get(i).click();
                    log.info("Selected Level2");
                    break;
                }
            }

            pause(2000);
            List<WebElement> Plan_Type = findElements1(locators.get("Plan_type"));
            for (int i = 0; i < 2; i++) {
                String selected_PT = Plan_Type.get(i).getText();
                if (selected_PT.equals(PlanType)) {
                    Plan_Type.get(i).click();
                    log.info("Plan Type is selected");
                    break;
                }
            }

            scrollToamount();

            clickElement("#cbplancombo-real");
            log.info("Clicked on drop down of new plan");
            pause(2000);
            //  String Plan_name = getAlias("Drp_dwn_new_plan");
            List<WebElement> PlanName = findElements1(locators.get("Drp_dwn_new_plan"));
            for (int i = 0; i < PlanName.size(); i++) {
                String selected_PlanName = PlanName.get(i).getText();
                if (selected_PlanName.equals(New_Plan)) {
                    PlanName.get(i).click();
                    log.info("Plan Name is selected");
                    break;
                }
            }

            WebElement Reason = findElement(locators.get("txt_reason"));
            sendTextToElement(Reason, getAlias("txt_reason"));

            clickElement("#changeserviceplanbtn");
            log.info("Change Service Plan button is clicked");
            pause(2000);


            clickElement("#btn1");

            String Message = findElement1("//*[@id=\"messagediv\"]/span").getText();
            log.info(Message);

            if (Message.contains("Service Plan Changed Successfully.")) {
                log.info("Service Plan Changed Successfully completed.");
            }

        } catch (Exception e) {
            log.error("Service Plan chnage  FAILED.");

        }
    }

    //Created by chetan bhoi
    @Step
    public String changeServicePlan(String customerAccount, String planType, String newPlanName) {
        String responseMsg = null;
        try {
            searchCustomer(customerAccount);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 3);
            clickOnRightMenu("Package", "Change Service Plan");

            String currentChargeType = getTextFromElement(By.cssSelector("#lblchargetype-cell"));
            if (!currentChargeType.equalsIgnoreCase(planType)) {
                // Check 'override hierarchy' check box
                clickOnElement(By.cssSelector("#overridehierarchycheckbox-real"));
                String level1 = browser.getDriver().findElement(By.cssSelector("#business_hierarchy_combo_1-real")).getAttribute("value");
                if (level1.equalsIgnoreCase("Broadband")) {
                    selectValueFromDropDownBox(By.cssSelector("#business_hierarchy_combo_2-btn"), By.cssSelector("#business_hierarchy_combo_2-pp"), planType);
                }
            }

            if (planType.equalsIgnoreCase("Postpaid")) {
                clickOnElement(By.cssSelector("#postpaidcustomerradio-real"));
                if (currentChargeType.equals("Prepaid")) {
                    clickOnElement("#existingbillingaccountpopup");
                    switchToWindows(1);
                    clickOnLinkFromTableView(By.cssSelector("#badetaillistbox"), 1, 1);
                }
            }

            if (planType.equalsIgnoreCase("Prepaid")) {
                clickOnElement(By.cssSelector("#prepaidcustomerradio-real"));
            }

            pause(500);
            selectValueFromDropDownBox(By.cssSelector("#cbplancombo-btn"), By.cssSelector("#cbplancombo-pp"), newPlanName);

            sendTextToElement(By.cssSelector("#reasontextbox"), "ttest");

            clickOnElement(By.cssSelector("#changeserviceplanbtn"));

            pause(500);
            // Click 'OK' on the confirmation message box
            clickElement(locators.get("cancel_btn_click_OK"));

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            log.info(message);
            responseMsg = message.split("OK")[0];

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return responseMsg;
    }

    /**
     * subscribe addone package with dynamic date
     *
     * @param customerAccount
     * @param addonPkg
     * @param startDate
     * @return It will return response message
     * @Author Chetan bhoi
     */
    @Step
    public String subScribeAddonPkg(String customerAccount, String addonPkg, String startDate) {

        String response = null;
        try {
            searchCustomer(customerAccount);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 3);
            clickOnRightMenu("Package", "Add-On Package");

            selectValueFromDropDownBox(By.cssSelector("#selectplancombo-btn"), By.cssSelector("#selectplancombo-pp"), addonPkg);

            if (startDate != "") {
                CommonUtils.sendDate(By.cssSelector("#dtfromdate-real"), startDate);
                CommonUtils.sendDate(By.cssSelector("#billingfromdt-real"), startDate);
            }
            clickOnElement(By.cssSelector("#btnadd"));

            sendTextToElement(By.cssSelector("#reasontextbox"), "Test");

            clickOnButton("Subscribe");

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            message = message.split("OK")[0].trim();
            response = message;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    /**
     * subscribe addon package with today date
     *
     * @param customerAccount
     * @param addonPkg
     * @return It will return response message
     * @Author Chetan bhoi
     */
    @Step
    public String subScribeAddonPkg(String customerAccount, String addonPkg) {
        String response = null;
        try {
            searchCustomer(customerAccount);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox"), 1, 3);
            clickOnRightMenu("Package", "Add-On Package");

            pause(1000);
            selectValueFromDropDownBox(By.cssSelector("#selectplancombo-btn"), By.cssSelector("#selectplancombo-pp"), addonPkg);

            pause(1000);
            clickOnElement(By.cssSelector("#btnadd"));

            pause(1000);
            sendTextToElement(By.cssSelector("#reasontextbox"), "Test");

            pause(1000);
            clickOnButton("Subscribe");

            pause(1000);
            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            message = message.split("OK")[0].trim();
            response = message;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    //Created by anju ashok
    //updated by chetan bhoi
    @Step
    public String unSubscribeAddon(String customerAccountNumber) {
        String responseMessage = null;
        try {
            searchCustomer(customerAccountNumber);

            clickElement("#serviceinstancename0");
            log.info("Plan is selected from CA");
            pause(1000);

            WebElement RHN_Package = findElement(locators.get("RHN_Pacakge_menu"));
            clickElement(RHN_Package);
            pause(1000);

            clickOnElement(locators.get("Remove_Addon"));
            log.info("Remove Addon is selected");

            clickonElement1(locators.get("Delete_Addon"));
            pause(1000);

            scrollToamount();

            String Reason = getAlias("txt_reason");
            //   WebElement txt_reason = findElement1(locators.get("txt_reason"));
            WebElement txt_reason = findElement(locators.get("txt_reason"));
            sendTextToElement(txt_reason, Reason);

            clickOnElement(locators.get("Btn_Remove_Addon"));

            pause(1000);

            String Message = findElement1(locators.get("Message_text")).getText();
            responseMessage = Message;
            log.info(Message);
        } catch (Exception e) {
            log.error("Addon Removed Failed");
        }
        return responseMessage;
    }

    /**
     * Changes credit class of the customer
     *
     * @param custAccountNumber account number of the customer whose plan needs to be changed
     * @param creditClassName   stores name of the new credit class
     * @author anuj gupta
     */
    @Step
    public String changeCreditClass(String custAccountNumber, String creditClassName) {

        String message = null;
        try {
            // Search for a postpaid customer
            searchCustomer(custAccountNumber);

            // Click on billing account
            clickElement(locators.get("click_Billingaccount"));
            clickOnRightMenu("Account", "Change Credit Class");

            // select new credit class from the drop down menu
            selectValueFromDropDownBox(By.cssSelector(locators.get("drp_dwn_credit_class_type_btn")), By.cssSelector(locators.get("drp_dwn_credit_class_type_box")), creditClassName);

            // Enter remarks
            sendTextToElement(By.cssSelector(locators.get("change_reason_txt_area")), aliases.get("txt_remark"));
            clickElement(locators.get("change_credit_class_btn"));

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            message = msgbox.getText();
            message = message.split("OK")[0].trim();
            clickElement(locators.get("btn_ok"));

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return message;

    }// End of changeCreditClass

    @Step
    public String regenerateCancelBill(String CANumber) {
        String response = null;
        try {

            pause(1000);
            searchCustomer(CANumber);

            clickOnElement(locators.get("click_Billingaccount"));

            pause(1500);
            clickOnElement(locators.get("RHN_financialInfo"));

            clickOnElement(locators.get("RHN_FiannacialDetails"));

            pause(1000);
            clickOnElement(locators.get("RHN_RegenerateCancelBill"));
            pause(1000);

            clickOnElement(locators.get("btn_regeneratebillPreview"));
            pause(1500);
            clickOnElement(locators.get("btn_RenegerateBill"));

            String Msg = findElement1(locators.get("Success_Msg_regenerateBill")).getText();
            log.info(Msg);
            response = Msg;
        } catch (Exception e) {
            log.error("Invoice cancellation Failed");
        }
        return response;
    }

    /**
     * Splits charge with another customer
     *
     * @param custAccountNumber      customer account number to search the customer whose charge is being split
     * @param splitWithAccountNumber account number of the customer with whom charge is being split
     * @author anuj gupta
     */
    @Step
    public String splitChargeConfig(String custAccountNumber, String splitWithAccountNumber) {

        String message = null;
        try {
            searchCustomer(custAccountNumber);

            // Click on plan name link
            clickElement(locators.get("plan_name_lnk"));

            // Open split charge screen
            clickOnRightMenu("Billing", "Split Charge Configuration");

            // Click on add button
            clickElement(locators.get("split_charge_add_btn"));

            // Enter details
            sendTextToElement(By.cssSelector(locators.get("split_charge_name_txt")), aliases.get("split_charge_name"));
            selectValueFromDropDownBox(By.cssSelector("#chargetype-btn"), By.cssSelector("#chargetype-pp"), "Usage Charge");
            clickElement(locators.get("split_charge_billing_accnt_ext_lnk"));
            sendTextToElement(By.cssSelector(locators.get("split_charge_threshold_txt")), aliases.get("split_charge_threshold"));

            switchToWindows(1);
            sendTextToElement(By.cssSelector(locators.get("split_with_account_number")), splitWithAccountNumber);
            clickElement(locators.get("search_btn"));
            clickOnElement(By.xpath("//div[text()='" + splitWithAccountNumber + "']"));

            pause(500);
            switchToWindows(0);
            switchToFrame(browser.getDriver().findElement(By.cssSelector(locators.get("split_charge_frame_class"))));
            clickElement(locators.get("split_charge_add_btn"));

            // Check if change action is successful
            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            message = msgbox.getText();
            message = message.split("OK")[0].trim();

            clickElement(locators.get("btn_ok"));

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return message;

    }//end of splitChargeConfig

    /**
     * Checks if a row is present in debit note list of adjustment tab under financial details of the customer
     *
     * @param custAccountNumber Account number of the customer whose adjustment needs to be verified
     * @author anuj gupta
     */
    @Step
    public boolean checkAdjustmentCreated(String custAccountNumber) {

        boolean result = false;
        try {
            searchCustomer(custAccountNumber);
            clickOnElement(By.cssSelector("#bankname1"));
            clickOnRightMenu("Financial Information", "Financial Details");
            clickElement("#adjustmenttab-cave");

            By creditNote = By.xpath("//table[@id='adjustmentlistcreditbox-cave']//a");
            By debitNote = By.xpath("//table[@id='adjustmentdebitgrid-cave']//a");
            if (isElementPresent(creditNote) || isElementPresent(debitNote))
                result = true;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    //Created by anju ashok
    @Step
    public String createCustomerCUG(String fname, String planType, String planName, String billCycle, String CUGType) {
        String CANumber = null;
        try {
            clickOverSubMenu("Account", "Create Customer Account");
            pause(500);
            waitUntilVisible(By.tagName("iframe"));
            switchToFrame1("iframe");
            sendTextToElement(By.cssSelector(locators.get("caam.customerFname.txtbox.css")), fname);
            String lname = "Test_" + System.currentTimeMillis();
            sendTextToElement(By.cssSelector(locators.get("caam.customerLname.txtbox.css")), lname);
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerBillingArea.ddb.css")), By.cssSelector("#billingareacombo-pp"), "Default");
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerCategory.ddb.css")), By.cssSelector("#customercategorycombo-pp"), "Default");
            sendTextToElement(By.cssSelector(locators.get("caam.customerAdd1.txtarea.css")), "test");
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerCity.ddb.css")), By.cssSelector("#citycustomercombo-pp"), aliases.get("caam.customerCity"));
            waitUntilVisible(By.cssSelector("#btnconfigureserviceinstancefront"));
            clickOnButton("Configure Service Details >>");

            waitUntilVisible(By.cssSelector(locators.get("caam.customerLevel.ddb.css")));
            //  String levlValue = aliases.get("caam.customerLevelname");
            String levlValue = "Basic";
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerLevel.ddb.css")), By.cssSelector("#business_hierarchy_combo_1-pp"), levlValue);
            if (levlValue.equals("Broadband")) {
                selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerLevel2.ddb.css")), By.cssSelector("#business_hierarchy_combo_2-pp"), planType);
            }
            waitUntilVisible(By.cssSelector("#accountcurrencycombo-btn"));
            selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerCurrency.ddb.css")), By.cssSelector("#accountcurrencycombo-pp"), aliases.get("caam.currency"));
            waitUntilVisible(By.cssSelector("#postpaidcustomerradio-cnt"));
            clickByTextAndTag(planType, "label");
            pause(500);
            waitUntilVisible(By.cssSelector(locators.get("caam.customerPlanname.ddb.css")));
            if (CUGType.equals("Owner")) {
                selectValueFromDropDownBox(By.cssSelector("#selectplancombo-btn"), By.cssSelector("#selectplancombo-pp"), planName);
                pause(500);
                String CUG_Group_Name = "Test_" + generateRandomString();
                pause(500);
                clickElement("#ownerradio-real");
                //  log.info(""+CUG_Group_Name);
                sendTextToElement(findElement("#cuggroupname"), CUG_Group_Name);
                sendTextToElement(findElement("#cugshortcodevalue"), "70");
                pause(500);
            } else {
                selectValueFromDropDownBox(By.cssSelector("#selectplancombo-btn"), By.cssSelector("#selectplancombo-pp"), planName);
                pause(500);
                String cuggroupnumber = savedValues.get("CUGGroupNumber");
                sendTextToElement(findElement("#cuggroupid"), cuggroupnumber);
                clickElement("#cugshortcodevalue");
                pause(500);

            }
            clickOnButton("Configure Billing Details >>");

            pause(500);
            if (planType.equalsIgnoreCase("Postpaid")) {
                waitUntilVisible(By.cssSelector(locators.get("caam.customerBillCycle.ddb.css")));
                selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerBillCycle.ddb.css")), By.cssSelector("#billingcyclecombo-pp"), billCycle);
                selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerBillDelmode.ddb.css")), By.cssSelector("#billdeliverymodecombo-pp"), "E-Mail");
                selectValueFromDropDownBox(By.cssSelector(locators.get("caam.customerBillFormtype.ddb.css")), By.cssSelector("#cmbbillformattype-pp"), "Detailed");
            }
            clickOnButton("Create Customer");

            WebElement msgbox = waitUntilVisible(By.cssSelector("#messageboxwindow-cave"));
            String message = msgbox.getText();
            log.info(message.split("OK")[0]);
            if (!message.contains("Successfully")) {
                Assert.fail("Customer is not created.");
            }
            WebElement custAccNumber = msgbox.findElement(By.tagName("a"));
            CANumber = custAccNumber.getText();
            custAccNumber.click();

            String[] strlist = message.split("\n");

            String CUGGroupNumber = strlist[7].split(":")[1].trim();
            log.info(CUGGroupNumber);
            String CuGShorCode = strlist[11].split(":")[1].trim();
            log.info(CuGShorCode);
            savedValues.put("CUGGroupNumber", CUGGroupNumber);
            savedValues.put("CuGShorCode", CuGShorCode);

        } catch (Exception e) {
            log.error(e.getMessage());
            takeScreenShot("test");
        }
        return CANumber;
    }

    //Created by anju ashok
    @Step
    public String subscribeAddon(String CANumber, String Addon_Package) {
        String response = null;
        try {

            searchCustomer(CANumber);
            clickElement("#serviceinstancename0");
            log.info("Plan is selected from CA");
            pause(2000);

            WebElement RHN_Package = findElement(locators.get("RHN_Pacakge_menu"));
            clickElement(RHN_Package);
            pause(2000);

            String AddOnPackage = "Add-On Package";
            List<WebElement> SubMenu_Package = findElements1(locators.get("Package_SubMenu"));
            for (int i = 0; i < SubMenu_Package.size(); i++) {
                String SubMenu = SubMenu_Package.get(i).getText();
                if (SubMenu.contains(AddOnPackage)) {
                    SubMenu_Package.get(i).click();
                    log.info("Selected SubMenu Package");
                    break;
                }
            }
            pause(2000);
            selectValueFromDropDownBox(By.cssSelector("#selectplancombo-btn"), By.cssSelector("#selectplancombo-pp"), Addon_Package);
            clickOnElement(locators.get("btn_Add"));
            scrollToamount();
            String reason = getAlias("txt_reason");
            pause(3000);
            sendTextToElement(By.cssSelector(locators.get("txt_reason")), reason);
            clickOnElement(locators.get("Subscribe_button"));
            pause(3000);
            String Message = findElement1(locators.get("Success_Message")).getText();
            pause(3000);
            response = Message;

            if (Message.contains("Addon Subscribed successfully.")) {
                log.info("Addon Subscribed successfully.");

            }
        } catch (Exception e) {
            e.getMessage();
        }
        return response;
    }

    //Created:Anju
    @Step
    public String generateODBill(String CANumber, String endBillDate) {
        String response = null;
        try {
            searchCustomer(CANumber);
            pause(2000);
            clickOnElement(locators.get("click_Billingaccount"));

            pause(1000);
            clickOnElement(locators.get("RHN_financialInfo"));

            clickOnElement(locators.get("tab_generatebill"));

//            clickOnElement(locators.get("click_selectdate"));
//
//            String i = getCurrentDate();
//
//            WebElement ele = BaseTest.getDriver().findElement(By.xpath(".//td[@class='z-calendar-cell z-calendar-weekday z-calendar-selected']"));
//            ele.click();

            CommonUtils.sendDate(By.cssSelector("#lblendbilldateval-real"), endBillDate);

            WebElement Remark = findElement(locators.get("txt_remark"));
            sendTextToElement(Remark, getAlias("txt_remark"));

            clickOnElement(locators.get("btn_previewODBill"));

            clickOnElement(locators.get("btn_GenerateODbill"));

            String Message = findElement(locators.get("Success_Msg_ODBill")).getText();
            response = Message;
            if (Message.equals("Bill successfully generated ")) {
                log.info("Hot Bill generated Successfully.");

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("Hot Bill generated Failed.");
        }
        return response;
    }

    //Created:Anju
    @Step
    public String cancelBill(String CANumber) {
        String reponse = null;
        try {
            searchCustomer(CANumber);
            pause(2000);
            clickOnElement(locators.get("click_Billingaccount"));
            pause(1000);
            clickOnElement(locators.get("RHN_financialInfo"));

            clickOnElement(locators.get("RHN_FiannacialDetails"));

            pause(2000);
            scrollToamount();

            List<WebElement> SelectBill = findElements1(locators.get("btn_ViewBill"));
            SelectBill.get(0).click();

            pause(2000);

            JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
            js.executeScript("window.scrollBy(0,+250)", "");

            WebElement markascancelled = browser.getDriver().findElement(By.xpath("(//span[text()='Mark As Cancelled'])[1]"));
            scrollTo(markascancelled);
            markascancelled.click();

            clickonElement1(".//*[contains(text(),'Mark As Cancelled')]");

            pause(3000);

            ArrayList<String> tab = new ArrayList<>(browser.getDriver().getWindowHandles());
            switchToWindows(1);

            selectValueFromDropDownBox(By.cssSelector(locators.get("btn_reason_Cancellation")), By.cssSelector(locators.get("drp_dwn_reasonCancelBill")), getAlias("Reason_BillCancel"));
            sendTextToElement(findElement(locators.get("txt_Remark_CancelBill")), getAlias("txt_Remark_CancelBill"));
            clickOnElement(locators.get("btn_CancelBill"));
            String MsgCancelBill = findElement(locators.get("markBill_Cancel")).getText();
            clickOnElement("#successok");

            pause(500);
            switchToWindows(0);

            reponse = MsgCancelBill;
            if (MsgCancelBill.contains("cancelled successfully.")) {
                log.info("Invoice is cancelled successfully.");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            log.info("Invoice cancellation Failed");

        }
        return reponse;
    }

    //Created By:Anju
    @Step
    public String verifyAccountStatement(String CA_Number) {
        String response = null;
        try {
            searchCustomer(CA_Number);

            clickOnElement(locators.get("BA_number"));

            clickOnElement(locators.get("Financial_Info_tab"));

            clickOnElement("#accountstatement");

            String amount = findElement("#lblpayments").getText();
            log.info("" + amount);
            response = amount;
            if (!amount.contains("")) {

                log.info("Amount is present in  Account Statement ");
            }

        } catch (Exception e) {
            log.error("Amount billed is improper");
        }
        return response;
    }

    @Step
    public String searchCUGGroup() {
        String response = null;
        try {
            clickOverSubMenu("Account", "Search CUG Group");
            pause(1000);
            switchToFrame1("iframe");

            pause(1000);
            WebElement CUG_GroupNumber = findElement1("//*[@id='cuggroupnumber-cell']/input");
            String cuggroupnumber = savedValues.get("CUGGroupNumber");
            sendTextToElement(CUG_GroupNumber, cuggroupnumber);

            clickElement("#btnsearch");
            pause(1000);
            clickElement("#billingaccountname0");

            String Status = findElement1("//*[@id='statusvaluelabel']").getText();
            response = Status;
            if (Status.contains("Active")) {
                log.info("CUG members added");
            }

        } catch (Exception e) {
            log.error("Unable to search CUG group");
        }
        return response;
    }

    @Step
    public String changeBillCycle(String CANumber, String BillCycle_Name) {
        String response = null;
        try {
            searchCustomer(CANumber);
            clickElement("#bankname0");

            clickOnRightMenu("Account", "Change Billing Cycle");

            selectValueFromDropDownBox(By.cssSelector("#cmbnewbillingcycle-btn"), By.cssSelector("#cmbnewbillingcycle-pp"), BillCycle_Name);
            clickElement("#btnchangebillingcycle");
            pause(2000);
            String Msg = findElement("#messagediv>span").getText();
            response = Msg;
            if (Msg.contains("Billing Cycle Changed Successfully")) {
                log.info("Billing Cycle Changed Successfully");
            }

        } catch (Exception e) {

            log.error("Billing Cycle Change FAIL");
        }
        return response;
    }

    /**
     * @param custAccountNumber
     * @return It will return next bill date of customer
     * @Author Chetan bhoi
     */
    @Step
    public String getCustomerNextBillDate(String custAccountNumber) {
        String nextBillDate = null;
        try {
            searchCustomer(custAccountNumber);
            clickOnLinkFromTableView(By.cssSelector("#accountinfolistbox-body"), 1, 4);
            clickOnRightMenu("Financial Information", "Financial Details");
            String billdate = getTextFromTableView(By.cssSelector("#fullbillviewgrid"), 1, 7);
            nextBillDate = billdate.trim();
            log.info(billdate);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return nextBillDate;
    }

    /**
     * It is used for mark bill as disputed after generating bill invoice
     *
     * @param customerAccountNumber account no. of the customer
     * @return It will return response message
     * @author Chetan Bhoi
     * @Date 13/03/2019
     */
    @Step
    public String markBillAsDisputed(String customerAccountNumber) {
        String response = "";
        try {
            verifyRegularInvoice(customerAccountNumber);
            scrollToamount();
            clickonElement1(By.xpath("(//span[@title='Click to see Invoice Detail.'])[1]"));
            clickonElement1(By.xpath("//span[text()='Mark As Disputed']"));
/*
            WebElement tblbox = browser.getDriver().findElement(By.xpath(".//div[@id='fullbillviewgrid-body']//tr[@id='parent_0']"));
            List<WebElement> listTd = tblbox.findElements(By.tagName("td"));
            listTd.get(6).click();
*/
         /*   scrollToamount();
            List<WebElement> labels = browser.getDriver().findElements(By.xpath(".//*[@class='z-groupbox']//.//span[@class='z-label']"));
            for (WebElement label : labels) {
                String labelText = label.getText();
                if (labelText.equalsIgnoreCase("Mark As Disputed")) {
                    label.click();
                    pause(200);
                    break;
                }
            }
*/

            /*Set <String> handles =browser.getDriver().getWindowHandles();
            Iterator<String> it = handles.iterator();

            while (it.hasNext()) {
                String parent = it.next();
                System.out.println(parent);
                String newwin = it.next();
                System.out.println(newwin);
                browser.getDriver().switchTo().window(parent);
            */
            pause(2000);
            switchToWindows(1);
            String amount = getTextFromElement(By.cssSelector("#lblbillamountval"));
            selectValueFromDropDownBox(By.cssSelector("#cbreason-btn"), By.cssSelector("#cbreason-pp"), 1);
            sendTextToElement(By.cssSelector("#txtdisputedamount"), amount);
            sendTextToElement(By.cssSelector("#txtremarks"), "Test Remark for dispute bill");
            clickOnElement(By.cssSelector("#btndisputebill"));
            pause(500);
            response = getTextFromElement(By.cssSelector("#msgdiv"));
            log.info(response);
            clickOnElement(By.cssSelector("#successok"));
            pause(2000);
            switchToWindows(0);
             /*else {
              //  response = getTextFromElement(By.xpath(".//*[contains(@class,'z-messagebox-window')]//div[@class='z-window-content']"));
            //}*/
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return response;
    }
}
