package eu.gs.gslibrary.utils.utility;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * It returns a string of the current date and time, yesterday's date, tomorrow's date, plus a number of days, or minus a
 * number of days, in the format you specify
 */
@UtilityClass
public class DateUtils {

    /**
     * It returns the current date and time in the format specified by the format parameter.
     *
     * @param format The format of the date you want to return.
     * @return A string representation of the current date and time.
     */
    public static String today(String format) {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }

    /**
     * It returns a string of yesterday's date in the format you specify
     *
     * @param format The format of the date you want to return.
     * @return A string representation of the date in the format specified.
     */
    public static String yesterday(String format) {
        LocalDateTime date = LocalDateTime.now().minusDays(1);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }

    /**
     * Return the date tomorrow in the format specified by the format parameter.
     *
     * @param format The format of the date you want to return.
     * @return A string representation of the date in the format specified.
     */
    public static String tomorrow(String format) {
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }

    /**
     * It takes in a number of days and a format string, and returns a string of the date in the specified format, plus the
     * number of days
     *
     * @param days The number of days to add to the current date.
     * @param format The format of the date you want to return.
     * @return A string representation of the date in the format specified.
     */
    public static String plusDays(int days, String format) {
        LocalDateTime date = LocalDateTime.now().plusDays(days);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }

    /**
     * It returns a string of the current date minus the number of days you pass in, formatted as the format you pass in
     *
     * @param days The number of days to subtract from the current date.
     * @param format The format of the date you want to return.
     * @return A string representation of the date in the format specified.
     */
    public static String minusDays(int days, String format) {
        LocalDateTime date = LocalDateTime.now().minusDays(days);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }
}
