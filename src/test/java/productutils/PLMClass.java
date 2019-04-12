package productutils;

import frameworkutils.BrowserFactory;
import frameworkutils.ExcelManager;
import frameworkutils.LogManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class PLMClass extends BaseTest {

    private LogManager log = LogManager.getInstance();
    private ExcelManager excel = ExcelManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();
    private Map<String, String> rowData;

    @Step
    public String createProductSpecification(String psName, String serviceName) {
        String message = null;
        try {
            psName = psName + "_" + System.currentTimeMillis();
            clickOnElement(getLocator("plm.ps.menu.css"));
            clickOnElement(getLocator("plm.ps.submenu.css"));
            //selectOptionFromLHSMenu("Product Specification", "Product Specification");
            Thread.sleep(1000);
            switchToFrame1("iframe");
            waitUntilVisible(By.id("zk_comp_btnCreate"));
            clickOnButton("Create");
            Thread.sleep(1000);
            sendTextToElement(By.cssSelector(getLocator("plm.ps.name.txtbox.css")), psName);
            WebElement addservicebtn = waitUntilVisible(By.xpath(getLocator("plm.ps.addBasicService.btn.xpath")));
            addservicebtn.click();
            selectValueFromDropDownBox(By.cssSelector(getLocator("plm.ps.serspec.sername.ddb.btn.css")), By.cssSelector(getLocator("plm.ps.serspec.sername.ddb.listview.css")), serviceName);
            clickOnButton(getLocator("plm.create.btn.name"));

            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            message = mesgbox.getText();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return message;
    }

    @Step
    public String createPackageValidity() {
        String message = null;
        try {
            ////clickOnElement(getLocator("plm.ps.menu.css"));
            ////clickOnElement(getLocator("plm.ps.submenu.css"));
            selectOptionFromLHSMenu("Usage Tariff", "Validity Policy");
            Thread.sleep(1000);
            switchToFrame1("iframe");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return message;
    }

    @Step
    public String createRateCard(String rateCardName, String rateCardType) {
        String message = null;
        try {
            rateCardName = rateCardName + "_" + System.currentTimeMillis();
            selectOptionFromLHSMenu("Usage Tariff", "Rate Card");
            Thread.sleep(1000);
            switchToFrame1("iframe");
            waitUntilVisible(By.id("zk_comp_btnCreate"));
            clickOnButton("Create");
            Thread.sleep(1000);

            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbRCType-btn"), By.cssSelector("#zk_comp_cmbRCType-pp"), rateCardType);
            sendTextToElement(By.cssSelector("#zk_comp_txtName"), rateCardName);
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbCurrency-btn"), By.cssSelector("#zk_comp_cmbCurrency-pp"), "INR");
            sendTextToElement(By.cssSelector("#zk_comp_dtxtPeakRate"), "0.10");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbRateUOM-btn"), By.cssSelector("#zk_comp_cmbRateUOM-pp"), "KB");
            sendTextToElement(By.cssSelector("#zk_comp_txtPulse"), "1");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbUOM-btn"), By.cssSelector("#zk_comp_cmbUOM-pp"), "KB");
            clickOnButton("Create");

            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            message = mesgbox.getText();
            log.info(message);
        } catch (Exception e) {
            rateCardName = null;
            log.error(e.getMessage());
        }
        return rateCardName;
    }

    @Step
    public String createRateCardGroup(String RateCardGroupName, String RateCardName, String RateCardType) {
        try {
            RateCardGroupName = RateCardGroupName + "_" + System.currentTimeMillis();
            selectOptionFromLHSMenu("Usage Tariff", "Rate Card Group");
            Thread.sleep(1000);
            switchToFrame1("iframe");
            waitUntilVisible(By.id("zk_comp_buttonCreate"));
            clickOnButton("Create");
            Thread.sleep(1000);

            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbLimitTypeId-btn"), By.cssSelector("#zk_comp_cmbLimitTypeId-pp"), RateCardType);
            sendTextToElement(By.cssSelector("#zk_comp_txtName"), RateCardGroupName);
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbCurrencyId-btn"), By.cssSelector("#zk_comp_cmbCurrencyId-pp"), "INR");
            clickOnButton("Add");
            Thread.sleep(500);
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_rpcombo_1-btn"), By.cssSelector("#zk_comp_rpcombo_1-pp"), "DefaultRatingPolicy");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_rccombo_1-btn"), By.cssSelector("#zk_comp_rccombo_1-pp"), RateCardName);
            clickOnButton("Create");


            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            String message = mesgbox.getText();
            log.info(message);
            Assert.assertEquals(message.contains("successfully"), true, "Fail create method.");
        } catch (Exception e) {
            RateCardGroupName = null;
            log.error(e.getMessage());
            Assert.fail("Fail create method.");
        }
        return RateCardGroupName;

    }

    @Step
    public void createUnitCreditPolicy() {
        try {
            selectOptionFromLHSMenu("Usage Tariff", "Unit Credit Policy");
            Thread.sleep(1000);
            switchToFrame1("iframe");


            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            String message = mesgbox.getText();
            log.info(message);
            Assert.assertEquals(message.contains("successfully"), true, "Fail create method.");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Fail create method.");
        }
    }

    @Step
    public void createUsageParameterGroup() {
        try {
            //clickOnElement(getLocator("plm.ps.menu.css"));
            //clickOnElement(getLocator("plm.ps.submenu.css"));
            selectOptionFromLHSMenu("Usage Tariff", "Usage Parameter Group");
            Thread.sleep(1000);
            switchToFrame1("iframe");


            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            String message = mesgbox.getText();
            log.info(message);
            Assert.assertEquals(message.contains("successfully"), true, "Fail create method.");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Fail create method.");
        }
    }

    /**
     * Creates a new charge using data driven strategy
     *
     * @param excelRow Row number of the excel sheet in which the charge creation data is stored
     * @return Name of the charge if created successfully otherwise returns null
     * @author anuj gupta
     */
    @Step
    public String createCharge(int excelRow) {

        String chargeName = null;
        try {
            Map<String, String> data = excel.getRowAsMap("createCharge", excelRow);

            // Clicks on product manager link
            clickOverMenu("Product Manager");

            // clicks on charge management link under non usage tariff menu option
            selectOptionFromLHSMenu("Non Usage Tariff", "Charge Management");

            // switch to iframe and then clicks on create button
            switchToFrame1("iframe");
            clickOnButton("Create");

            // Creates a random chargeName and sets in charge name field
            chargeName = "Automation_" + generateRandomString(3);
            sendTextToElement(By.cssSelector("#zk_comp_txtChargeNameId"), chargeName);

            // Creates a date in given format and sets date in start date field
            CommonUtils.sendDate(By.cssSelector("#zk_comp_fromDateID-real"), CommonUtils.getDateByDayOffset(0));

            // Enters value in status dropdown
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbStatusId-btn"), By.cssSelector("#zk_comp_cmbStatusId-pp"), data.get("status"));

            // Enters value in currency dropdown
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbCurrencyId-btn"), By.cssSelector("#zk_comp_cmbCurrencyId-pp"), data.get("currency"));

            selectRadioButton(data.get("chargingPattern"));
            selectRadioButton(data.get("chargeType"));

            if (data.get("chargingPattern").equals("Postpaid")) {
                selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbchargeAssociationId-btn"), By.cssSelector("#zk_comp_cmbchargeAssociationId-pp"), data.get("chargeAssociation"));

                if (data.get("chargeAssociation").equals("Package")) {
                    selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbTrigEventId-btn"), By.cssSelector("#zk_comp_cmbTrigEventId-pp"), data.get("triggeringEvent"));
                    selectRadioButton(data.get("prorationFlag"));

                    if (data.get("triggeringEvent").equals("All")) {
                        selectRadioButton(data.get("billType"));
                    }
                }
            }

            if (data.get("chargeType").equals("Policy Based"))
                selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbLookupPolicyId-btn"), By.cssSelector("#zk_comp_cmbLookupPolicyId-pp"), data.get("lookupPolicy"));

            if (!data.get("revenueCode").isEmpty()) {
                clickOnElement("#zk_comp_ledgerCodeViewImageId");
                clickonElement1("//div[text()='" + data.get("revenueCode") + "']");
                clickOnButton("Select");
            }
            pause(1000);
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbChargeCategoryId-btn"), By.cssSelector("#zk_comp_cmbChargeCategoryId-pp"), data.get("chargeCategory"));

            setChargeCycle(data);

            if (!data.get("attributeName").isEmpty()) {
                clickOnElement("#zk_comp_chargeSpecificationTabId-cnt");
                clickOnButton("New Attribute");
                selectValueFromDropDownBox(By.cssSelector("#zk_comp_attributecombo1-btn"), By.cssSelector("#zk_comp_attributecombo1-pp"), data.get("attributeName"));
                sendTextToElement(By.cssSelector("#zk_comp_attributevalue1"), data.get("attributeValue"));
            }

            // Click on create button
            clickOnButton("Create");

            // wait for success message to be visible
            pause(1000);

            // Strores locator of charge name
            By chargeNameLocator = By.cssSelector("#viewchargepageid");

            // If charge is successfully created, stores charge name and click on ok button on success message alert box
            if (isElementPresent(chargeNameLocator)) {
                chargeName = getTextFromElement(chargeNameLocator);
                clickElement("#zk_comp_btnTab");
            } else {
                chargeName = null;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return chargeName;
    }

    /**
     * Set charge cycle while creating a charge
     *
     * @param data Row number of the excel sheet in which the charge creation data is stored
     * @author anuj gupta
     */
    private void setChargeCycle(Map<String, String> data) {

        try {
            scrollToamount();
            clickOnElement("#zk_comp_cmbchargepatternid-btn");

            switch (data.get("recurrencePattern")) {

                case "Recurring":
                    clickonElement1("//span[text()='Recurring']");
                    pause(2000);
                    scrollToamount();

                    if (isElementPresent(By.cssSelector("#zk_comp_cmbrecchargecyclepolicyid-btn"))) {
                        clickOnElement("#zk_comp_cmbrecchargecyclepolicyid-btn");
                        pause(1000);
                        clickonElement1("//ul[@id='zk_comp_cmbrecchargecyclepolicyid-cave']//span[text()='Monthly']");

                    } else if (isElementPresent(By.cssSelector("#zk_comp_price_cht02_monthly")))
                        sendTextToElement(By.cssSelector("#zk_comp_price_cht02_monthly"), data.get("price"));
                    break;

                case "Non Recurring":
                    clickonElement1("//span[contains(text(),'Non')]");
                    pause(2000);
                    scrollToamount();

                    if (isElementPresent(By.cssSelector("#zk_comp_txtnrcpriceid")))
                        sendTextToElement(By.cssSelector("#zk_comp_txtnrcpriceid"), data.get("price"));

                    else if (isElementPresent(By.cssSelector("#zk_comp_period_cht03_monthly"))) {
                        sendTextToElement(By.cssSelector("#zk_comp_period_cht03_monthly"), data.get("period"));
                        sendTextToElement(By.cssSelector("#zk_comp_price_cht03_monthly"), data.get("price"));
                    }
                    break;
                case "Recurring Limited Period":
                    clickonElement1("//span[contains(text,'Limited')]");
                    pause(2000);
                    scrollToamount();

                    if (isElementPresent(By.cssSelector("#zk_comp_cmbrecchargelimitedcyclepolicyid-btn"))) {
                        clickOnElement("#zk_comp_cmbrecchargelimitedcyclepolicyid-btn");
                        pause(2000);
                        clickonElement1("//ul[@id='zk_comp_cmbrecchargelimitedcyclepolicyid-cave']//span[text()='Monthly']");

                    } else if (isElementPresent(By.cssSelector("#zk_comp_txtprerclpolicyperiodid")))
                        sendTextToElement(By.cssSelector("#zk_comp_txtprerclpolicyperiodid"), data.get("period"));

                    else if (isElementPresent(By.cssSelector("#zk_comp_txtprerclpriceid"))) {
                        sendTextToElement(By.cssSelector("#zk_comp_txtprerclpriceid"), data.get("price"));
                        sendTextToElement(By.cssSelector("#zk_comp_txtprerclperiodid"), data.get("period"));
                    }
                    break;
                default:
                    log.error("Invalid recurrence pattern found");
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Creates a new PLM package using data driven approach
     * <p>
     * All configuration data of one type of package is stored in a separate row in excel.
     * </p>
     *
     * @param excelRow Row number of the excel sheet in which the configuration data is stored
     * @return Success message used for assertion
     * @author anuj gupta
     */
    @Step
    public String createPackage(int excelRow) {

        String response = "";
        rowData = excel.getRowAsMap("createPackage", excelRow);
        try {
            // Clicks on product manager link
            clickOverMenu("Product Manager");

            waitForElement(By.xpath("//span[contains(text(),'Product Offer')]"), WaitType.ELEMENT_TO_BE_CLICKABLE);

            // clicks on 'product package' link under 'product offer' menu option
            selectOptionFromLHSMenu("Product Offer", "Product Package");

            // switch to iframe and then clicks on create button
            switchToFrame1("iframe");

            waitForElement(By.cssSelector("#zk_comp_buttonCreate"), WaitType.ELEMENT_TO_BE_CLICKABLE);
            clickOnButton("Create");

            // select charging pattern from dropdown
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbChargingPattern-btn"), By.cssSelector("#zk_comp_cmbChargingPattern-pp"), rowData.get("chargingPattern"));

            // select category
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbPackageCategoryId-btn"), By.cssSelector("#zk_comp_cmbPackageCategoryId-pp"), rowData.get("category"));

            // click on create button
            clickOnButton("Next");

            // Creates a random chargeName and sets in charge name field
            String packageName = "Automation_" + rowData.get("chargingPattern") + generateRandomString(3);
            sendTextToElement(By.cssSelector("#zk_comp_packageNameId"), packageName);

            // Select status
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_policyStatusComboId-btn"), By.cssSelector("#zk_comp_policyStatusComboId-pp"), rowData.get("status"));

            // Select currency
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_currencyComboID-btn"), By.cssSelector("#zk_comp_currencyComboID-pp"), rowData.get("currency"));

            // Configure details
            configureBasicDetails();
            configureProductSpecification();
            configureUsageCharges();
            configureCharges();
            configureDeposit();
            configureDiscount();
            configureOfferCharacterstics();

            clickOnButton("Create");
            response = getTextFromElement("#viewproductpageid");
            clickOnButton("Ok");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    /**
     * @author anuj gupta
     */
    @Step
    private void configureBasicDetails() {

        if (!rowData.get("shelfValidityMonths").isEmpty()) {
            sendTextToElement(By.cssSelector("#zk_comp_shelfMonths"), rowData.get("shelfValidityMonths"));
            sendTextToElement(By.cssSelector("#zk_comp_shelfDays"), rowData.get("shelfValidityDays"));
        }
        selectValueFromDropDownBox(By.cssSelector("#zk_comp_cmbValidPolicyID-btn"), By.cssSelector("#zk_comp_cmbValidPolicyID-pp"), rowData.get("packageValidityPolicy"));
        clickOnButton("Next");
    }

    /**
     * @author anuj gupta
     */
    @Step
    private void configureProductSpecification() {

        if (!rowData.get("productSpecName").isEmpty()) {
            clickOnButton("Add Prod Spec");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboprodspecname1-btn"), By.cssSelector("#zk_comp_comboprodspecname1-pp"), rowData.get("productSpecName"));
            clickOnButton("Next");

        } else if (isElementPresent(By.xpath("//li[contains(@class,'z-tab-selected')]//span[text()='Configure Product Specification']")))
            clickOnButton("Next");
    }

    /**
     * @author anuj gupta
     */
    @Step
    private void configureUsageCharges() {

        if (!rowData.get("usageLimit").isEmpty())
            sendTextToElement(By.cssSelector("#zk_comp_usagelimitid"), rowData.get("usageLimit"));

        if (!rowData.get("basicServiceName").isEmpty()) {
            clickOnButton("Add Basic Service");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_service_basic_service_upg_1-btn"), By.cssSelector("#zk_comp_comboid_service_basic_service_upg_1-pp"), rowData.get("basicServiceName"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_upgcombo_basic_service_upg_1-btn"), By.cssSelector("#zk_comp_comboid_upgcombo_basic_service_upg_1-pp"), rowData.get("basicPccPackage"));
        }
        if (!rowData.get("vasServiceName").isEmpty()) {
            clickOnButton("Add VAS Service");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_service_vas_service_upg_1-btn"), By.cssSelector("#zk_comp_comboid_service_vas_service_upg_1-pp"), rowData.get("vasServiceName"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_upgcombo_vas_service_upg_1-btn"), By.cssSelector("#zk_comp_comboid_upgcombo_vas_service_upg_1-pp"), rowData.get("vasPccPackage"));
        }
        clickOnButton("Next");
    }

    /**
     * @author anuj gupta
     */
    @Step
    private void configureCharges() {

        if (!rowData.get("rcName").isEmpty()) {
            clickOnElement("#zk_comp_addreccharge");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_itmchg_rc_id_1-btn"), By.cssSelector("#zk_comp_comboid_itmchg_rc_id_1-pp"), rowData.get("rcName"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_billcycle_rc_id_1-btn"), By.cssSelector("#zk_comp_comboid_billcycle_rc_id_1-pp"), rowData.get("rcBillCycle"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_service_rc_id_1-btn"), By.cssSelector("#zk_comp_comboid_service_rc_id_1-pp"), rowData.get("rcService"));
        }

        if (!rowData.get("rlpName1").isEmpty()) {
            clickOnElement("#zk_comp_addreclmp");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_itmchg_rlc_id_1-btn"), By.cssSelector("#zk_comp_comboid_itmchg_rlc_id_1-pp"), rowData.get("rlpName1"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_billcycle_rlc_id_1-btn"), By.cssSelector("#zk_comp_comboid_billcycle_rlc_id_1-pp"), rowData.get("rlpBillCycle1"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_service_rlc_id_1-btn"), By.cssSelector("#zk_comp_comboid_service_rlc_id_1-pp"), rowData.get("rlpService1"));
        }

        if (!rowData.get("rlpName2").isEmpty()) {
            clickOnElement("#zk_comp_addreclmp");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_itmchg_rlc_id_2-btn"), By.cssSelector("#zk_comp_comboid_itmchg_rlc_id_2-pp"), rowData.get("rlpName2"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_billcycle_rlc_id_2-btn"), By.cssSelector("#zk_comp_comboid_billcycle_rlc_id_2-pp"), rowData.get("rlpBillCycle2"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_service_rlc_id_2-btn"), By.cssSelector("#zk_comp_comboid_service_rlc_id_2-pp"), rowData.get("rlpService2"));
        }

        if (!rowData.get("nrcName1").isEmpty()) {
            clickOnElement("#zk_comp_addnonrec");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_itmchg_nrc_id_1-btn"), By.cssSelector("#zk_comp_comboid_itmchg_nrc_id_1-pp"), rowData.get("nrcName1"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_service_nrc_id_1-btn"), By.cssSelector("#zk_comp_comboid_service_nrc_id_1-pp"), rowData.get("nrcService1"));
        }

        if (!rowData.get("nrcName2").isEmpty()) {
            clickOnElement("#zk_comp_addnonrec");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_itmchg_nrc_id_2-btn"), By.cssSelector("#zk_comp_comboid_itmchg_nrc_id_2-pp"), rowData.get("nrcName2"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_comboid_service_nrc_id_2-btn"), By.cssSelector("#zk_comp_comboid_service_nrc_id_2-pp"), rowData.get("nrcService2"));
        }

        clickOnButton("Next");
    }

    /**
     * @author anuj gupta
     */
    @Step
    private void configureDeposit() {

        if (!rowData.get("depositName").isEmpty()) {
            clickOnButton("Add Deposit");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_combodepositname1-btn"), By.cssSelector("#zk_comp_combodepositname1-pp"), rowData.get("depositName"));
            clickOnButton("Next");
        } else if (isElementPresent(By.xpath("//li[contains(@class,'z-tab-selected')]//span[text()='Configure Deposit']")))
            clickOnButton("Next");
    }

    /**
     * @author anuj gupta
     */
    @Step
    private void configureDiscount() {

        if (!rowData.get("discountName").isEmpty()) {
            clickOnButton("Add Discount");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_combodiscountname1-btn"), By.cssSelector("#zk_comp_combodiscountname1-pp"), rowData.get("discountName"));
            clickOnButton("Next");
        } else if (isElementPresent(By.xpath("//li[contains(@class,'z-tab-selected')]//span[text()='Configure Discount']")))
            clickOnButton("Next");
    }

    /**
     * @author anuj gupta
     */
    @Step
    private void configureOfferCharacterstics() {

        if (!rowData.get("attributeName").isEmpty()) {
            clickOnButton("New Attribute");
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_attributecombo1-btn"), By.cssSelector("#zk_comp_attributecombo1-pp"), rowData.get("attributeName"));
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_attributesingledesctvalue1-btn"), By.cssSelector("#zk_comp_attributesingledesctvalue1-pp"), rowData.get("attributeValue"));
        }
    }

    /**
     * Associates a package with the business hierarchy
     *
     * @param packageName Name of the package
     * @param serviceName Service name of the package
     * @param category    cateory of the package
     * @param level       levels of business hierarchy
     * @return boolean result whether association is successful or not
     * @author anuj gupta
     */
    @Step
    public boolean associateBusinessHierarchy(String packageName, String serviceName, String category, String... level) {

        boolean isAssociated = false;
        try {
            // Clicks on product manager link
            clickOverMenu("Product Manager");

            waitForElement(By.xpath("//span[contains(text(),'Product Hierarchy')]"), WaitType.ELEMENT_TO_BE_CLICKABLE);

            // clicks on 'product package' link under 'product offer' menu option
            selectOptionFromLHSMenu("Product Hierarchy", "Business Hierarchy");

            // switch to iframe and then clicks on create button
            switchToFrame1("iframe");

            // Loop through all the levels and click select each level
            for (int i = 0; i < level.length - 1; i++) {
                By locator = By.xpath("//span[contains(text(),'" + level[i] + "')]/preceding-sibling::span/i");
                clickonElement1(locator);
            }
            // On final level, click to view option 'Associate Package'
            clickonElement1("//span[contains(text(),'" + level[level.length - 1] + "')]");

            // Click on associate package link
            clickonElement1("//span[text()='Associate Package']");

            // Enter details
            sendTextToElement(By.cssSelector("#zk_comp_confignodeNameId"), packageName);
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_serviceComboId-btn"), By.cssSelector("#zk_comp_serviceComboId-pp"), serviceName);
            selectValueFromDropDownBox(By.cssSelector("#zk_comp_categoryComboID-btn"), By.cssSelector("#zk_comp_categoryComboID-pp"), category);

            // click on search button
            clickOnButton("Search");

            // Select the reqbrowser.getDriver()red pacakge
            clickOnElement("#zk_comp_pkgname_1-cave");

            // click on associate button
            clickOnButton("Associate");
            isAssociated = isElementPresent(By.xpath("//span[contains(text(),'" + packageName + "')]"));

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return isAssociated;
    }

    /**
     * Creates a node at root level
     *
     * @param nodeName Name of the node to be created
     * @return boolean success result
     * @author anuj gupta
     */
    @Step
    public boolean createNode(String nodeName) {

        boolean isCreated = false;
        try {
            // Clicks on product manager link
            clickOverMenu("Product Manager");

            waitForElement(By.xpath("//span[contains(text(),'Product Hierarchy')]"), WaitType.ELEMENT_TO_BE_CLICKABLE);

            // clicks on 'product package' link under 'product offer' menu option
            selectOptionFromLHSMenu("Product Hierarchy", "Business Hierarchy");

            // switch to iframe and then clicks on create button
            switchToFrame1("iframe");
            clickOnButton("Create Level1 Node");
            sendTextToElement(By.cssSelector("#zk_comp_confignodeNameId"), nodeName);
            clickOnElement("#zk_comp_configdescId");
            clickOnButton("Create");

            pause(500);
            isCreated = isElementPresent(By.xpath("//table[@id='zk_comp_hierarchyTree-cave']//span[contains(text(),'" + nodeName + "')]"));

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return isCreated;
    }

    @Step
    public void createDiscount() {
        try {
            //clickOnElement(getLocator("plm.ps.menu.css"));
            //clickOnElement(getLocator("plm.ps.submenu.css"));
            selectOptionFromLHSMenu("Non Usage Tariff", "Discount Management");
            Thread.sleep(1000);
            switchToFrame1("iframe");


            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            String message = mesgbox.getText();
            log.info(message);
            Assert.assertEquals(message.contains("successfully"), true, "Fail create method.");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Fail create method.");
        }
    }

    @Step
    public void createDeposite() {
        try {
            //clickOnElement(getLocator("plm.ps.menu.css"));
            //clickOnElement(getLocator("plm.ps.submenu.css"));
            selectOptionFromLHSMenu("Non Usage Tariff", "Deposit Management");
            Thread.sleep(1000);
            switchToFrame1("iframe");


            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            String message = mesgbox.getText();
            log.info(message);
            Assert.assertEquals(message.contains("successfully"), true, "Fail create method.");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Fail create method.");
        }
    }

    @Step
    public void createProductOffer() {
        try {
            //clickOnElement(getLocator("plm.ps.menu.css"));
            //clickOnElement(getLocator("plm.ps.submenu.css"));
            selectOptionFromLHSMenu("Usage Tariff", "Rate Card");
            Thread.sleep(1000);
            switchToFrame1("iframe");


            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            String message = mesgbox.getText();
            log.info(message);
            Assert.assertEquals(message.contains("successfully"), true, "Fail create method.");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Fail create method.");
        }
    }

    @Step
    public void createBusinessHierarchy() {
        try {
            //clickOnElement(getLocator("plm.ps.menu.css"));
            //clickOnElement(getLocator("plm.ps.submenu.css"));
            selectOptionFromLHSMenu("Usage Tariff", "Rate Card");
            Thread.sleep(1000);
            switchToFrame1("iframe");


            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            String message = mesgbox.getText();
            log.info(message);
            Assert.assertEquals(message.contains("successfully"), true, "Fail create method.");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Fail create method.");
        }
    }

    @Step
    public void associatePackage() {
        try {
            //clickOnElement(getLocator("plm.ps.menu.css"));
            //clickOnElement(getLocator("plm.ps.submenu.css"));
            selectOptionFromLHSMenu("Usage Tariff", "Rate Card");
            Thread.sleep(1000);
            switchToFrame1("iframe");


            WebElement mesgbox = waitUntilVisible(By.xpath(".//*[@id='zk_comp_modalDialog-cave']"));
            String message = mesgbox.getText();
            log.info(message);
            Assert.assertEquals(message.contains("successfully"), true, "Fail create method.");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Fail create method.");
        }
    }

    @Step
    public boolean reloadCache() {

        boolean isReloadDone = false;
        try {
            // Clicks on product manager link
            clickOverMenu("Product Manager");

            selectOptionFromLHSMenu("System Configuration", "Reload Cache");

            switchToFrame1("iframe");
            clickOnButton("Ok");
            isReloadDone = true;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return isReloadDone;
    }

    @Step
    private void selectOptionFromLHSMenu(String menu, String subMenu) {
        selectOptionFromLHSMenu(menu);
        selectOptionFromLHSMenu(subMenu);
    }

    @Step
    private void selectOptionFromLHSMenu(String menu) {
        try {
            browser.getDriver().switchTo().defaultContent();
            Thread.sleep(500);
/*
        List<WebElement> listTR = getDriver().findElements(By.tagName("tr"));
        int i=0;
        for (WebElement tr : listTR) {
            String classname = tr.getAttribute("class");
            if(!classname.contains("selected")) {
*/
            Thread.sleep(500);
            List<WebElement> spanlist = browser.getDriver().findElements(By.tagName("span"));
            for (WebElement span : spanlist) {
                String menuName = span.getText().trim();
                if (menuName.equals(menu.trim())) {
                    span.click();
                    break;
                }
            }
            Thread.sleep(500);
 /*           }
            if(i==1){
                break;
            }
        }*/
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
