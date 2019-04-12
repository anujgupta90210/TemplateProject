package frameworkutils;

import org.apache.log4j.*;

/**
 * Utility Logger Class to log each event in console, html log file and a text file
 *
 * @author Anuj Gupta
 */
public class LogManager {

    private static LogManager log;
    // Initializes logger class and gives a name to the logger
    private Logger logger = Logger.getLogger(" - ");
    private ConsoleAppender consoleAppender = new ConsoleAppender();
    private String conversionPattern;

    // To prevent other classes to create object of this utility class
    private LogManager() {
    }

    public static synchronized LogManager getInstance() {

        if (log == null)
            log = new LogManager();
        return log;
    }

    /**
     * Setup method to create setup for console, HTML_LOGS_PATH & file logs
     */
    public void setup() {

        setConsoleLogs();
        setUserLogs();
        setHTMLogs();
    }// End of setup

    /**
     * Creates setup for HTML_LOGS_PATH Logs
     */
    private void setHTMLogs() {

        try {
            HTMLLayout htmlLayout = new HTMLLayout();

            // Create HTML_LOGS_PATH file where logs will be generated
            FileAppender fileAppender = new FileAppender(htmlLayout, Constants.HTML_LOGS_PATH, false);

            // Generates new logs on each run by overwriting old logs and not appending to existing ones
            fileAppender.setAppend(false);

            // Prepares the appender for use
            fileAppender.activateOptions();

            // Add newAppender to the list of appenders
            logger.addAppender(fileAppender);

            // In order to avoid this redundancy, set the additivity property of Log4j logger to false and then the log messages which are coming to that logge will not be propagated to it’s parent loggers
            logger.setAdditivity(false);

        } catch (Exception e) {
            System.err.println("Error while setting up HTML_LOGS_PATH Logs");
        }
    }// End of setHTMLogs

    /**
     * Creates setup for console Logs
     */
    private void setConsoleLogs() {

        // Sets default configuration for the console appender
        BasicConfigurator.configure();

        // Prepares the appender for use
        consoleAppender.activateOptions();

        // Add newAppender to the list of appenders
        logger.addAppender(consoleAppender);

        // Set the additivity flag to false
        logger.setAdditivity(false);
    }// End of setConsoleLogs

    /**
     * Creates setup for user Logs
     */
    @SuppressWarnings("depricated")
    private void setUserLogs() {

        try {
            freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);

            // Initialize reference variable for file appender
            FileAppender fileAppender = new FileAppender();

            // Create text log file where logs will be generated
            fileAppender.setFile(Constants.USER_LOGS_PATH);

            // Initialize reference variable for pattern layout class
            PatternLayout layout = new PatternLayout();

            // Set format for logs to be displayed in text file
            String fileConversionPattern = "%-5p %d{yyyy-MM-dd HH:mm:ss}%-1c%m %n";

            // Apply format to file
            layout.setConversionPattern(fileConversionPattern);
            fileAppender.setLayout(layout);

            // Generates new logs on each run by overwriting old logs and not appending to existing ones
            fileAppender.setAppend(false);

            // Prepares the appender for use
            fileAppender.activateOptions();

            // Add newAppender to the list of appenders
            logger.addAppender(fileAppender);

            // Set the additivity flag to false
            logger.setAdditivity(false);

        } catch (Exception e) {
           System.err.println("Error while setting up user Logs");
        }
    }// End of setUserLogs

    /**
     * Setup for Info level logs
     *
     * @param message Message to be displayed in logs
     */
    public void info(String message) {

        try {
            // Initialize reference variable for pattern layout class
            PatternLayout infoLayout = new PatternLayout();

            // Set format for info logs to be displayed
            conversionPattern = "\u001b[m%-5p %d{yyyy-MM-dd HH:mm:ss}%-1c" + getStackTrace() + "%1c%m\u001b[m%n";

            // Apply format to appender
            infoLayout.setConversionPattern(conversionPattern);
            consoleAppender.setLayout(infoLayout);

            // Prepares the appender for use
            consoleAppender.activateOptions();

            // Add newAppender to the list of appenders
            logger.addAppender(consoleAppender);

            // Append message to log
            logger.info(message);

        } catch (Exception e) {
            error("Error in info log method");
        }
    }// End of info

    /**
     * Setup for Error level logs
     *
     * @param message Message to be displayed in logs
     */
    public void error(String message) {

        try {
            // Initialize reference variable for pattern layout class
            PatternLayout errorLayout = new PatternLayout();

            // Set format for info logs to be displayed
            conversionPattern = "\u001b[31;1m%-5p %d{yyyy-MM-dd HH:mm:ss}%-1c" + getStackTrace() + "%1c%m\u001b[m%n";

            // Apply format to file
            errorLayout.setConversionPattern(conversionPattern);
            consoleAppender.setLayout(errorLayout);

            // Prepares the appender for use
            consoleAppender.activateOptions();

            // Add newAppender to the list of appenders
            logger.addAppender(consoleAppender);

            // Append message to log
            logger.error(message);

        } catch (Exception e) {
            System.err.println("Error in error log method");
        }
    }// End of error

    /**
     * Setup for Warn level logs
     *
     * @param message Message to be displayed in logs
     */
    public void warn(String message) {

        try {
            // Initialize reference variable for pattern layout class
            PatternLayout infoLayout = new PatternLayout();

            // Set format for info logs to be displayed
            conversionPattern = "\u001b[36;1m%-5p %d{yyyy-MM-dd HH:mm:ss}%-1c" + getStackTrace() + "%1c%m\u001b[m%n";

            // Apply format to appender
            infoLayout.setConversionPattern(conversionPattern);
            consoleAppender.setLayout(infoLayout);

            // Prepares the appender for use
            consoleAppender.activateOptions();

            // Add newAppender to the list of appenders
            logger.addAppender(consoleAppender);

            // Append message to log
            logger.warn(message);

        } catch (Exception e) {
            error("Error in warn log method");
        }
    }// End of warn

    /**
     * Setup for Debug level logs
     *
     * @param message Message to be displayed in logs
     */
    public void debug(String message) {

        try {
            // Initialize reference variable for pattern layout class
            PatternLayout infoLayout = new PatternLayout();

            // Set format for info logs to be displayed
            conversionPattern = "\u001b[32;1m%-5p %d{yyyy-MM-dd HH:mm:ss}%-1c" + getStackTrace() + "%1c%m\u001b[m%n";

            // Apply format to appender
            infoLayout.setConversionPattern(conversionPattern);
            consoleAppender.setLayout(infoLayout);

            // Prepares the appender for use
            consoleAppender.activateOptions();

            // Add newAppender to the list of appenders
            logger.addAppender(consoleAppender);

            // Append message to log
            logger.debug(message);

        } catch (Exception e) {
            error("Error in debug log method");
        }
    }// End of debug

    /**
     * Setup for Fatal level logs
     *
     * @param message Message to be displayed in logs
     */
    public void fatal(String message) {

        try {
            // Initialize reference variable for pattern layout class
            PatternLayout infoLayout = new PatternLayout();

            // Set format for info logs to be displayed
            conversionPattern = "\u001b[34;1m%-5p %d{yyyy-MM-dd HH:mm:ss}%-1c" + getStackTrace() + "%1c%m\u001b[m%n";

            // Apply format to appender
            infoLayout.setConversionPattern(conversionPattern);
            consoleAppender.setLayout(infoLayout);

            // Prepares the appender for use
            consoleAppender.activateOptions();

            // Add newAppender to the list of appenders
            logger.addAppender(consoleAppender);

            // Append message to log
            logger.fatal(message);

        } catch (Exception e) {
            error("Error in fatal log method");
        }
    }// End of fatal

    /**
     * Setup to display class name, method name, & line number of the calling method in logs
     */
    private String getStackTrace() {

        try {
            // Stores class name along with its packagae name
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();

            // Stores just the class name
            String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

            // Stores method name under execution
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();

            // Stores line number under execution
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            // Stores class, method and line number in a particular format
            return "(" + className + "." + methodName + ":" + lineNumber + ")";

        } catch (Exception e) {
            error("Error in getStackTrace method");
        }
        return "";
    }// End of getStackTrace
}
