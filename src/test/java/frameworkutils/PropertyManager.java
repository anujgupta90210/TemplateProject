package frameworkutils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Utility class to load and fetch value from any given property file
 *
 * @author Anuj Gupta
 */
public class PropertyManager {

    private static PropertyManager property;
    private LogManager log = LogManager.getInstance();
    private Properties prop = new Properties();

    // To prevent other classes to create of this utility class
    private PropertyManager() {
    }

    public static PropertyManager getInstance() {

        if (property == null)
            property = new PropertyManager();
        return property;
    }

    /**
     * Fetch property from config.properties file
     *
     * @param fileName Name of the property file
     * @param property Property whose value is required
     * @return value of property received as parameter
     */
    public String getProperty(String fileName, String property) {

        String value = null;
        try {
            // Initialize reference variable of FileInputStream class
            FileInputStream fis = new FileInputStream(Constants.PROPERTY_FILE_PATH + fileName + ".properties");
            prop.load(fis);
            value = prop.getProperty(property);

            if (value == null) {
                log.error(property + " property missing from " + fileName + ".properties file");
                return null;
            }
        } catch (Exception e) {
            log.error("Unable to load property: " + property + " from the property file: " + fileName);
        }
        return value;
    }
}
