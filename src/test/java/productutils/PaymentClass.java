package productutils;

import frameworkutils.BrowserFactory;
import frameworkutils.LogManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

public class PaymentClass extends BaseTest {

    CAAMClass caam = new CAAMClass();
    private LogManager log = LogManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();

    //Created by Anju
    public String makePayment(String CA_Number, String PaymentMode) {
        String response1 = null;
        try {
            caam.searchCustomer(CA_Number);
            clickOnElement("#serviceinstancename0");

            Thread.sleep(500);
            String BA_Number = findElement("#billingaccnumber").getText();
            System.out.print("" + BA_Number);
            clickOverSubMenu("Payments", "Make Payment");

            Thread.sleep(2000);
            WebElement iframe = findElement1(locators.get("iframe"));
            switchToFrame(iframe);
            Thread.sleep(1000);

            clickonElement1("//*[@title='Click To Search Billing Account Number']");
            ArrayList<String> tab = new ArrayList<>(browser.getDriver().getWindowHandles());
            browser.getDriver().switchTo().window(tab.get(1));
            log.info(browser.getDriver().getCurrentUrl());
            browser.getDriver().manage().window().maximize();
            Thread.sleep(3000);
            sendTextToElement(findElement("#txtentireaccnumber"), BA_Number);
            Thread.sleep(2000);

            clickOnElement("#btnsearch");

            clickonElement1(".//span[@class='z-listitem-checkable z-listitem-radio']");
            Thread.sleep(2000);

            browser.getDriver().switchTo().window(tab.get(0));

            switchToFrame(iframe);

            clickOnElement(By.cssSelector("#tbpaymentinfo-cnt"));

            Thread.sleep(1000);
            clickOnElement("#paymentmode-btn");

            Thread.sleep(1000);
            selectValueFromDropDownBox(By.cssSelector("#paymentmode-btn"), By.cssSelector("#paymentmode-pp"), PaymentMode);

            sendTextToElement(By.cssSelector("#discription"), "Test");

            Thread.sleep(2000);
            scrollToamount();

            clickOnElement(locators.get("btn_MakePayment"));

            Thread.sleep(3000);
            clickonElement1("//*[@id='winpaymentslip-close']");
            response1 = "Paid";
            System.out.print("Payment done successfully");

        } catch (Exception e) {
            System.out.print("Payment Failed");
        }
        return response1;
    }

    @Step
    //Created by Chetan Bhoi
    public String makePayment1(String billingAccountNumber, String paymentType, String paymentMode) {
        String response = null;
        try {
            clickOverSubMenu("Payments", "Make Payment");
            Thread.sleep(1000);
            switchToFrame1("iframe");
            Thread.sleep(1000);
            selectValueFromDropDownBox(By.cssSelector("#cmbpaycurrency-btn"), By.cssSelector("#cmbpaycurrency-pp"), "INR");
            selectValueFromDropDownBox(By.cssSelector("#paymenttype-btn"), By.cssSelector("#paymenttype-pp"), paymentType);

            Thread.sleep(1000);
            sendTextToElement(findElement("#txtaccountnumber"), billingAccountNumber);
            clickOnElement(By.cssSelector("#btnsearchbills"));

            Thread.sleep(1500);
            //WebElement payDetTab = waitUntilVisible(By.cssSelector("#tbpaymentinfo-cnt"));
            //payDetTab.click();
            clickOnElement(By.cssSelector("#tbpaymentinfo-cnt"));
            Thread.sleep(1000);

            clickOnElement(By.cssSelector("#paymentmode-btn"));
            Thread.sleep(1000);
            //clickOnElement(By.cssSelector("#paymentmode-btn"));
            selectValueFromDropDownBox(By.cssSelector("#paymentmode-btn"), By.cssSelector("#paymentmode-pp"), paymentMode);
            sendTextToElement(By.cssSelector("#discription"), "Test");

            clickOnElement(By.cssSelector("#btnmakepayment"));
            Thread.sleep(1000);

            WebElement closebtnSlip = waitUntilVisible(By.xpath(".//*[@id='winpaymentslip-close']//i"));
            closebtnSlip.click();

            String receiptNumber = getTextFromElement(By.cssSelector("#receiptno"));
            response = receiptNumber;

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }

    //Created by Chetan Bhoi
    @Step
    public String checkPaymentStatus(String paymentReceiptNumber) {
        String response = null;
        try {
            searchPayment(paymentReceiptNumber);
            response = getLinkTextFromTableView1("searchresult", 1, 6);
        } catch (Exception e) {
            log.info("Fail to search payment");
        }
        return response;
    }

    //Created by Chetan Bhoi
    @Step
    public int changePaymentStatus(String paymentReceiptNumber, String currentPayStatus, String newPayStatus) {
        int faileEntries = 999;
        try {
            clickOverSubMenu("Payments", "Manage Payments");
            switchToFrame1("iframe");
            clickOnElement(By.cssSelector("#tbbchangepaymentstatus"));
            Thread.sleep(200);
            sendTextToElement(By.cssSelector("#creditdocno"), paymentReceiptNumber);
            Thread.sleep(200);
            selectValueFromDropDownBox(By.cssSelector("#paymentstatus-btn"), By.cssSelector("#paymentstatus-pp"), currentPayStatus);
            clickOnButton("Search");

            //clickOnLinkFromTableView(By.cssSelector("#searchresult"),1,1);
            clickOnLinkFromTableView1("searchresult", 1, 1);
            selectValueFromDropDownBox(By.cssSelector("#reason-btn"), By.cssSelector("#reason-pp"), "Others");
            sendTextToElement(By.cssSelector("#description"), "test");

            if (newPayStatus.equalsIgnoreCase("Realized")) {
                clickOnButton("Move To Realized");
            }
            if (newPayStatus.equalsIgnoreCase("Billed")) {
                clickOnButton("Move To Billed");
            }
            if (newPayStatus.equalsIgnoreCase("Cancelled")) {
                clickOnButton("Move To Cancelled");
            }
            if (newPayStatus.equalsIgnoreCase("Reverse")) {
                clickOnButton("Reverse Billed Payment");
            }

            Thread.sleep(200);
            clickOnElement(By.cssSelector("#btnsubmit"));

            WebElement msgbox = waitUntilVisible(By.cssSelector("div.z-messagebox"));
            String entries = msgbox.getText().split(":")[3];
            faileEntries = Integer.parseInt(entries.replaceAll(" ", "").trim());
            log.info("Response message:-" + entries);
        } catch (Exception e) {
            log.info("Issue in searching payment list");
        }
        return faileEntries;
    }

    @Step
    private void searchPayment(String paymentReceiptNumber) {
        try {
            clickOverSubMenu("Payments", "Manage Payments");
            Thread.sleep(500);
            switchToFrame1("iframe");
            sendTextToElement(By.cssSelector("#creditdocno"), paymentReceiptNumber);
            Thread.sleep(500);
            clickOnButton("Search");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
