package eu.gs.gslibrary.utils.api;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtils {

    /**
     * @return 	- Today's date with given format.
     */
    public static String today(String format) {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }

    /**
     * @return 	- Yesterday's date with given format.
     */
    public static String yesterday(String format) {
        LocalDateTime date = LocalDateTime.now().minusDays(1);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }

    /**
     * @return 	- Tomorrow's date with given format.
     */
    public static String tomorrow(String format) {
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }

    public static String plusDays(int days, String format) {
        LocalDateTime date = LocalDateTime.now().plusDays(days);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }

    public static String minusDays(int days, String format) {
        LocalDateTime date = LocalDateTime.now().minusDays(days);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(timeFormatter);
    }
}
