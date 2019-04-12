package productutils;

import frameworkutils.BrowserFactory;
import frameworkutils.LogManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DunningClass extends BaseTest {

    private LogManager log = LogManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();
    private BaseTest baseTest = BaseTest.getInstance();

    @Step
    public void createAction(String ActionType) {
        try {

            baseTest.pause(3000);
            clickOverSubMenu("Dunning", "Dunning Configuration", "Action Management");
            baseTest.pause(3000);

            switchToFrame1("iframe");

            baseTest.pause(3000);
            clickonElement1(locators.get("CD_Btn_Action"));

            String Name_Action = getAlias("txt_action_name") + generateRandomString();

            WebElement ActionName = findElement(locators.get("txt_action_name"));

            sendTextToElement(ActionName, Name_Action);
            baseTest.pause(3000);


            selectValueFromDropDownBox(By.cssSelector("#cmbcactiontype-btn"), By.cssSelector("#cmbcactiontype-pp"), ActionType);

            String value1 = getAlias("Action_Category");
            selectValueFromDropDownBox(By.cssSelector("#cmbcdunningtype-btn"), By.cssSelector("#cmbcdunningtype-pp"), value1);

            String ET = getAlias("drp_dwn_Execution_Type");
            ET.replaceAll(" ", "");
            selectValueFromDropDownBox(By.cssSelector(locators.get("drp_dwn_Execution_Type")), By.cssSelector("#cmbcexecutiontype-pp"), ET);

            scrollToamount();
            clickOnElement("#cmbpluginname-btn");

            List<WebElement> pluginname = findElements1("//*[@id=\"cmbpluginname-cave\"]/li/span[2]");
            scrollToamount();
            for (int i = 0; i < pluginname.size(); i++) {
                String plugin = pluginname.get(i).getText();
                String Value = getAlias("drp_dwn_plugin_name").trim();
                if (plugin.contains(Value)) {
                    pluginname.get(i).click();
                    log.info("Plugin name Selected");
                    break;
                }
            }


            WebElement Description = findElement(locators.get("txt_description"));
            sendTextToElement(Description, getAlias("txt_description"));

            scrollToamount();

            clickOnElement(locators.get("Chk_box_SelectAll"));

            scrollToamount();
            scrollTo(findElement("#btnccreate"));

            baseTest.pause(2000);

            clickOnElement("#btnccreate");

            clickOnElement("#btn1");

            String Message = findElement1(locators.get("Get_Message")).getText();

            if (Message.contains("Creation of test Action is successful.")) {
                log.info("Creation of test Action is successful.");

            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Step
    public void createSatffGroup(String StaffName1, String StaffName2) throws InterruptedException {
        try {

            baseTest.pause(5000);

            clickOverSubMenu("Dunning", "Dunning Configuration", "Staff Group");

            switchToFrame1("iframe");
            baseTest.pause(5000);

            clickOnElement(locators.get("CD_btn_CreateStaffGroup"));
            baseTest.pause(3000);

            WebElement StaffGroup_Name = findElement(locators.get("CD_StaffGroupName"));

            String StaffGroup = getAlias("StaffGroupName") + generateRandomString();

            sendTextToElement(StaffGroup_Name, StaffGroup);

            baseTest.pause(5000);

            sendTextToElement(findElement(locators.get("Staff_Descripton")), getAlias("Staff_Descripton"));

            baseTest.pause(4000);

            clickOnElement(locators.get("Search_AdminName"));

            WebElement txt_staffName = findElement(locators.get("txt_Staff_Name"));

            String StaffName = getAlias("txt_Staff_Name");

            sendTextToElement(txt_staffName, StaffName);

            clickOnElement(locators.get("Btn_search"));
            baseTest.pause(3000);

            List<WebElement> AdminName = findElements1(locators.get("tbl_UserName"));
            for (int i = 0; i < AdminName.size(); i++) {
                String Name = AdminName.get(i).getText();
                if (Name.equals(StaffName1)) {
                    AdminName.get(i).click();
                    log.info("Admin Name Selected");
                    break;

                }
            }
            clickOnElement(locators.get("btn_Select"));

            baseTest.pause(5000);

            clickOnElement(locators.get("Staff_Name_Search"));
            WebElement txt_staffName1 = findElement(locators.get("txt_Staff_Name"));
            sendTextToElement(txt_staffName1, StaffName2);
            clickOnElement(locators.get("Btn_search"));
            baseTest.pause(5000);

            List<WebElement> StaffName01 = findElements1(locators.get("tbl_UserName"));
            for (int i = 0; i < StaffName01.size(); i++) {
                String SName1 = StaffName01.get(i).getText();
                if (SName1.contains(StaffName2)) {
                    StaffName01.get(i).click();
                    log.info("Staff Name 1 Selected");
                    break;
                }
            }

            clickOnElement(locators.get("btn_Select"));

            clickOnElement(locators.get("btn_Add_Details"));

/////////////////////////////////////////
            baseTest.pause(3000);
            clickOnElement(locators.get("Staff_Name_Search"));

            baseTest.pause(5000);

            WebElement txt_staffName2 = findElement(locators.get("txt_Staff_Name"));
            sendTextToElement(txt_staffName2, StaffName1);
            clickOnElement(locators.get("Btn_search"));
            baseTest.pause(3000);

            List<WebElement> StaffName02 = findElements1(locators.get("tbl_UserName"));
            for (int i = 0; i < StaffName02.size(); i++) {
                String SName2 = StaffName02.get(i).getText();
                if (SName2.equals(StaffName1)) {
                    StaffName02.get(i).click();
                    log.info("Staff Name 2 Selected");
                    break;

                }
            }

            clickOnElement(locators.get("btn_Select"));

            baseTest.pause(2000);

            clickOnElement(locators.get("btn_Add_Details"));

            clickOnElement(locators.get("btn_createstaffgroup"));

            baseTest.pause(3000);

            String Message = findElement1("//*[@class=\"z-messagebox z-div\"]/span").getText();

            if (Message.contains("Staff Group is successful.")) {
                savedValues.put("StaffGroup", StaffGroup);
                log.info("Creation of Staff Group is successful.");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Creation of  Staff Group Failed");
        }
    }

    @Step
    public void ScenarioManagementManualAction() {
        try {

            baseTest.pause(3000);
            clickOverSubMenu("Dunning", "Dunning Configuration", "Scenario Management");
            baseTest.pause(3000);

            switchToFrame1("iframe");

            baseTest.pause(2000);

            clickOnElement(locators.get("btn_Scenario_Create"));

            String ScenarioName = getAlias("txt_scenarioName") + generateRandomString();
            baseTest.pause(2000);

            WebElement Name = findElement(locators.get("txt_scenarioName"));
            sendTextToElement(Name, ScenarioName);

            savedValues.put("ScenarioName", ScenarioName);

            baseTest.pause(2000);
            selectValueFromDropDownBox(By.cssSelector(locators.get("select_btn_currency")), By.cssSelector(locators.get("drp_dwn_currency")), "INR");

            baseTest.pause(2000);
            clickOnElement(locators.get("btn_reversal"));

            baseTest.pause(2000);
            clickOnElement(locators.get("btn_cyclicreversal"));

            String Scenarios = getAlias("number_of_scenarios");

            int iTest = Integer.parseInt(Scenarios);
            for (int j = 1; j <= iTest; j++) {

                clickOnElement(locators.get("btn_level"));

                baseTest.pause(2000);

                List<WebElement> LevelSelection = findElements1(locators.get("drp_dwn_level"));
                for (int i = 0; i < LevelSelection.size(); i++) {

                    String Level = LevelSelection.get(i).getText().trim();

                    String Scene = getAlias("Level" + j).trim();

                    if (Scene.equals(Level)) {
                        LevelSelection.get(i).click();
                        break;
                    }
                }

                scrollToamount();

                clickOnElement(locators.get("btn_action"));

                List<WebElement> Action = findElements1(locators.get("drp_dwn_Action"));
                for (int i = 0; i < Action.size(); i++) {

                    String Value = Action.get(i).getText().trim();

                    String Value_Action = getAlias("Action" + j).trim();

                    if (Value.equals(Value_Action)) {
                        Action.get(i).click();
                        break;
                    }
                }

                scrollToamount();

                baseTest.pause(2000);

                clickOnElement(locators.get("btn_reversalAction"));

                List<WebElement> Reversal_Selection = findElements1(locators.get("drp_dwn_reversalAction"));
                for (int i = 0; i < Reversal_Selection.size(); i++) {
                    String RS = Reversal_Selection.get(i).getText();
                    String Reversal_Action = getAlias("Reactivate" + j);
                    if (RS.equals(Reversal_Action)) {
                        Reversal_Selection.get(i).click();
                        log.info("Reversal Action is selected");
                        break;
                    }
                }

                WebElement Agingfrom = findElement(locators.get("txt_agingFrom"));
                sendTextToElement(Agingfrom, getAlias("txt_agingFrom" + j));

                WebElement AgingTO = findElement(locators.get("txt_agingTo"));
                sendTextToElement(AgingTO, getAlias("txt_agingTo" + j));

                scrollToamount();
                scrollToamount();

                baseTest.pause(2000);
                clickOnElement(locators.get("drp_dwn_staffgroupname"));

                baseTest.pause(2000);
                List<WebElement> GroupNAME = findElements1(locators.get("GroupName_values"));
                for (int i = 0; i < GroupNAME.size(); i++) {
                    String Name1 = GroupNAME.get(i).getText().trim();
                    String StaffGroup = getSavedValues("StaffGroup");
                    if (Name1.equals("Group_ikemnlqaqx")) {
                        GroupNAME.get(i).click();
                        log.info("Group NAME is selected");
                        break;
                    }
                }

                sendTextToElement(findElement(locators.get("txt_thresholdValue")), getAlias("txt_thresholdValue"));

                baseTest.pause(2000);
                clickOnElement(locators.get("btn_add_details"));
                JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
                js.executeScript("window.scrollBy(0,-250)", "");

                baseTest.pause(2000);
            }


            clickOnElement("#btncreate");

            String Message = findElement1(locators.get("Success_message_scenario")).getText();

            if (Message.contains("Creation of Scenario is successful.")) {
                log.info("Creation of Scenario is successful.");

            }

        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Creation of Scenario is Failed");

        }

    }

    @Step
    public void ScenarioManagementAutoAction() {
        try {

            baseTest.pause(3000);
            clickOverSubMenu("Dunning", "Dunning Configuration", "Scenario Management");
            baseTest.pause(5000);

            switchToFrame1("iframe");

            baseTest.pause(5000);

            clickOnElement(locators.get("btn_Scenario_Create"));

            String ScenarioName = getAlias("txt_scenarioName") + generateRandomString();
            baseTest.pause(2000);

            WebElement Name = findElement(locators.get("txt_scenarioName"));
            sendTextToElement(Name, ScenarioName);

            baseTest.pause(2000);
            selectValueFromDropDownBox(By.cssSelector(locators.get("select_btn_currency")), By.cssSelector(locators.get("drp_dwn_currency")), "INR");

            baseTest.pause(2000);
            clickOnElement(locators.get("btn_reversal"));

            baseTest.pause(3000);
            clickOnElement(locators.get("btn_cyclicreversal"));

            String Scenarios = getAlias("number_of_scenarios");

            int iTest = Integer.parseInt(Scenarios);
            for (int j = 1; j <= iTest; j++) {

                clickOnElement(locators.get("btn_level"));

                baseTest.pause(2000);

                List<WebElement> LevelSelection = findElements1(locators.get("drp_dwn_level"));
                for (int i = 0; i < LevelSelection.size(); i++) {

                    String Level = LevelSelection.get(i).getText().trim();

                    String Scene = getAlias("Level" + j).trim();

                    if (Scene.equals(Level)) {
                        LevelSelection.get(i).click();
                        break;
                    }
                }

                scrollToamount();

                clickOnElement(locators.get("btn_action"));

                List<WebElement> Action = findElements1(locators.get("drp_dwn_Action"));
                for (int i = 0; i < Action.size(); i++) {

                    String Value = Action.get(i).getText().trim();

                    String Value_Action = getAlias("Action_" + j).trim();

                    //  String Value_Action = Action +j

                    if (Value.equals(Value_Action)) {
                        Action.get(i).click();
                        break;
                    }
                }

                scrollToamount();

                baseTest.pause(5000);

                clickOnElement(locators.get("btn_reversalAction"));

                List<WebElement> Reversal_Selection = findElements1(locators.get("drp_dwn_reversalAction"));
                for (int i = 0; i < Reversal_Selection.size(); i++) {
                    String RS = Reversal_Selection.get(i).getText();
                    String Reversal_Action = getAlias("Reactivate_" + j);
                    if (RS.equals(Reversal_Action)) {
                        Reversal_Selection.get(i).click();
                        log.info("Reversal Action is selected");
                        break;
                    }
                }

                WebElement Agingfrom = findElement(locators.get("txt_agingFrom"));
                sendTextToElement(Agingfrom, getAlias("txt_agingFrom" + j));

                WebElement AgingTO = findElement(locators.get("txt_agingTo"));
                sendTextToElement(AgingTO, getAlias("txt_agingTo" + j));

                scrollToamount();
                scrollToamount();

                baseTest.pause(2000);
                clickOnElement(locators.get("drp_dwn_staffgroupname"));

                baseTest.pause(2000);
                List<WebElement> GroupNAME = findElements1(locators.get("GroupName_values"));
                for (int i = 0; i < GroupNAME.size(); i++) {
                    String Name1 = GroupNAME.get(i).getText().trim();
                    String StaffGroup = getSavedValues("StaffGroup");
                    log.info("" + StaffGroup);
                    if (Name1.equals("Group_ikemnlqaqx")) {
                        GroupNAME.get(i).click();
                        log.info("Group NAME is selected");
                        break;
                    }
                }

                sendTextToElement(findElement(locators.get("txt_thresholdValue")), getAlias("txt_thresholdValue"));

                baseTest.pause(2000);
                clickOnElement(locators.get("btn_add_details"));
                JavascriptExecutor js = (JavascriptExecutor) browser.getDriver();
                js.executeScript("window.scrollBy(0,-250)", "");

                baseTest.pause(2000);
            }


            clickOnElement("#btncreate");

            String Message = findElement1(locators.get("Success_message_scenario")).getText();

            if (Message.contains("Creation of Scenario is successful.")) {
                log.info("Creation of Scenario is successful.");
                savedValues.put("ScenarioName", ScenarioName);
            }

        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Creation of Scenario is Failed");

        }

    }

    @Step
    public void CreditClass(String Value_Scenario) {
        try {

            baseTest.pause(2000);
            clickOverSubMenu("Dunning", "Dunning Configuration", "Credit Class Management");
            baseTest.pause(2000);

            switchToFrame1("iframe");
            clickOnElement(locators.get("btn_creditclass"));

            baseTest.pause(2000);
            WebElement txt_creditclass_Name = findElement(locators.get("txt_creditclass_Name"));
            String CreditClass_Name = getAlias("CreditClass_Name") + generateRandomString();
            sendTextToElement(txt_creditclass_Name, CreditClass_Name);

            baseTest.pause(2000);
            selectValueFromDropDownBox(By.cssSelector(locators.get("select_btn_currency")), By.cssSelector("#cmbcurrency-pp"), "INR");

            selectValueFromDropDownBox(By.cssSelector("#startdatetype_dunning_module-btn"), By.cssSelector("#startdatetype_dunning_module-pp"), "Bill Date");

            baseTest.pause(2000);
            WebElement GraceDays = findElement(locators.get("txt_graceDays"));
            sendTextToElement(GraceDays, getAlias("GraceDaysValue"));

            baseTest.pause(2000);
            selectValueFromDropDownBox(By.cssSelector(locators.get("Value_Particular")), By.cssSelector(locators.get("Click_Particular")), Value_Scenario);

            String UnitType = getAlias("ValueType");
            baseTest.pause(2000);
            selectValueFromDropDownBox(By.cssSelector(locators.get("Click_Unittype")), By.cssSelector(locators.get("Value_Unit_Type")), UnitType);

            sendTextToElement(findElement(locators.get("txt_entrythreshold")), getAlias("txt_entrythreshold"));

            sendTextToElement(findElement(locators.get("txt_exitthreshold")), getAlias("txt_exitthreshold"));

            sendTextToElement(findElement(locators.get("txt_debt")), getAlias("txt_debt"));

            clickOnElement(locators.get("cc_create"));
            baseTest.pause(2000);

            String Message = findElement1(locators.get("CC_Success_Msg")).getText();
            log.info("" + Message);

            if (Message.contains("created successfully")) {
                log.info("Credit Class created successfully");
            }

        } catch (Exception e) {
            log.info("Credit Class creation failed ");
            log.error(e.getMessage());

        }
    }

    /**
     * It will created dunning unit for manual flow achieve
     *
     * @param billingAccountNumber
     * @param invoiceNumber
     * @return
     * @Author Chetan Bhoi
     * @updated_By anuj gupta
     */
    @Step
    public String createDunningUnit(String billingAccountNumber, String invoiceNumber) {
        String response = null;
        try {
            clickOverSubMenu("Dunning", "Dunning Unit Management");
            switchToFrame1("iframe");
            clickOnElement(By.cssSelector("#tbbcreateunit-cnt"));
            sendTextToElement(By.cssSelector("#accountnumber"), billingAccountNumber);//"ELITE005192"
            sendKeysToElement(By.cssSelector("#accountnumber"), Keys.ENTER);
            pause(500);
            sendTextToElement(By.cssSelector("#phonenumber"), "12365478920");
            selectValueFromDropDownBox(By.cssSelector("#creditclasscombo-btn"), By.cssSelector("#creditclasscombo-pp"), "Auto_cc");

            scrollToamount();
            scrollToamount();

            clickOnElement(By.cssSelector("#billingbillimage"));
            switchToWindows(1);
            findElement("#txtaccountname").clear();
            clickOnElement("#btnsearch");
            clickOnElement("#searchresultlistbox-rows>tr");
            clickOnButton("Select");
            switchToWindows(0);
            pause(500);
            switchToFrame1("iframe");
            sendTextToElement(By.cssSelector("#reason"), "Test");
            clickOnButton("Create Unit");
            clickOnButton("Yes");
            pause(2000);
            response = findElement1("(//*[@class='z-messagebox z-div']/span)[2]").getText();

        } catch (Exception e) {
            log.error(e.getMessage());
            response = "Dunning Unit is not created.";
        }
        return response;
    }
}
