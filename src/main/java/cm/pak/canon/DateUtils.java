package cm.pak.canon;

import cm.pak.canon.beans.Week;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateUtils {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public static List<String> getDatesBetween(final String to, final String from) throws ParseException {
        final LocalDate toLocal = getToLocal(to);
        final LocalDate fromLocal = getToLocal(from);
        long numOfDaysBetween = ChronoUnit.DAYS.between(toLocal, fromLocal);
        return IntStream.iterate(0, i -> i+1)
                .limit(numOfDaysBetween + 1)
                .mapToObj(i -> SDF.format(Date.from(toLocal.plusDays(i).atStartOfDay()
                        .atZone(ZoneId.systemDefault()).toInstant())))
                .collect(Collectors.toList());
    }

    public static List<Week> getWeeksBetween(final String from, final String to) throws ParseException {
        final LocalDate toLocal = getToLocal(to);
        LocalDate startLocal = getToLocal(from);
        if (startLocal.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            startLocal = startLocal.plusDays(1);
        } else if( startLocal.getDayOfWeek().equals(DayOfWeek.TUESDAY)) {
            startLocal = startLocal.minusDays(1);
        } else if( startLocal.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
            startLocal = startLocal.minusDays(2);
        } else if( startLocal.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
            startLocal = startLocal.minusDays(3);
        } else if( startLocal.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            startLocal = startLocal.minusDays(4);
        } else if( startLocal.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            startLocal = startLocal.minusDays(5);
        }
        final LocalDate fromLocal = startLocal ;
        long numOfDaysBetween = ChronoUnit.WEEKS.between(fromLocal, toLocal);
        return IntStream.iterate(0, i -> i+1)
                .limit(numOfDaysBetween + 1)
                .mapToObj(i -> {
                    final LocalDate localDate = fromLocal.plusWeeks(i);
                    return new Week(String.format("Week-%s", i+1), getDate(localDate), getDate(localDate.plusDays(6)));
                })
                .collect(Collectors.toList());
    }

    public static List<String> getMonthsBetween(final String to, final String from) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        final LocalDate toLocal = getToLocal(to);
        final LocalDate fromLocal = getToLocal(from);
        long numOfDaysBetween = ChronoUnit.MONTHS.between(toLocal, fromLocal);
        return IntStream.iterate(0, i -> i+1)
                .limit(numOfDaysBetween + 1)
                .mapToObj(i -> sdf.format(Date.from(toLocal.plusMonths(i)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant())))
                .collect(Collectors.toList());
    }
    public static List<String> getYearsBetween(final String to, final String from) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        final LocalDate toLocal = getToLocal(to);
        final LocalDate fromLocal = getToLocal(from);
        long numOfDaysBetween = ChronoUnit.YEARS.between(toLocal, fromLocal);
        return IntStream.iterate(0, i -> i+1)
                .limit(numOfDaysBetween + 1)
                .mapToObj(i -> sdf.format(Date.from(toLocal.plusYears(i)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant())))
                .collect(Collectors.toList());
    }

    public static LocalDate getToLocal(String to) throws ParseException {
        return SDF.parse(to)
                .toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date getDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

}
