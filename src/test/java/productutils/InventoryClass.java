package productutils;

import frameworkutils.BrowserFactory;
import frameworkutils.LogManager;
import io.qameta.allure.Step;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class InventoryClass extends BaseTest {

    private LogManager log = LogManager.getInstance();
    private BrowserFactory browser = BrowserFactory.getInstance();

    @Step
    public String createGroup(String groupName) {
        String response = null;
        try {
            clickOverSubMenu("Inventory", "Inventory Configuration", "Group Management");
            Thread.sleep(1000);
            waitUntilVisible(By.tagName("iframe"));
            switchToFrame1("iframe");
            waitUntilVisible(By.cssSelector(locators.get("inventory.createGroup.tab.css")));
            clickOnElement(locators.get("inventory.createGroup.tab.css"));
            sendTextToElement(By.cssSelector(locators.get("inventory.groupname.txtbox.css")), groupName);
            sendTextToElement(By.cssSelector(locators.get("inventory.groupaliase.txtbox.css")), "test");
            sendTextToElement(By.cssSelector("#descriptiontext"), "test");
            clickOnButton("Create");
            waitUntilVisible(By.xpath("//*[starts-with(@class,'z-messagebox-window')]"));
            WebElement mssgbox = browser.getDriver().findElement(By.xpath("//*[starts-with(@class,'z-messagebox-window')]"));
            response = mssgbox.findElement(By.tagName("span")).getText();

        } catch (Exception e) {
            response = null;
            log.error(e.getMessage());
        }
        return response;
    }

    @Step
    public String createSubGroup(String groupName, String subGroupName) {

        String response = null;
        try {
            Thread.sleep(500);
            clickOverSubMenu("Inventory", "Inventory Configuration", "Sub Group Management");
            Thread.sleep(1000);
            waitUntilVisible(By.tagName("iframe"));
            switchToFrame1("iframe");
            waitUntilVisible(By.cssSelector(locators.get("inventory.createSubGroup.tab.css")));
            clickOnElement(locators.get("inventory.createSubGroup.tab.css"));

            sendTextToElement(By.cssSelector(locators.get("inventory.subgroupname.txtbox.css")), subGroupName);
            sendTextToElement(By.cssSelector(locators.get("inventory.subgroupAliasName.txtbox.css")), subGroupName);

            selectValueFromDropDownBox(By.cssSelector("#inventorgroupcb-btn"), By.cssSelector("#inventorgroupcb-pp"), groupName);

            sendTextToElement(By.cssSelector(locators.get("inventory.subgroupMinLen.txtbox.css")), "5");
            sendTextToElement(By.cssSelector(locators.get("inventory.subgroupMaxLen.txtbox.css")), "10");

            sendTextToElement(By.cssSelector(locators.get("inventory.subgroupDesc.txtarea.css")), "test");
            clickOnButton("Create");
            WebElement msgbox = waitUntilVisible(By.cssSelector("#createinventorysubtyperesponsediv"));
            response = msgbox.findElement(By.tagName("span")).getText();
        } catch (Exception e) {
            response = null;
            log.info(e.getMessage());
        }
        return response;
    }

    /**
     * Creates a new warehouse
     *
     * @param warehouseEmail   Email of the warehouse
     * @param warehouseCountry Country in which warehouse is located
     * @param warehouseState   State in which warehouse is located
     * @param warehouseCity    City in which warehouse is located
     * @author anuj gupta
     */
    @Step
    public String createWareHouse(String warehouseName, String warehouseEmail, String warehouseCountry, String warehouseState, String warehouseCity) {

        String message = "";
        try {

            pause(500);
            clickOverSubMenu("Inventory", "Inventory Configuration", "Warehouse Management");

            pause(500);
            switchToFrame(browser.getDriver().findElement(By.cssSelector(locators.get("split_charge_frame_class"))));
            clickElement("#createinventorywarehouse-cave");

            sendTextToElement(By.cssSelector(locators.get("warehouseName")), warehouseName);
            sendTextToElement(By.cssSelector(locators.get("warehouseEmail")), warehouseEmail);
            selectValueFromDropDownBox(By.cssSelector(locators.get("warehouseCountryDrpdwnBtn")), By.cssSelector(locators.get("warehouseCountryDrpdwnBox")), warehouseCountry);
            selectValueFromDropDownBox(By.cssSelector(locators.get("warehouseStateDrpdwnBtn")), By.cssSelector(locators.get("warehouseStateDrpdwnBox")), warehouseState);
            selectValueFromDropDownBox(By.cssSelector(locators.get("warehouseCityDrpdwnBtn")), By.cssSelector(locators.get("warehouseCityDrpdwnBox")), warehouseCity);
            clickOnButton("Create");

            WebElement msgbox = waitUntilVisible(By.xpath("//div[@id='createwarehouseresponsediv']/span"));
            message = msgbox.getText();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return message;
    }

    /**
     * Generates a new inventory batch
     *
     * @param inventoryGroupName    Name of the inventory group
     * @param inventorySubGroupName Name of the inventory sub group
     * @param warehosueName         Name of the warehosue
     * @author anuj gupta
     */
    @Step
    public String generateInventoryBatch(String warehouseName, String filePath, String inventoryGroupName, String inventorySubGroupName, String warehosueName) {

        String batchName = "";
        try {
            // Open inventory batch management
            pause(500);
            clickOverSubMenu("Inventory", "Inventory Batch Management");

            pause(500);
            switchToFrame(browser.getDriver().findElement(By.cssSelector(locators.get("split_charge_frame_class"))));

            // Switch to generate inventory batch tab
            clickElement("#generatebatchtab-cave");

            // Enter details to create inventory batch
            selectValueFromDropDownBox(By.cssSelector(locators.get("inventoryGroupDrpdwnBtn")), By.cssSelector(locators.get("inventoryGroupDrpdwnBox")), inventoryGroupName);
            selectValueFromDropDownBox(By.cssSelector(locators.get("inventorySubGroupDrpdwnBtn")), By.cssSelector(locators.get("inventorySubGroupDrpdwnBox")), inventorySubGroupName);
            selectValueFromDropDownBox(By.cssSelector(locators.get("inventoryWarehouseNameDrpdwnBtn")), By.cssSelector(locators.get("inventoryWarehouseNameDrpdwnBox")), warehouseName);

            // Store minimum inventory range value
            String inventoryRange = getTextFromElement(By.cssSelector("#configdisp"));
            int minInventoryRange = Integer.parseInt(StringUtils.substringBetween(inventoryRange, "Min Postfix Length : ", ","));

            // Generates random number of length 'minInventoryRange'
            long min = createRandomNumber(minInventoryRange);
            long max = min + 10;

            for (int i = 1; i < 11; i++) {
                long data = min + i;
                writeToCSV(filePath, Long.toString(data), i, 1);
            }

            sendTextToElement(By.cssSelector(locators.get("inventoryGroupFromRange")), Long.toString(min));
            sendTextToElement(By.cssSelector(locators.get("inventoryGroupToRange")), Long.toString(max));

            // Click on generate button
            clickOnButton("Generate");

            pause(500);

            // Store batch name generated
            String batch = getTextFromElement(By.xpath("//p[contains(text(),'Batch Number')]"));
            batchName = StringUtils.substring(batch, 14);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return batchName;
    }

    /**
     * View inventory details generated for the given batch
     *
     * @param batchNumber           Batch Number for which inventory details needs to be viewed
     * @param inventoryGroupName    Inventory group dropdown value
     * @param inventorySubGroupName Inventory subgroup dropdown value
     * @author anuj gupta
     */
    @Step
    public String viewInventoryDetails(String batchNumber, String inventoryGroupName, String inventorySubGroupName) {

        String status = "";
        try {
            // Open inventory batch management
            pause(500);
            clickOverSubMenu("Inventory", "Inventory Batch Management");

            pause(500);
            switchToFrame(browser.getDriver().findElement(By.cssSelector(locators.get("split_charge_frame_class"))));

            // Enter mandatory details
            sendTextToElement(By.cssSelector(locators.get("batchNumber")), batchNumber);
            selectValueFromDropDownBox(By.cssSelector(locators.get("searchInventoryGroupDrpdwnBtn")), By.cssSelector(locators.get("searchInventoryGroupDrpdwnBox")), inventoryGroupName);
            selectValueFromDropDownBox(By.cssSelector(locators.get("searchInventorySubGroupDrpdwnBtn")), By.cssSelector(locators.get("searchInventorySubGroupDrpdwnBox")), inventorySubGroupName);

            // Click on search button
            clickOnButton("Search");

            pause(500);
            // Click on view details button
            clickOnElement("#indexes1");

            pause(500);
            // click on view Inventory details button
            clickOnElement("#inventorydtl");

            status = getTextFromElement(By.xpath("(//tbody[@id='listinventorydetail-rows']//span)[4]"));

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return status;
    }
}
