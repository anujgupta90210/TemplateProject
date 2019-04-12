package frameworkutils;

import io.qameta.allure.Step;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Manage test data via excel file
 *
 * @author anuj gupta
 */
public class ExcelManager {

    private static ExcelManager excel;
    private Workbook workbook;
    private Sheet sheet;
    private FileInputStream fis;
    private FileOutputStream fos;
    private Cell cell;
    private LogManager log = LogManager.getInstance();
    private PropertyManager prop = PropertyManager.getInstance();
    private String workBookPath = Constants.EXCEL_PATH + prop.getProperty("config", "workbookName");

    private ExcelManager() {
    }

    public static ExcelManager getInstance() {

        if (excel == null)
            excel = new ExcelManager();
        return excel;
    }

    /**
     * Opens the file input stream
     */
    public void openInputStream() {

        try {
            fis = new FileInputStream(workBookPath);

            if (workBookPath.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else {
                workbook = new HSSFWorkbook(fis);
            }

        } catch (Exception e) {
            log.error("Unable to open file input stream");
        }
    }

    /**
     * Closes the file input stream
     */
    public void closeInputStream() {

        try {
            fis.close();

        } catch (Exception e) {
            log.error("Unable to close file input stream");
        }
    }

    /**
     * Opens the file output stream
     */
    public void openOutputStream() {

        try {
            fos = new FileOutputStream(workBookPath);

        } catch (Exception e) {
            log.error("Unable to open file output stream");
        }
    }

    /**
     * Closes the file output stream
     */
    public void closeOutputStream() {

        try {
            fos.close();

        } catch (Exception e) {
            log.error("Unable to close file output stream");
        }
    }

    /**
     * Returns no. of columns in the excel sheet
     *
     * @param sheetName Name of excel sheet
     * @return Number of columns in excel
     */
    public Integer getColumnCount(String sheetName) {

        int columnCount = 0;
        try {
            // Get sheet with the given name
            sheet = workbook.getSheet(sheetName);

            // Stores the last cell number of the first row in sheet
            columnCount = (int) sheet.getRow(0).getLastCellNum();

        } catch (Exception e) {
            log.error("Unable to get the column count for sheet: " + sheetName);
        }
        return columnCount;
    }

    /**
     * Returns no. of rows in the excel sheet
     *
     * @param sheetName Name of excel sheet
     * @return Number of rows in excel
     */
    public Integer getRowCount(String sheetName) {

        int rowCount = 0;
        try {
            // Get sheet with the given name
            sheet = workbook.getSheet(sheetName);
            rowCount = (sheet.getLastRowNum() + 1);

        } catch (Exception e) {
            log.error("Unable to get the row count for sheet: " + sheetName);
        }
        return rowCount;
    }

    /**
     * Returns value of a particular cell in excel sheet
     *
     * @param sheetName  Name of excel sheet
     * @param rowNumber  Row no. from which data needs to be fetched
     * @param columnName Column name from which data needs to be fetched
     * @return value of cell in string format
     */
    public String getCellData(String sheetName, int rowNumber, String columnName) {

        String data = "";
        try {
            // Get sheet with the given name
            sheet = workbook.getSheet(sheetName);

            // Stores count of columns present in the sheet
            int columnCount = getColumnCount(sheetName);
            int columnNumber = 0;

            // Loop though all the columns in first row and find index of column with given name
            for (int i = 0; i < columnCount; i++) {
                cell = sheet.getRow(0).getCell(i);

                // Store name of each cell in first row
                String name = cell.getStringCellValue();

                // If cell name matches the column name, set columnNumber to index at which column is found
                if (name.equals(columnName)) {
                    columnNumber = i;
                    break;
                }
            }
            // Stores the cell at the given index
            cell = sheet.getRow(rowNumber).getCell(columnNumber);

            // Checks if cell value if of type boolean
            if (cell.getCellType() == CellType.BOOLEAN)
                data = String.valueOf(cell.getBooleanCellValue());

            // Checks if cell value if of type numeric
            if (cell.getCellType() == CellType.NUMERIC) {
                DataFormatter formatter = new DataFormatter();
                data = formatter.formatCellValue(cell);
            }

            // Checks if cell value if of type string
            if (cell.getCellType() == CellType.STRING)
                data = cell.getStringCellValue();

            if (cell == null || cell.getCellType() == CellType.BLANK || cell.getStringCellValue().isEmpty())
                data = "";

        } catch (Exception e) {
            log.error("Unable to get cell data for sheet: " + sheetName + " at row: " + rowNumber + " at column: " + columnName);
        }
        return data;
    }

    /**
     * Returns value of a particular cell in excel sheet
     *
     * @param sheetName    Name of excel sheet
     * @param rowNumber    Row no. from which data needs to be fetched
     * @param columnNumber Column no. from which data needs to be fetched
     * @return value of cell in string format
     */
    public String getCellData(String sheetName, int rowNumber, int columnNumber) {

        String data = "";
        try {
            // Get sheet with the given name
            sheet = workbook.getSheet(sheetName);

            // Stores the cell at the given index
            cell = sheet.getRow(rowNumber).getCell(columnNumber);

            if (cell == null)
                data = "";

                // Checks if cell value if of type boolean
            else if (cell.getCellType() == CellType.BOOLEAN)
                data = String.valueOf(cell.getBooleanCellValue());

                // Checks if cell value if of type numeric
            else if (cell.getCellType() == CellType.NUMERIC) {
                DataFormatter formatter = new DataFormatter();
                data = formatter.formatCellValue(cell);
            }

            // Checks if cell value if of type string
            else if (cell.getCellType() == CellType.STRING)
                data = cell.getStringCellValue();

        } catch (Exception e) {
            log.error("Unable to get cell data for sheet: " + sheetName + " at row: " + rowNumber + " at column: " + columnNumber);
        }
        return data;
    }

    /**
     * Returns data of entire row in the form of a HashMap
     *
     * @param sheetName name of the sheet whose data is being fetched
     * @param rowNumber number of the row from which data is being fetched
     * @return Data of the entire row in a Map
     */
    public Map<String, String> getRowAsMap(String sheetName, int rowNumber) {

        // Stores data of each row
        Map<String, String> rowData = new HashMap<>();

        try {
            openInputStream();
            int columnCount = getColumnCount(sheetName);

            for (int i = 0; i < columnCount; i++)
                rowData.put(getCellData(sheetName, 0, i), getCellData(sheetName, rowNumber, i));

        } catch (Exception e) {
            log.error("Unable to get row data for sheet: " + sheetName + " at row: " + rowNumber);

        } finally {
            closeInputStream();
        }
        return rowData;
    }

    /**
     * Returns column as a map
     *
     * @param sheetName   name of the sheet whose data is being fetched
     * @param keyColumn   column number as key of the map
     * @param valueColumn column number as value of the map
     * @return Data of the entire column in a Map
     */
    public Map<String, String> getColumnAsMap(String sheetName, int keyColumn, int valueColumn) {

        Map<String, String> columnData = new HashMap<>();
        try {
            openInputStream();
            int rowCount = getRowCount(sheetName);

            for (int i = 0; i < rowCount; i++)
                columnData.put(getCellData(sheetName, i, keyColumn), getCellData(sheetName, i, valueColumn));

        } catch (Exception e) {
            log.error("Unable to get column as a map");

        } finally {
            closeInputStream();
        }
        return columnData;
    }

    /**
     * Stores data of an excel sheet in a Java Map collection
     *
     * @param sheetName Name of the sheet whose data is being fetched
     * @return Data of the entire sheet in a Map
     */
    public Map<Integer, Map<String, String>> getSheetAsMap(String sheetName) {

        // Stores data of the entire sheet
        Map<Integer, Map<String, String>> sheetData = new HashMap<>();
        try {
            openInputStream();
            // Stores data of each row
            Map<String, String> rowData = new HashMap<>();

            int rows = getRowCount(sheetName);
            int columns = getColumnCount(sheetName);

            // Stores all column names
            List<String> columnNames = new LinkedList<>();

            // Loop to add column names in a list
            for (int i = 0; i < columns; i++)
                columnNames.add(getCellData(sheetName, 0, i));

            // Loop to store data of the entire sheet
            for (int i = 1; i < rows; i++) {
                for (int j = 0; j < columnNames.size(); j++) {
                    rowData.put(columnNames.get(j), getCellData(sheetName, i, j));
                }
                sheetData.put(i, rowData);
                rowData = new HashMap<>();
            }
        } catch (Exception e) {
            log.error("Unable to get the sheet data for sheet: " + sheetName);
        } finally {
            closeInputStream();
        }
        return sheetData;
    }

    /**
     * Sets value of a particular cell in excel sheet
     *
     * @param sheetName  Name of excel sheet
     * @param rowNumber  Row no. at which data needs to be set
     * @param columnName Column name at which data needs to be set
     * @param value      Value to set in the cell
     */
    public void setCellData(String sheetName, int rowNumber, String columnName, Object value) {

        try {
            openInputStream();

            // Get sheet with the given name
            sheet = workbook.getSheet(sheetName);

            // Stores count of columns present in the sheet
            int columnCount = getColumnCount(sheetName);
            int columnNumber = 0;

            // Loop though all the columns in first row and find index of column with given name
            for (int i = 0; i < columnCount; i++) {
                cell = sheet.getRow(0).getCell(i);

                // Store name of each cell in first row
                String name = cell.getStringCellValue();

                // If cell name matches the column name, set columnNumber to index at which column is found
                if (name.equals(columnName)) {
                    columnNumber = i;
                    break;
                }
            }
            // Stores the cell at the given index
            cell = sheet.getRow(rowNumber).getCell(columnNumber);
            cell.setCellValue(value.toString());
            closeInputStream();

            openOutputStream();
            workbook.write(fos);

        } catch (Exception e) {
            log.error("Unable to set cell data: " + value + " in sheet: " + sheetName + " at row: " + rowNumber + " at column: " + columnName);
        } finally {
            closeOutputStream();
        }
    }

    /**
     * Sets value of a particular cell in excel sheet
     *
     * @param sheetName    Name of excel sheet
     * @param rowNumber    Row no. at which data needs to be set
     * @param columnNumber Column number at which data needs to be set
     * @param value        Value to set in the cell
     */
    @Step
    public void setCellData(String sheetName, int rowNumber, int columnNumber, Object value) {

        try {
            openInputStream();

            sheet = workbook.getSheet(sheetName);
            cell = sheet.getRow(rowNumber).getCell(columnNumber);
            cell.setCellValue(value.toString());
            closeInputStream();
            openOutputStream();
            workbook.write(fos);

        } catch (Exception e) {
            log.error("Unable to set cell data: " + value + " in sheet: " + sheetName + " at row: " + rowNumber + " at column: " + columnNumber);
        } finally {
            closeOutputStream();
        }
    }
}
