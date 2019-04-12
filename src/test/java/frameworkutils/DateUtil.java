package frameworkutils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Date utility to handle date methods
 *
 * @author anuj gupta
 */
public class DateUtil {

    private DateUtil date;
    private LogManager log = LogManager.getInstance();

    private DateUtil() {

    }

    /**
     * Singleton method to create only a single instance of the Date class
     *
     * @return Object of Date class
     */
    public DateUtil getInstance() {
        if (date == null)
            date = new DateUtil();

        return date;
    }

    /**
     * Adds or subtracts the integer value from current date
     *
     * @param format       Contains format of date e.g. [mm/dd/yyyy]
     * @param numberOfDays Number of days to add or remove from current date
     * @return date in format
     */
    public String getDateByDayOffset(String format, int numberOfDays) {

        try {
            // Set the format of date
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            // Creates instance of Calendar class
            Calendar cal = Calendar.getInstance();

            // Adds 'numberOfDays' to current date
            cal.add(Calendar.DATE, numberOfDays);

            // Returns new formatted date
            return dateFormat.format(cal.getTime());

        } catch (Exception e) {
            log.error("Unable to fetch date");
            return null;
        }
    }

    /**
     * Adds or subtracts the integer value from current month
     *
     * @param format         Contains format of date e.g. [mm/dd/yyyy]
     * @param numberOfMonths Number of months to add or remove from current date
     * @return date
     */
    public String getDateByMonthOffset(String format, int numberOfMonths) {

        try {
            // Set the format of date
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            // Creates instance of Calendar class
            Calendar cal = Calendar.getInstance();

            // Adds 'numberOfDays' to current date
            cal.add(Calendar.MONTH, numberOfMonths);

            // Returns new formatted date
            return dateFormat.format(cal.getTime());

        } catch (Exception e) {
            log.error("Unable to fetch date");
            return null;
        }
    }
}
