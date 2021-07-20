package Model;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeConversion {
    private static String dateFormat = "MM-dd-yyyy";
    private static String timeFormat = "HH:mm";
    private static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

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

    public static String convertLocalDateLocalTimetoUTCString(LocalDate ld, LocalTime lt) {
        LocalDateTime ldt = LocalDateTime.of(ld, lt);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        LocalDateTime utcLDT = zdt.toLocalDateTime();
        DateTimeFormatter dtfDateTime = DateTimeFormatter.ofPattern(dateTimeFormat);
        return utcLDT.format(dtfDateTime);
    }
}
