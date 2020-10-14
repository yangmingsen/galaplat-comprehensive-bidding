package com.galaplat.comprehensive.bidding.utils;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class DateFormUtils {

    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_3 = "yyyyMMddHHmmss";
    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private static final DateFormat DAY_FORMAT = new SimpleDateFormat(PATTERN_DATE);
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(PATTERN_DATE_TIME);
    private static final SimpleDateFormat DAY_SDF = new SimpleDateFormat(PATTERN_DATE);
    public static final SimpleDateFormat DAY_DENSE = new SimpleDateFormat(PATTERN_DATE_3);

    private static final Long ONE_MINUTE = 1000*60L;
    private static final Long ONE_HOUR = ONE_MINUTE*60L;


    public static String getNowDay() {
        return DAY_FORMAT.format(new Date());

    }

    public static String getNowTime() {
        return DATE_FORMAT.format(new Date());
    }

    public static String dateToDay(Date date) {
        if (date == null) {
            return null;
        }
        return DAY_FORMAT.format(date);
    }

    public static String dateFormat(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Date getThisDateBeforeNowDate(int day) {
        return getThisDateBeforeDate(new Date(), day);
    }

    public static Date getThisDateBeforeDate(Date date, int day) {
        Calendar temp = Calendar.getInstance();
        Date _date = new Date(date.getTime());
        temp.setTime(_date);
        temp.add(Calendar.DATE, day);
        return temp.getTime();
    }

    public static Date getThisDateBeforeSecond(Date date, int second) {
        Calendar temp = Calendar.getInstance();
        Date _date = new Date(date.getTime());
        temp.setTime(_date);
        temp.add(Calendar.SECOND, second);
        return temp.getTime();
    }

    public static List<String> getNowDateBetweenDay(Date beginDate) {
        return getDateBetweenDay(beginDate, new Date());
    }

    public static List<String> getDateBetweenDayStr(String beginDate, String endDate) {
        try {
            return getDateBetweenDay(DAY_SDF.parse(beginDate), DAY_SDF.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public static List<String> getDateBetweenDay(Date beginDate, Date endDate) {

        if (beginDate == null || endDate == null) {
            return Collections.emptyList();
        }

        List<String> days = new ArrayList<String>();

        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(beginDate);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(endDate);

        while (tempStart.before(tempEnd)) {
            days.add(DAY_FORMAT.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }

        return days;
    }

    public static int getDateBetweenSpaceHour(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            return 0;
        }
        return (int)((beginDate.getTime() - endDate.getTime()) / ONE_HOUR);
    }

    public static int compare(Date beginDate, Date endDate) {

        Assert.isNull(beginDate, "对比时间 beginDate 为空");
        Assert.isNull(endDate, "对比时间 endDate 为空");

        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();

        if (beginTime > endTime) {
            return 1;
        } else if (beginTime < endTime) {
            return -1;
        } else {
            return 0;
        }
    }

    public static int compare(String beginDate, String endDate) {
        return compare(strToDay(beginDate), strToDay(endDate));
    }

    public static Date strToDay(String date) {
        try {
            return DAY_SDF.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date strToDay(String date, String pattern) {
        try {
            DateFormat DAY_FORMAT = new SimpleDateFormat(pattern);
            return DAY_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DateTime getMonthAndWeek(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        final int month = c.get(Calendar.MONTH) + 1;
        int week = c.get(Calendar.WEEK_OF_YEAR);
        final int year = c.get(Calendar.YEAR);

        final int weekday = c.get(Calendar.DAY_OF_WEEK);

        if (weekday == 1) {
            week -= 1;
        }

        return DateTime.builder()
                .month(month)
                .week(week)
                .year(year)
                .build();
    }

    @Builder
    @Getter
    public static class DateTime{
        private int month;
        private int week;
        private int year;

    }

    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    public static String format(Date date, FastDateFormat pattern) {
        return DateFormatUtils.format(date, pattern.getPattern());
    }

    public static Date localDate2Date(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static void main(String[] args) throws ParseException {
        Date date0 = DAY_FORMAT.parse("2020-05-31");
        Date date1 = DAY_FORMAT.parse("2020-06-02");
        Date date2 = DAY_FORMAT.parse("2020-06-07");
        Date date3 = DAY_FORMAT.parse("2020-06-08");

        System.out.println(getMonthAndWeek(date0).getWeek());
        System.out.println(getMonthAndWeek(date1).getWeek());
        System.out.println(getMonthAndWeek(date2).getWeek());
        System.out.println(getMonthAndWeek(date3).getWeek());
    }
}
