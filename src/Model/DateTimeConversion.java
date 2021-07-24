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

    public static LocalDate convertZDTtoLocalDate(ZonedDateTime zdt) {
        LocalDateTime ldt = zdt.toLocalDateTime();
        return ldt.toLocalDate();
    }

    public static LocalTime convertZDTtoLocalTime(ZonedDateTime zdt) {
        LocalDateTime ldt = zdt.toLocalDateTime();
        return ldt.toLocalTime();
    }

    /**
     * Used for formatting LocalDate and LocalTime to LocalDateTime string and converting
     * local time to UTC
     * @param ld
     * @param lt
     * @return UTC LocalDateTime as a string
     */
    public static String convertLocalDateLocalTimetoUTCString(LocalDate ld, LocalTime lt) {
        System.out.println("Local Date: " + ld + " Local Time: " + lt);
        LocalDateTime ldt = LocalDateTime.of(ld, lt);
        ZonedDateTime ldtZoned = ldt.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("Zoned Date Time: " + utcZoned);
        LocalDateTime utcLDT = utcZoned.toLocalDateTime();
        System.out.println("Local Date Time before format: " + utcLDT);
        DateTimeFormatter dtfDateTime = DateTimeFormatter.ofPattern(dateTimeFormat);
        System.out.println("Formatted DateTime(to DB): " + utcLDT.format(dtfDateTime) + "\n");
        return utcLDT.format(dtfDateTime);
    }

    /**
     * Converts LocalDateTime (UTC Time Zone) to the user's system default (Local Time)
     * @param ldt
     * @return
     */
    public static String LDTUTCtoStringLocalTime (LocalDateTime ldt) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateTimeFormat);
        LocalDateTime newLDT = ldt.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        return newLDT.format(dtf);
    }

    /**
     * Formats LocalDateTime to String format "yyyy-MM-dd HH:mm:ss"
     * @param LocalDateTime variable
     * @return Formatted string of param
     */
    public static String formatLDT(LocalDateTime ldt) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateTimeFormat);
        return ldt.format(dtf);
    }
}
