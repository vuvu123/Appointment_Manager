package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConversion {
    private static String dateFormat = "MM-dd-yyyy";
    private static String timeFormat = "HH:mm";

    public static String convertZDTtoStringLocalDate(ZonedDateTime zdt) {
        LocalDateTime ldt = zdt.toLocalDateTime();
        LocalDate ld = ldt.toLocalDate();
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern(dateFormat);
        return ld.format(dtfDate);
    }

    public static String convertZDTtoStringLocalTime(ZonedDateTime zdt) {
        LocalDateTime ldt = zdt.toLocalDateTime();
        LocalTime lt = ldt.toLocalTime();
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern(timeFormat);
        return lt.format(dtfTime);
    }
}
