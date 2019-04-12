package productutils;

import frameworkutils.ExcelManager;
import frameworkutils.ReportManager;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import oracle.jdbc.util.Login;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.Map;

public class BRMTestRunner extends ProjectBaseTest {

    private InventoryClass inventory = new InventoryClass();
    private CAAMClass caam = new CAAMClass();
    private PaymentClass payment = new PaymentClass();
    private BillingClass billing = new BillingClass();
    private InfratoolClass infratool = new InfratoolClass();
    private DunningClass dunning = new DunningClass();
    private SystemClass system = new SystemClass();
    private String assertMessage = "DEFAULT ASSERT MESSAGE";
    private LoginPage login = new LoginPage();
    private ExcelManager excel = ExcelManager.getInstance();
    private ReportManager report = ReportManager.getInstance();

//    /**
//     * @author Chetan bhoi
//     */
//    @Feature("BRM")
//    @Story("Create a modem in inventory")
//    @Test
//    public void TestCase_C108349() {
//
//        login.loginOM("anuj", "111");
//        String groupname = aliases.get("inventory.groupname");
//
//        //Create group
//        String response = inventory.createGroup(groupname);
//        if (!response.contains("Successfully")) {
//            if (!response.contains("already")) {
//                Assert.fail(response);
//            }
//        }
//
//        //Create sub group1
//        String subMessage1 = inventory.createSubGroup(groupname, aliases.get("inventory.subgroupname1"));
//        if (!subMessage1.contains("Successfully")) {
//            if (!subMessage1.contains("already")) {
//                Assert.fail(subMessage1);
//            }
//        }
//
//        //Create sub group2
//        String subMessage2 = inventory.createSubGroup(groupname, aliases.get("inventory.subgroupname2"));
//        if (!subMessage2.contains("Successfully")) {
//            if (!subMessage2.contains("already")) {
//                Assert.fail(subMessage2);
//            }
//        }
//    }
//
//    /**
//     * @author Chetan bhoi
//     */
//    @Feature("BRM")
//    @Story("Create a currency of USD and as exchange rate")
//    @Test
//    public void TestCase_C108353() {
//
//        login.loginOM("admin", "sysadmin");
//        String currency1 = "YENS";
//        String currMessage = system.createCurrency(currency1);
//        Assert.assertTrue(currMessage.contains("successfully") || currMessage.contains("already exist"), currMessage);
//
//        String currency2 = "YULZ";
//        String currMessage2 = system.createCurrency(currency2);
//        Assert.assertTrue(currMessage2.contains("successfully") || currMessage2.contains("already exist"), currMessage2);
//
//        String exchangeMessage = system.createExchangeRate(currency1, currency2);
//        Assert.assertTrue(exchangeMessage.contains("successfully") || exchangeMessage.contains("Please Select value") || exchangeMessage.contains("value overlap"), exchangeMessage);
//    }
//
//    /**
//     * @author Chetan bhoi
//     */
//    @Feature("BRM")
//    @Story("Create postpaid customer in BRM for Daily_1.5Rs/min package subscribe addon package change plan from postpaid to prepaid and Verify Prepaid Invoice and make the payment then cancel payment check the status")
//    @Test
//    public void TestCase_C108373() {
//
//        login.loginOM("test01", "1235");
//        //Create postpaid cutomer
//        String custAccountNumber = caam.createCustomer("chetan", "Postpaid", aliases.get("caam.planName"), aliases.get("caam.billingCyclename"), "");
//        Assert.assertNotNull(custAccountNumber, "Customer account is not created");
//
//        //Change status to active
//        String message = caam.changeStatus(custAccountNumber, "Active");
//        Assert.assertTrue(message.contains("Successfully"), "Customer status is not changed");
//
//        //Verify sales invoice after activate customer
//        String salesIvoice = caam.verifySalesInvoice(custAccountNumber);
//        Assert.assertNotNull(salesIvoice, "Sales invoice is not generated.");
//
//        //Subscribe addon
//        String response = caam.subScribeAddonPkg(custAccountNumber, aliases.get("caam.addonPkgName"));
//        Assert.assertTrue(response.contains("successfully"), "Addon package is successfully subscribed.");
//
//        //Get billing account number of the customer
//        String billingAccountNumber = caam.getBillingAccountNumber1(custAccountNumber);
//        Assert.assertNotNull(billingAccountNumber, "Error in billing account number");
//
//        //Make payment
//        String paymentnumber = payment.makePayment1(billingAccountNumber, "Debit Payment", "Cash");
//        Assert.assertNotNull(paymentnumber, "Payment is not successfully done");
//
//        //Check payment status as billed
//        String status = payment.checkPaymentStatus(paymentnumber);
//        Assert.assertEquals(status, "Billed", "Payment status is not true");
//
//        //Change service plan
//        String changePlanMessage = caam.changeServicePlan(custAccountNumber, "Prepaid", aliases.get("caam.prepaid.newPlan"));
//        Assert.assertTrue(changePlanMessage.contains("Successfully"), changePlanMessage);
//
//        //Change payment status as cancelled
//        int faileEntries = payment.changePaymentStatus(paymentnumber, "Billed", "Cancelled");
//        Assert.assertEquals(faileEntries, 0, "Payment status is not changed successfully   ");
//
//        //Verify payment status as cancelled
//        String cancelledStatus = payment.checkPaymentStatus(paymentnumber);
//        Assert.assertEquals(cancelledStatus, "Cancelled", "Payment status is not true");
//    }
//
//    /**
//     * @author Chetan bhoi
//     */
//    // PCC integration is required
//    @Feature("BRM")
//    @Story("Create postpaid customer with CDMA_0.5Rs/min_Dataat_512kbps and change plan postpaid to prepaid and generate the prepaid statement, recharge using voucher")
//    @Test
//    public void TestCase_C108374() {
//
//        login.loginOM("darshan", "111");
//        //create postpaid customer
//        String custAccountNumber = caam.createCustomer("chetan", "Postpaid", aliases.get("caam.planName"), aliases.get("caam.billingCyclename"), "");
//        Assert.assertNotNull(custAccountNumber, "Customer account is not created");
//
//        //change customer status to active
//        String message = caam.changeStatus(custAccountNumber, "Active");
//        Assert.assertTrue(message.contains("Successfully"), "Customer status is not changed");
//
//        //verify sales invoice
//        String salesIvoice = caam.verifySalesInvoice(custAccountNumber);
//        Assert.assertNotNull(salesIvoice, "Sales invoice is not generated.");
//
//        //get billing account number
//        String billingAccountNumber = caam.getBillingAccountNumber1(custAccountNumber);
//        Assert.assertNotNull(billingAccountNumber, "Error in billing account number");
//
//        //Make payment
//        String paymentnumber = payment.makePayment1(billingAccountNumber, "Debit Payment", "Cash");
//        Assert.assertNotNull(paymentnumber, "Payment is not successfully done");
//
//        //Verify payment status as billed
//        String status = payment.checkPaymentStatus(paymentnumber);
//        Assert.assertEquals(status, "Billed", "Payment status is not true");
//
//        //Change service plan to prepaid
//        String changePlanMessage = caam.changeServicePlan(custAccountNumber, "Prepaid", aliases.get("caam.prepaid.newPlan"));
//        Assert.assertTrue(changePlanMessage.contains("Successfully"), changePlanMessage);
//
//        //Recharge using voucher
//        String voucherPin = CommonUtils.getVoucherPin();
//        String responseMessage = caam.rechargeByVoucher(custAccountNumber, voucherPin);
//        Assert.assertTrue(responseMessage.contains("Successful"), responseMessage);
//
//        //Verify recharge balance
//
//        //Generate prepaid statement
//        String nextBillDate = caam.getCustomerNextBillDate(custAccountNumber);
//        infratool.runPrepaidStatementGenerationService(nextBillDate);
//
//        String serviceStatus = infratool.verifyPrepaidStatementGenerationService();
//        Assert.assertTrue(serviceStatus.equalsIgnoreCase("Completed"), "Invalid status is:" + serviceStatus);
//
//        //Verify prepaid invoice
//    }
//
//    /**
//     * @author Chetan bhoi
//     */
//    // requires backdated customer
//    @Feature("BRM")
//    @Story("Verify postpaid customer with dunning flow and make it PTP on reactivate it and terminate with subscribe addon action.")
//    @Test
//    public void TestCase_C108375() {
//
//        // Create backdated postpaid customer and create its debit note using api
//        String customerAccountNumber = "ELITE010663";
//
//        // Run bill generation service:
//        String runServMsg = billing.runBillGenerationService(customerAccountNumber);
//        Assert.assertTrue(runServMsg.contains("successfully"), runServMsg);
//
//        billing.verifyBillgenerationService();
//
//        //Dunning Unit Collector Service
//        String billingAccountNumber = caam.getBillingAccountNumber(customerAccountNumber);
//        String invoiceNo = caam.verifyRegularInvoice(customerAccountNumber);
//
//        String response = dunning.createDunningUnit(billingAccountNumber, invoiceNo);
//        Assert.assertTrue(response.contains("uccessful"), response);
//
//        //Run Process Queue service
//        String processMessage = infratool.runProcessQueue();
//        Assert.assertTrue(processMessage.contains("uccessful"), processMessage);
//
//        //verify it is in dunning or not
//        String dunStatus = caam.checkInvoiceInDunningStatus(customerAccountNumber);
//        Assert.assertEquals(dunStatus, "Yes", "Customer invoice is not in dunning status.");
//
//        //Promise to pay
//        String dueDate = caam.getCustomerNextBillDate(customerAccountNumber);
//        String ptpMessage = caam.promiseToPayRequest(customerAccountNumber, dueDate);
//        Assert.assertTrue(ptpMessage.contains("successfully"), "Promise To Pay request is not done successfully.");
//
//        //make payment to reactivate
//        String paymentReceipt = payment.makePayment1(billingAccountNumber, "Debit Payment", "Cash");
//        Assert.assertNotNull(paymentReceipt, "Make payment is not successfully done.");
//
//        String currPayStatus = caam.verifyInvoiceStatus(customerAccountNumber);
//        Assert.assertEquals(currPayStatus, "Paid", "Found invalid status of invoice is:" + currPayStatus);
//
//        //change status to suspend-permenent
//        String statusMsg = caam.changeStatus(customerAccountNumber, "Suspend");
//        Assert.assertTrue(statusMsg.contains("Successfully"), statusMsg);
//
//        //change status to terminate
//        statusMsg = caam.changeStatus(customerAccountNumber, "Terminate");
//        Assert.assertTrue(statusMsg.contains("Successfully"), statusMsg);
//
//        //verify subscribe addone option is availble or not
//    }
//
//    /**
//     * @author Chetan bhoi
//     */
//    // requires backdated customer
//    @Feature("BRM")
//    @Story("After customer subscription of basic and addon, mark stop billing for 10 days then mark start billing and generate regular invoice at the end of bill cycle date, mark it as disputed")
//    @Test
//    public void TestCase_C108377() {
//
//        //Create backdated postpaid customer with basic plan
//        String customerAccountNumber = "ELITE010536";
//
//        //Subscribe addon plan
//        String subMessage = caam.subScribeAddonPkg(customerAccountNumber, "MON_Addon", aliases.get("billing.startbill.date"));
//        Assert.assertTrue(subMessage.contains("successfully"), subMessage);
//
//        //Mark Stop Bill
//        String stopBillMessage = billing.markStopBill(customerAccountNumber, aliases.get("billing.stopbill.date"));
//        Assert.assertTrue(stopBillMessage.contains("successfully"), stopBillMessage);
//
//        //Start billing after 10 days
//        String startBillMessage = billing.markStartBill(customerAccountNumber, aliases.get("billing.stopbill.date"));
//        Assert.assertTrue(startBillMessage.contains("successfully"), startBillMessage);
//
//        //Generate regular invoice
//        String servMsg = billing.runBillGenerationService(customerAccountNumber);
//        Assert.assertTrue(servMsg.contains("successfully"), servMsg);
//
//        //Verify bill
//        String invStatus = caam.verifyRegularInvoice(customerAccountNumber);
//        Assert.assertNotNull(invStatus, "Not found regular invoice of the customer");
//
//        //Mark bill as disputed
//        String markMsg = caam.markBillAsDisputed(customerAccountNumber);
//        Assert.assertTrue(markMsg.contains("Successfully"), markMsg);
//    }
//
//    /**
//     * @author Chetan bhoi
//     */
//    // PCC integration is required
//    @Feature("BRM")
//    @Story("Create Prepaid Account with plan then subscribe addon after activation of customer status then unsubscribe addon then recharge using plan")
//    @Test
//    public void TestCase_C113321() {
//
//        //create customer
//        String custAccountNumber = caam.createCustomer("chetan", "Prepaid", aliases.get("caam.prepaid.basic.plan"), aliases.get("caam.billingCyclename"), "");
//        Assert.assertNotNull(custAccountNumber, "Customer account is not created");
//
//        //change customer status to active
//        String message = caam.changeStatus(custAccountNumber, "Active");
//        Assert.assertTrue(message.contains("Successfully"), "Customer status is not changed");
//
//        //subscribe addon
//        String response = caam.subScribeAddonPkg(custAccountNumber, "prepaid_addon");
//        Assert.assertTrue(response.contains("successfully"), "Addon package is not successfully subscribed.");
//
//        //unsubscribe addon
//        String resMessage = caam.unSubscribeAddon(custAccountNumber);
//        Assert.assertEquals(resMessage, "Addon Removed Successfully", resMessage);
//
//        //recharge by plan
//        String rechargeResponse = caam.rechargeByPlan(custAccountNumber, "Recharge_110");
//        Assert.assertTrue(rechargeResponse.contains("Successful"), rechargeResponse);
//
//        //Verify monetory balance
//        //Assert.fail("Partially done. Remaining to verify monetory balance");
//    }
//
//    @Ignore
//    @Feature("BRM")
//    @Story("Verify the expiry date of a customer with dbr package subscription")
//    @Test
//    public void TestCase_C108387() {
//
//        Assert.fail("Pending: DDR package");
//    }
//
//    /**
//     * @author Chetan bhoi
//     */
//    // PCC integration is required
//    @Feature("BRM")
//    @Story("Change plan from prepaid with customer having addon to prepaid of customer and recharge using plan")
//    @Test
//    public void TestCase_C108388() {
//
//        //create customer
//        String custAccountNumber = caam.createCustomer("chetan", "Prepaid", aliases.get("caam.prepaid.basic.plan"), aliases.get("caam.billingCyclename"), "");
//        Assert.assertNotNull(custAccountNumber, "Customer account is not created");
//
//        //change customer status to active
//        String message = caam.changeStatus(custAccountNumber, "Active");
//        Assert.assertTrue(message.contains("Successfully"), "Customer status is not changed");
//
//        //change service plan with prepaid plan
//        String changePlanMessage = caam.changeServicePlan(custAccountNumber, "Prepaid", aliases.get("caam.prepaid.newPlan"));
//        Assert.assertTrue(changePlanMessage.contains("Successfully"), changePlanMessage);
//
//        //Recharge using plan
//        String rechargeResponse = caam.rechargeByPlan(custAccountNumber, "Recharge_110");
//        Assert.assertTrue(rechargeResponse.contains("Successful"), rechargeResponse);
//    }
//
//    /**
//     * @author Chetan bhoi
//     */
//    @Feature("BRM")
//    @Story("Subscribe an addon when customer in registered status and change status to termniate")
//    @Test
//    public void TestCase_C108390() {
//
//        //create customer in registered mode
//        String custAccountNumber = caam.createCustomer("chetan", "Prepaid", aliases.get("caam.prepaid.basic.plan"), aliases.get("caam.billingCyclename"), "");
//        Assert.assertNotNull(custAccountNumber, "Customer account is not created");
//
//        //subscribe addon package
//        String response = caam.subScribeAddonPkg(custAccountNumber, "prepaid_addon");
//        Assert.assertTrue(response.contains("successfully"), "Addon package is successfully subscribed.");
//
//        //change status as terminate or suspend
//        String message = caam.changeStatus(custAccountNumber, "Suspend");
//        Assert.assertTrue(message.contains("Successfully"), "Customer status is not changed");
//
//        //Verify service status
//        String serviceStatus = caam.getServiceStatus(custAccountNumber);
//        Assert.assertEquals(serviceStatus, "Suspend Permanent", "Found invalid status of customer");
//    }
//
//    @Ignore("Pending: Related to OCS")
//    @Feature("BRM")
//    @Story("Create a customer with Prepaid_2GB_PerDay package do the usage of the 2GB and throttle down to 64kbps speed")
//    @Test
//    public void TestCase_C108393() {
//
//        Assert.fail("Pending, Related to OCS");
//    }
//
//    /**
//     * @author anju
//     */
//    // requires backdated customer
//    @Feature("BRM")
//    @Story("Add the Postpaid customer created with Daily_600mins_free package to dunning using auto flow(configured) and reactivate the customer then generate the OD bill mark it as cancelled and regenerate the cancelled bill and make payment")
//    @Test
//    public void TestCase_C108376() {
//
//        String CA_Number = "ELITE010576";
//
//        String result = caam.changeCreditClass(CA_Number, "D1Class");
//        Assert.assertNotNull(result, "Credit class failed to attach with customer account");
//
//        infratool.runDunningUnitCollectorService("D1Class");
//        Assert.assertNotNull(result, "Credit class failed to attach with customer account");
//
//        infratool.runDunningUnitCollectorService("Auto_cc");
//        infratool.runEntryQueueService();
//        infratool.runProcessQueue();
//
//        String runServMsg = billing.runBillGenerationService(CA_Number);
//        Assert.assertTrue(runServMsg.contains("successfully"), runServMsg);
//
//        billing.verifyBillgenerationService();
//
//        String InvoiceName = billing.check_invoicae_generated_at_billingaccount(CA_Number);
//        Assert.assertNotNull(InvoiceName, "Bill not generated.");
//
//        //Get billing account number of the customer
//        String billingAccountNumber = caam.getBillingAccountNumber1(CA_Number);
//        Assert.assertNotNull(billingAccountNumber, "Unable to fetch billing account number");
//
//        String PaymentStatus = payment.makePayment1(billingAccountNumber, "Debit Payment", "Cash");
//        Assert.assertNotNull(PaymentStatus, "Payment not done.");
//
//        String response = caam.generateODBill(CA_Number, CommonUtils.getDateByDayOffset(0));
//        Assert.assertTrue(response.contains("successfully"), "OD Bill not Generated.");
//
//        String Status = caam.cancelBill(CA_Number);
//        Assert.assertTrue(Status.contains("successfully"), "Unalbe to cancel the bill");
//
//        String message = caam.regenerateCancelBill(CA_Number);
//        Assert.assertTrue(message.contains("successfully"), "Unable to regenerate cancelled bill");
//
//        String PaymentStatus1 = payment.makePayment1(billingAccountNumber, "Debit Payment", "Cash");
//        Assert.assertTrue(PaymentStatus1.contains("Paid"), "Payment not done.");
//    }
//
//    /**
//     * @author anju
//     */
//    @Feature("BRM")
//    @Story("Subscribe an addon when customer in registered status and verify the account statement")
//    @Test
//    public void TestCase_C108389() {
//
//        //Manually enter the addon package name and basic  package name
//        String CustAccountNumber = caam.createCustomer("Test Prepaid", "Prepaid", "PrePaid_Basic", aliases.get("caam.billingCyclename"), "");
//        Assert.assertNotNull(CustAccountNumber, "Customer account is not created");
//
//        String AddonStatus = caam.subscribeAddon(CustAccountNumber, "Test_Addon_2");
//        Assert.assertTrue(AddonStatus.contains("successfully"), "Unable to subscribe Addon.");
//
//        String status = caam.changeStatus(CustAccountNumber, "Active");
//        Assert.assertTrue(status.contains("Successfully"), "Unable to subscribe Addon.");
//
//        caam.verifyAccountStatement(CustAccountNumber);
//    }
//
//    /**
//     * @author anju
//     */
//    @Feature("BRM")
//    @Story("Time based postpaid plan with Multiple Rating Policies to support CUG customer")
//    @Test
//    public void TestCase_C108381() {
//
//        String CustAccountNumber = caam.createCustomerCUG("CUG_Owner", "PostPaid", "CUG_Pack", aliases.get("caam.billingCyclename"), "Owner");
//        Assert.assertNotNull(CustAccountNumber, "Customer account is not created");
//
//        String Msg = caam.changeStatus(CustAccountNumber, "Active");
//        Assert.assertTrue(Msg.contains("Successfully"), "Unable to change the status.");
//
//        String Custnumber2 = caam.createCustomerCUG("CUG_Member", "PostPaid", "CUG_Pack", aliases.get("caam.billingCyclename"), "Member");
//        Assert.assertNotNull(Custnumber2, "Customer account is not created");
//
//        String Msg1 = caam.changeStatus(Custnumber2, "Active");
//        Assert.assertTrue(Msg1.contains("Successfully"), "Unable to change the status.");
//
//        String Status = caam.searchCUGGroup();
//        Assert.assertTrue(Status.contains("Active"), "Unable to search CUG group.");
//    }
//
//    /**
//     * @author anju
//     */
//    @Feature("BRM")
//    @Story("Refund amount generation of custome whose payment is successfully comapleted,then status change to suspend permanent")
//    @Test
//    public void TestCase_C108391() {
//
//        String CustAccountNumber = caam.createCustomer("Test Refund", "Postpaid", aliases.get("caam.planName"),
//                aliases.get("caam.billingCyclename"), "");
//        Assert.assertNotNull(CustAccountNumber, "Customer account is not created");
//
//        String Msg = caam.changeStatus(CustAccountNumber, "Active");
//        Assert.assertTrue(Msg.contains("Successfully"), "Unable to change the status.");
//
//        // Fetch billing accout number
//        String billingAccountNumber = caam.getBillingAccountNumber(CustAccountNumber);
//        assertMessage = "Unable to fetch billing account number.";
//        Assert.assertNotNull(billingAccountNumber, assertMessage);
//
//        String Message = payment.makePayment1(billingAccountNumber, "Debit Payment", "Cash");
//        Assert.assertTrue(Message.contains("successfully"), "Unable to make the payment.");
//
//        String Status = caam.changeStatus(CustAccountNumber, "Suspend");
//        Assert.assertTrue(Status.contains("Successfully"), "Unable to change the status.");
//
//        String runServMsg = billing.runBillGenerationService(CustAccountNumber);
//        Assert.assertTrue(runServMsg.contains("successfully"), runServMsg);
//
//        billing.verifyBillgenerationService();
//        String InvoiceName = billing.check_invoicae_generated_at_billingaccount(CustAccountNumber);
//        Assert.assertNotNull(InvoiceName, "Bill not generated.");
//    }
//
//    /**
//     * @author anju
//     */
//    // requries backdated customer from api createCust
//    @Feature("BRM")
//    @Story("Subscriber subscribes an EMI for phone at the mid of the month and after few months of payment, customer violates the rule of contract and generate bill")
//    @Test
//    public void TestCase_C108382() {
//
//        //Backadetd customer
//        String CustAccountNumber = "ELITE010580";//getAlias("caam.bd.customer2");
//
//        //  caam.subscribeAddon(CustAccountNumber,"PenaltyPack_Updated");
//        String runServMsg = billing.runBillGenerationService(CustAccountNumber);
//        Assert.assertTrue(runServMsg.contains("successfully"), runServMsg);
//
//        billing.verifyBillgenerationService();
//        String InvoiceName = billing.check_invoicae_generated_at_billingaccount(CustAccountNumber);
//        Assert.assertNotNull(InvoiceName, "Bill not generated.");
//
//        String Status = caam.changeStatus(CustAccountNumber, "Suspend", "passdate");
//        Assert.assertTrue(Status.contains("Successfully"), "Unable to change the status.");
//
//        String runServMsg2 = billing.runBillGenerationService(CustAccountNumber);
//        Assert.assertTrue(runServMsg2.contains("successfully"), runServMsg);
//
//        billing.verifyBillgenerationService();
//        String InvoiceName1 = billing.check_invoicae_generated_at_billingaccount(CustAccountNumber);
//        Assert.assertNotEquals(InvoiceName, InvoiceName1, "Bill not generated.");
//    }
//
//    /**
//     * @author anju
//     */
//    // requires backdated customer fromp api createCustomer
//    @Feature("BRM")
//    @Story("Customer creation backdated,change bill cycle,generate bill twice")
//    @Test
//    public void TestCase_C108383() {
//
//        String CustAccountNumber = "ELITE010584";
//
//        String runServMsg = billing.runBillGenerationService(CustAccountNumber);
//        Assert.assertTrue(runServMsg.contains("successfully"), runServMsg);
//
//        billing.verifyBillgenerationService();
//        String InvoiceName1 = billing.check_invoicae_generated_at_billingaccount(CustAccountNumber);
//        Assert.assertNotNull(InvoiceName1, "Bill not generated.");
//
//        String Status = caam.changeBillCycle(CustAccountNumber, "BillCycle_15");
//        Assert.assertTrue(Status.contains("Successfully"), "Unable to change the status.");
//
//        String runServMsg1 = billing.runBillGenerationService(CustAccountNumber);
//        Assert.assertTrue(runServMsg1.contains("successfully"), runServMsg);
//
//        billing.verifyBillgenerationService();
//        String InvoiceName2 = billing.check_invoicae_generated_at_billingaccount(CustAccountNumber);
//        Assert.assertNotEquals(InvoiceName1, InvoiceName2, "Bill not generated.");
//    }
//
//    /**
//     * @author anuj gupta
//     */
//    @Feature("BRM")
//    @Story("Create a postpaid customer, change plan to a different postpaid plan and generate bill")
//    @Test
//    public void TestCase_C108385() {
//
//        // Create the customer
//        String custAccountNumber = caam.createCustomer(aliases.get("caam.customerFname"), aliases.get("caam.planType"), aliases.get("caam.planName"), aliases.get("caam.billingCyclename"), aliases.get("caam.creditClassname"));
//        assertMessage = "Customer account is not created";
//        Assert.assertNotNull(custAccountNumber, assertMessage);
//
//        // Fetch billing account number
//        String billingAccountNumber = caam.getBillingAccountNumber(custAccountNumber);
//        assertMessage = "Unable to fetch billing account number.";
//        Assert.assertFalse(billingAccountNumber.isEmpty(), assertMessage);
//
//        // Change the status to active
//        String changeStatusMessage = caam.changeStatus(custAccountNumber, "Active");
//        assertMessage = "Customer status is not changed";
//        Assert.assertTrue(changeStatusMessage.contains("Successfully"), assertMessage);
//
//        // Change the plan to postpaid
//        String changePlanMessage = caam.changeServicePlan(custAccountNumber, "Postpaid", "Full_TalkTime_121");
//        assertMessage = "Unable to change plan from postpaid to postpaid";
//        Assert.assertTrue(changePlanMessage.contains("Successfully"), assertMessage);
//
//        // Make the payment
//        String makePaymentMessage = payment.makePayment1(billingAccountNumber, "Debit Payment", "Cash");
//        assertMessage = "Unable to make payment.";
//        Assert.assertNotNull(makePaymentMessage, assertMessage);
//
//        // Generate bill
//        String generateBill = billing.runBillGenerationService(custAccountNumber);
//        assertMessage = "Unable to generate bill";
//        Assert.assertTrue(generateBill.contains("successful"), assertMessage);
//
//        billing.verifyBillgenerationService();
//        String billResponse = billing.check_invoicae_generated_at_billingaccount(custAccountNumber);
//        assertMessage = "Bill not generated successfully.";
//        Assert.assertNotNull(billResponse, assertMessage);
//    }
//
//    /**
//     * @author anuj gupta
//     */
//    @Feature("BRM")
//    @Story("Create a customer using Data_1GB_daily_512kbps package with autosubscription of an addon Postpaid_Addon_500MB and perfrom split bill amongs the customer generate the bill for next month change the bill cycle and trasfer transfer SI and generate bill")
//    @Test
//    public void TestCase_C108378() {
//
//        // Create Customer
//        String custAccountNumber = caam.createCustomer(aliases.get("caam.customerFname"), aliases.get("caam.planType"), aliases.get("caam.planName"), aliases.get("caam.billingCyclename"), aliases.get("caam.creditClassname"));
//        assertMessage = "Customer account is not created";
//        Assert.assertNotNull(custAccountNumber, assertMessage);
//
//        // Change the status to active
//        String changeStatusMessage = caam.changeStatus(custAccountNumber, "Active");
//        assertMessage = "Customer status is not changed";
//        Assert.assertTrue(changeStatusMessage.contains("Successfully"), assertMessage);
//
//        // Create Customer
//        String splitWithCustAccountNumber = caam.createCustomer(aliases.get("caam.customerFname"), aliases.get("caam.planType"), aliases.get("caam.planName"), aliases.get("caam.billingCyclename"), aliases.get("caam.creditClassname"));
//        assertMessage = "Customer account is not created";
//        Assert.assertNotNull(splitWithCustAccountNumber, assertMessage);
//
//        // Change the status to active
//        String changeStatusMessage2 = caam.changeStatus(splitWithCustAccountNumber, "Active");
//        assertMessage = "Customer status is not changed";
//        Assert.assertTrue(changeStatusMessage2.contains("Successfully"), assertMessage);
//
//        // Fetch billing accout number
//        String splitWithBillingAccountNumber = caam.getBillingAccountNumber(splitWithCustAccountNumber);
//        assertMessage = "Unable to fetch billing account number.";
//        Assert.assertNotNull(splitWithBillingAccountNumber, assertMessage);
//
//        // Split the charge
//        String splitChargeMessage = caam.splitChargeConfig(custAccountNumber, splitWithBillingAccountNumber);
//        assertMessage = "Charge split not done successfully.";
//        Assert.assertTrue(splitChargeMessage.contains("Successfully"), assertMessage);
//
//        // Generate bill
//        String generateBill = billing.runBillGenerationService(custAccountNumber);
//        assertMessage = "Unable to generate bill";
//        Assert.assertTrue(generateBill.contains("successful"), assertMessage);
//
//        billing.verifyBillgenerationService();
//
//        String billResponse = billing.check_invoicae_generated_at_billingaccount(custAccountNumber);
//        assertMessage = "Bill is not generated successfully.";
//        Assert.assertNotNull(billResponse, assertMessage);
//    }
//
//    /**
//     * @author anuj gupta
//     */
//    @Feature("BRM")
//    @Story("Inventory Batch generation")
//    @Test
//    public void TestCase_C108350() {
//
//        // Create a warehouse
//        String filePath = System.getProperty("user.dir") + "/src/main/resources/BULK_INVENTORY_STATUS.csv";
//        String warehouseName = "Automated_warehouse_7";
//
//        String createWarehouseMessage = inventory.createWareHouse(warehouseName, aliases.get("warehouseEmail"), aliases.get("warehouseCountry"), aliases.get("warehouseState"), aliases.get("warehouseCity"));
//        assertMessage = "warehouse is not created successfully.";
//        Assert.assertTrue(createWarehouseMessage.contains("Successfully") || createWarehouseMessage.contains("Already Exist"), assertMessage);
//
//        // Generate Inventory
//        String batchNumber = inventory.generateInventoryBatch(warehouseName, filePath, aliases.get("inventoryGroupName"), aliases.get("inventorySubGroupName"), aliases.get("warehouseName"));
//        assertMessage = "Inventory batch is not generated successfully.";
//        Assert.assertTrue(batchNumber.contains("NBT"), assertMessage);
//
//        // Upload batch file
//        String uploadFileMessage = infratool.uploadBatchFile(filePath, aliases.get("groupAction"), aliases.get("bulkAction"), aliases.get("adapter"));
//        assertMessage = "batch file is not uploaded successfully.";
//        Assert.assertTrue(uploadFileMessage.contains("successfully"), assertMessage);
//
//        // Process batch
//        String processBatchMessage = infratool.processBatch(aliases.get("groupAction"), aliases.get("bulkAction"));
//        assertMessage = "batch is not processed successfully.";
//        Assert.assertTrue(processBatchMessage.contains("Process is running"), assertMessage);
//
//        // View inventory details to check status is Available or not.
//        String inventoryStatus = inventory.viewInventoryDetails(batchNumber, aliases.get("inventoryGroupName"), aliases.get("inventorySubGroupName"));
//        assertMessage = "Invalid status found: " + inventoryStatus;
//        Assert.assertFalse(inventoryStatus.isEmpty(), assertMessage);
//    }
//
//    /**
//     * @author anuj gupta
//     */
//    @Feature("BRM")
//    @Story("Customer creation ,transfer SI and generate Bill")
//    @Test
//    public void TestCase_C110594() {
//
//        // Create a customer
//        String custAccountNumber = caam.createCustomer(aliases.get("caam.customerFname"), aliases.get("caam.planType"), aliases.get("caam.planName"), aliases.get("caam.billingCyclename"), aliases.get("caam.creditClassname"));
//        assertMessage = "Customer account is not created";
//        Assert.assertNotNull(custAccountNumber, assertMessage);
//
//        // Change the status to active
//        String changeStatusMessage = caam.changeStatus(custAccountNumber, "Active");
//        assertMessage = "Customer status is not changed";
//        Assert.assertTrue(changeStatusMessage.contains("Successfully"), assertMessage);
//
//        //Get billing account number of the customer
//        String billingAccountNumber = caam.getBillingAccountNumber(custAccountNumber);
//        assertMessage = "Error in billing account number";
//        Assert.assertNotNull(billingAccountNumber, assertMessage);
//
//        //Make payment
//        String paymentnumber = payment.makePayment1(billingAccountNumber, "Debit Payment", "Cash");
//        assertMessage = "Payment is not successfully done";
//        Assert.assertNotNull(paymentnumber, assertMessage);
//
//        // Create customer for transfer SI
//        String transferToCustAccountNumber = caam.createCustomer(aliases.get("caam.customerFname"), aliases.get("caam.planType"), aliases.get("caam.planName"), aliases.get("caam.billingCyclename"), aliases.get("caam.creditClassname"));
//        assertMessage = "Customer account is not created";
//        Assert.assertNotNull(transferToCustAccountNumber, assertMessage);
//
//        // Change the status to active
//        String changeStatusMessage2 = caam.changeStatus(transferToCustAccountNumber, "Active");
//        assertMessage = "Customer status is not changed";
//        Assert.assertTrue(changeStatusMessage2.contains("Successfully"), assertMessage);
//
//        // Transfer Service Instance
//        String transferSIMessage = caam.transferServiceInstance(custAccountNumber, transferToCustAccountNumber, false);
//        assertMessage = "Unable to transfer Service Instance.";
//        Assert.assertTrue(transferSIMessage.contains("Successfully"), assertMessage);
//
//        // Check if adjustment is created or not
//        boolean adjustmentValue = caam.checkAdjustmentCreated(transferToCustAccountNumber);
//        assertMessage = "Unable to check if adjustment";
//        Assert.assertTrue(adjustmentValue, assertMessage);
//
//        // Generate bill
//        String generateBill = billing.runBillGenerationService(transferToCustAccountNumber);
//        assertMessage = "Unable to generate bill";
//        Assert.assertTrue(generateBill.contains("successful"), assertMessage);
//
//        billing.verifyBillgenerationService();
//
//        String billResponse = billing.check_invoicae_generated_at_billingaccount(transferToCustAccountNumber);
//        assertMessage = "Unable to generate bill";
//        Assert.assertNotNull(billResponse, assertMessage);
//    }
//
//    /**
//     * @author anuj gupta
//     */
//    @Feature("BRM")
//    @Story("Change plan from prepaid to postpaid of customer having monetary balance in BRM")
//    @Test
//    public void TestCase_C108386() {
//
//        // create prepaid customer
//        String custAccountNumber = caam.createCustomer("chetan", "Prepaid", aliases.get("caam.prepaid.basic.plan"), aliases.get("caam.billingCyclename"), "");
//        assertMessage = "Customer account is not created";
//        Assert.assertNotNull(custAccountNumber, assertMessage);
//
//        // Change the status to active
//        String changeStatusMessage = caam.changeStatus(custAccountNumber, "Active");
//        assertMessage = "Customer status is not changed";
//        Assert.assertTrue(changeStatusMessage.contains("Successfully"), assertMessage);
//
//        // Subcribe an addon
//        String response = caam.subScribeAddonPkg(custAccountNumber, "prepaid_addon");
//        assertMessage = "Addon package is successfully subscribed.";
//        Assert.assertTrue(response.contains("successfully"), assertMessage);
//
//        // Create billing account number
//        String billingAccountNumber = caam.createBillingAccount(custAccountNumber, "Postpaid", "");
//        assertMessage = "Unable to create billing account";
//        Assert.assertNotNull(billingAccountNumber, assertMessage);
//
//        // Change service plan from prepaid to postpaid
//        String changePlanMessage = caam.changeServicePlan(custAccountNumber, "Postpaid", aliases.get("caam.planName"));
//        assertMessage = "Unable to change plan from postpaid to postpaid";
//        Assert.assertTrue(changePlanMessage.contains("Successfully"), assertMessage);
//
//        // Generate bill/OD Bill after 10days subscription
//        String responseMessage = caam.generateODBill(custAccountNumber, CommonUtils.getDateByDayOffset(1));
//        assertMessage = "OD Bill not Generated.";
//        Assert.assertTrue(responseMessage.contains("successfully"), assertMessage);
//    }

    @Test
    public void test1() {
        Map<String, String> map = excel.getColumnAsMap("locators", 0, 1);
        for (Map.Entry<String, String> entry : map.entrySet())
            report.pass("Key: " + entry.getKey() + " Value: " + entry.getValue());
    }

    @Test
    public void test2() {
        report.pass("2");
    }

    @Test
    public void test3() {
        report.pass("3");
    }

    @Test
    public void test4() {
        report.pass("4");
    }

    @Test
    public void test5() {
        report.pass("5");
    }

    @Test
    public void test6() {
        report.pass("6");
    }
}
