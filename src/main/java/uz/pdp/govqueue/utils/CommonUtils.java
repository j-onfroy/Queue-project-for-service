package uz.pdp.govqueue.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class CommonUtils {

    public static Timestamp todayWithStartTime() {
        return Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)));
    }

    public static Timestamp yesterdayWithStartTime() {
        return Timestamp.valueOf(LocalDateTime.of(LocalDate.now().minus(1, ChronoUnit.DAYS), LocalTime.of(0, 0)));
    }
}
