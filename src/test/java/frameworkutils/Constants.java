package frameworkutils;

/**
 * Constant class to store all constants of the project
 *
 * @author anuj gupta
 */
public class Constants {

    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/test/resources/";
    public static final String PROPERTY_FILE_PATH = BASE_PATH + "/properties/";
    public static final String EXCEL_PATH = BASE_PATH + "data/";
    public static final String USER_LOGS_PATH = BASE_PATH + "logs/user.log";
    public static final String HTML_LOGS_PATH = BASE_PATH + "logs/user.html";
    public static final String REPORTER_PATH = BASE_PATH + "report/";
    public static final String ARCHIVE_REPORT_PATH = BASE_PATH + "archive";
    public static final String UPLOAD_FILE_PATH = BASE_PATH + "filesToUpload/";
    public static final String DRIVERS_PATH = BASE_PATH + "drivers/";

    private Constants() {
    }
}
