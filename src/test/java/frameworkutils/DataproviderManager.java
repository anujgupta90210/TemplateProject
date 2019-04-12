package frameworkutils;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Returns a HashMap for test data. To use data provider, the @Test method name should be same as the excel sheet name
 *
 * @author anuj gupta
 */
public class DataproviderManager {

    private LogManager log = LogManager.getInstance();
    private ExcelManager excel = ExcelManager.getInstance();

    @DataProvider(name = "dp")
    public Object[][] dataProvider(Method m) {

        Object[][] data = null;
        try {
            int rowCount = excel.getRowCount(m.getName()) - 1;
            int colCount = excel.getColumnCount(m.getName());

            data = new Object[rowCount][1];

            for (int i = 0; i < rowCount; i++) {
                Map<String, String> map = new HashMap<>();

                for (int j = 0; j < colCount; j++) {
                    map.put(excel.getCellData(m.getName(), 0, j), excel.getCellData(m.getName(), i + 1, j));
                }
                data[i][0] = map;
            }

        } catch (Exception e) {
            log.error("Error in data provider method");
        }
        return data;
    }
}
