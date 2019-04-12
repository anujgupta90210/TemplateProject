package productutils;

import frameworkutils.BrowserFactory;
import frameworkutils.LogManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Usage: Calendar Class to select any date.
 * Assumption: All calendars in the website opens from current date.
 * <p>
 * Implementation:
 * All methods are static so call methods using className eg: Cal.setDate().
 *
 * @author Anuj Gupta
 */
public class CommonUtils {

    private static File file;
    private static FileInputStream fis;
    private static FileOutputStream fos;
    private static Workbook wb = null;
    private static Sheet sheet;
    // Object of baseTest class to use click and wait methods
    // Stores day to be selected
    private static int targetDay = 0;
    // Stores month to be selected
    private static int targetMonth = 0;
    // Stores year to be selected
    private static int targetYear = 0;
    // Stores current month of the year
    private static int currentMonth = 0;
    // Stores current year
    private static int currentYear = 0;
    // Stores how many times next or previous icon needs to be clicked in navigate to correct month & year.
    private static int jumpMonthsBy = 0;
    // Boolean used to check if navigation is needed for next month or previous month
    private static boolean increment = true;
    private static BaseTest bt = BaseTest.getInstance();
    private static LogManager log = LogManager.getInstance();
    private static BrowserFactory browser = BrowserFactory.getInstance();

    private CommonUtils() {
        throw new IllegalStateException();
    }

    /**
     * Stores value of current month & year
     */
    private static void getCurrentMonthYear() {

        Calendar cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH) + 1;
        currentYear = cal.get(Calendar.YEAR);
    }

    /**
     * Stores value of target date, month & year
     *
     * @param targetDate date to be selected in the calendar
     */
    private static void getTargetDateMonthYear(String targetDate) {

        String[] dates = targetDate.split("/");
        targetDay = Integer.parseInt(dates[0]);
        targetMonth = Integer.parseInt(dates[1]);
        targetYear = Integer.parseInt(dates[2]);
    }

    /**
     * Calculates now many times next or previous month icon needs to be clicked
     */
    private static void calculateMonthToJump() {

        int targetMonths = targetYear * 12 + targetMonth;
        int currentMonths = currentYear * 12 + currentMonth;

        if (targetMonths - currentMonths > 0) {
            jumpMonthsBy = targetMonths - currentMonths;
        } else {
            jumpMonthsBy = currentMonths - targetMonths;
            increment = false;
        }
    }

    /**
     * Sets target date in the calendar
     *
     * @param targetDate date to be selected in the calendar
     */
    public static void setDate(By openCal, String targetDate) {

        try {
            getCurrentMonthYear();
            getTargetDateMonthYear(targetDate);
            calculateMonthToJump();

            // Opens the calendar
            bt.clickOnElement(openCal);
            //bt.clickElement("#effectdate-btn");

            for (int i = 0; i < jumpMonthsBy; i++) {

                if (increment) {
                    Thread.sleep(500);
                    bt.clickElement("#_z_0-right");

                } else {
                    Thread.sleep(500);
                    bt.clickElement("#_z_0-left");
                }
            }
            Thread.sleep(500);
            bt.clickonElement1("(//td[text()='" + targetDay + "'])");

        } catch (Exception e) {
            System.err.println("Unable to select the date in calendar");
        }
    }

    public static void sendDate(By dateInputTagElement, String targetDate) {
        try {
            bt.pause(200);
            List<WebElement> inputs = bt.findElements(dateInputTagElement);

            for (WebElement input : inputs) {
                ((JavascriptExecutor) browser.getDriver()).executeScript(
                        "arguments[0].removeAttribute('readonly','readonly')", input);
            }
            browser.getDriver().findElement(dateInputTagElement).clear();
            browser.getDriver().findElement(dateInputTagElement).sendKeys(targetDate);
            bt.pause(200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds or subtracts the integer value from current date
     *
     * @param numberOfDays Number of days to add or remove from current date
     * @return date in format
     * @author anuj gupta
     */
    public static String getDateByDayOffset(int numberOfDays) {

        try {
            String format = "dd-MM-YYYY";
            // Set the format of date
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            // Creates instance of Calendar class
            Calendar cal = Calendar.getInstance();

            // Adds 'numberOfDays' to current date
            cal.add(Calendar.DATE, numberOfDays);

            // Returns new formatted date
            return dateFormat.format(cal.getTime());

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Adds or subtracts the integer value from current month
     *
     * @param numberOfMonths Number of months to add or remove from current date
     * @return date
     * @author anuj gupta
     */
    public static String getDateByMonthOffset(int numberOfMonths) {

        try {
            String format = "dd-MM-YYYY";
            // Set the format of date
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            // Creates instance of Calendar class
            Calendar cal = Calendar.getInstance();

            // Adds 'numberOfDays' to current date
            cal.add(Calendar.MONTH, numberOfMonths);

            // Returns new formatted date
            return dateFormat.format(cal.getTime());

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String getRandomString(int length) {
        String randomString = null;
        try {
            char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            StringBuilder sb = new StringBuilder(20);
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            randomString = sb.toString().toUpperCase();
            System.out.println(randomString);
        } catch (Exception e) {
            e.getMessage();
        }
        return randomString;
    }

    public static int getRandomNumber() {
        int min = 10;
        int max = 99;
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static String getVoucherPin() {
        String voucherPin = null;
        try {
            String filePath = System.getProperty("user.dir") + "/src/main/resources/brm-ui2.xlsx";
            String sheetName = "VoucherPin";
            readSheet(filePath, sheetName);
            int rowVal = (int) sheet.getRow(1).getCell(1).getNumericCellValue();
            voucherPin = getCellValue(rowVal, 0);
            updateCellValue(1, 1, rowVal + 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return voucherPin;
    }

    private static void readSheet(String filePath, String sheetName) {
        try {
            file = new File(filePath);
            fis = new FileInputStream(file);
            if (filePath.contains(".xlsx")) {
                wb = new XSSFWorkbook(fis);
            } else if (filePath.contains(".xls")) {
                wb = new HSSFWorkbook(fis);
            }
            sheet = wb.getSheet(sheetName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCellValue(int rowNo, int cellNo) {
        String cellValue = null;
        cellValue = sheet.getRow(rowNo).getCell(cellNo).getStringCellValue();
        return cellValue;
    }

    private static void updateCellValue(int rowNo, int cellNo, int rowVal) {
        try {
            Row row = sheet.getRow(rowNo);
            Cell cell = row.getCell(cellNo);
            cell.setCellValue(rowVal);

            fos = new FileOutputStream(file);
            wb.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}