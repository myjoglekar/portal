/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.visumbu.vb.bean.DateRange;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

/**
 *
 * @author varghees
 */
public class DateUtils {

    final static Logger log = Logger.getLogger(DateUtils.class);

    public static Date get30DaysBack() {
        log.debug("Start function of get30DaysBack in DateUtils class");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Calendar cal = Calendar.getInstance();
        Calendar calReturn = Calendar.getInstance();
        calReturn.add(Calendar.DATE, -30);
        log.debug("End function of get30DaysBack in DateUtils class");
        return calReturn.getTime();
    }

    public static String getQueryString(String tableName, String searchText) {
        log.debug("Start function of getQueryString in DateUtils class");
        String searchString = searchText;
        searchString = searchString.replaceAll("\\s+and\\s+", " matchand ");
        searchString = searchString.replaceAll("\\s+or\\s+", " matchor ");
        searchString = searchString.replaceAll("createdBy", "createdBy.username");

        String queryString = "from " + tableName + " where  ";

        String[] searchTexts = searchString.split(" match");
        for (int i = 0; i < searchTexts.length; i++) {
            String searchStr = searchTexts[i];
            if (searchStr.startsWith("and ")) {
                queryString += " and ";
                searchStr = searchStr.replaceFirst("and ", "");
            } else if (searchStr.startsWith("or ")) {
                queryString += " or ";
                searchStr = searchStr.replaceFirst("or ", "");
            }
            log.debug(searchStr);

            searchStr = searchStr.trim();
            if (searchStr.contains("!=")) {
                searchStr = searchStr.replaceAll("\\s+!=\\s+", " != '");
            } else if (searchStr.contains("=")) {
                searchStr = searchStr.replaceAll("\\s*=\\s*", " = '");

            }
            searchStr = searchStr.replaceAll("\\s+like\\s+", " like '");
            if (searchStr.contains(" contains ")) {
                searchStr = "lower(" + searchStr.replaceAll("\\s+contains\\s+", ") like lower('%");
                searchStr += "%') ";
            } else {
                searchStr += "' ";
            }
            queryString += searchStr;
        }
        log.debug("End function of getQueryString in DateUtils class");
        return queryString;
    }

    public static String toJSDate(Date date) {
        log.debug("Start function of toJSDate in DateUtils class");
        String format = "yyyy-MM-dd HH:mm:ss";
        log.debug("End function of toJSDate in DateUtils class");
        return dateToString(date, format);
    }

    public static String toTTDate(Date date) {
        log.debug("Start function of toTTDate in DateUtils class");
        String format = "dd/MM/yyyy HH:mm:ss";
        log.debug("End function of toTTDate in DateUtils class");
        return dateToString(date, format);
    }

    public static String dateToString(Date date, String format) {
        log.debug("Start function of dateToString in DateUtils class");
        if (date == null) {
            return "-";
        }
        DateFormat df = new SimpleDateFormat(format);
        String reportDate = df.format(date);
        log.debug("End function of dateToString in DateUtils class");
        return reportDate;
    }

    public static Date getFirstDateOfCurrentMonth() {
        log.debug("Start function of getFirstDateOfCurrentMonth in DateUtils class");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        log.debug("End function of getFirstDateOfCurrentMonth in DateUtils class");
        return cal.getTime();
    }

    public static Date getFirstDayOfLastMonth() {
        log.debug("Start function of getFirstDayOfLastMonth in DateUtils class");
        Date date = getFirstDateOfCurrentMonth();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        log.debug("End function of getFirstDayOfLastMonth in DateUtils class");
        return cal.getTime();
    }

    public static Date getFirstDayOfNextMonth() {
        log.debug("Start function of getFirstDayOfNextMonth in DateUtils class");
        Date date = getFirstDateOfCurrentMonth();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        log.debug("End function of getFirstDayOfNextMonth in DateUtils class");
        return cal.getTime();
    }

    public static Date getTonight() {
        log.debug("Start function of getTonight in DateUtils class");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 23); //anything 0 - 23
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date today = calendar.getTime(); //the midnight, that's the first second of the day.
        log.debug("End function of getTonight in DateUtils class");
        return today;
    }

    public static Date getToday() {
        log.debug("Start function of getToday in DateUtils class");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date today = calendar.getTime(); //the midnight, that's the first second of the day.
        log.debug("End function of getToday in DateUtils class");
        return today;
    }

    public static Date get24HoursBack() {
        log.debug("Start function of get24HoursBack in DateUtils class");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        log.debug("End function of get24HoursBack in DateUtils class");
        return cal.getTime();
    }

    public static Date getOneMonthsBack(Date date) {
        log.debug("Start function of getOneMonthsBack in DateUtils class");
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        log.debug("End function of getOneMonthsBack in DateUtils class");
        return cal.getTime();
    }

    public static Date getSixMonthsBack(Date date) {
        log.debug("Start function of getSixMonthsBack in DateUtils class");
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.MONTH, -6);
        cal.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        log.debug("End function of getSixMonthsBack in DateUtils class");
        return cal.getTime();
    }

    public static Date getYesterday() {
        log.debug("Start function of getYesterday() in DateUtils class");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        log.debug("End function of getYesterday() in DateUtils class");
        return cal.getTime();
    }

    public static Date getYesterday(String date) {
        log.debug("Start function of getYesterday(String date) in DateUtils class");
        Calendar cal = Calendar.getInstance();
        Date parsedDate = getEndDate(date);
        cal.setTime(parsedDate);
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        log.debug("End function of getYesterday(String date) in DateUtils class");
        return cal.getTime();
    }

    public static Date getEndDate(String strEnd) {
        log.debug("Start function of getEndDate in DateUtils class");
        log.debug("Start Date " + strEnd);
        if (strEnd.length() < 12) {
            strEnd += " 23:59:59";
        }
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date endDate = null;
        try {
            endDate = (Date) formatter.parse(strEnd);
        } catch (Exception ex) {
            log.debug("Exception End ");
            endDate = new Date();
        }
        log.debug("End function of getEndDate in DateUtils class");
        return endDate;
    }

    public static Date getStartDate(String strStart) {
        log.debug("Start function of getStartDate in DateUtils class");
        log.debug("Start Date " + strStart);
        if (strStart.length() < 12) {
            strStart += " 00:00:00";
        }
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date startDate = null;
        try {
            startDate = (Date) formatter.parse(strStart);
        } catch (Exception ex) {
            log.debug("Exception Start ");
            startDate = DateUtils.getYesterday();
        }
        log.debug("End function of getStartDate in DateUtils class");
        return startDate;
    }

    public static Date getStartTodayDate(String strStart) {
        log.debug("Start function of getStartTodayDate in DateUtils class");
        log.debug("Start Date " + strStart);
        if (strStart.length() < 12) {
            strStart += " 00:00:00";
        }
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date startDate = null;
        try {
            startDate = (Date) formatter.parse(strStart);
        } catch (Exception ex) {
            log.debug("Exception Start ");
            startDate = DateUtils.getToday();
        }
        log.debug("End function of getStartTodayDate in DateUtils class");
        return startDate;
    }

    public static Date toDate(String dateStr, String formatStr) {
        log.debug("Start function of toDate(String dateStr, String formatStr) in DateUtils class");
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            DateFormat format = new SimpleDateFormat(formatStr);
            Date date = format.parse(dateStr);
            return date;
        } catch (ParseException ex) {
            log.error("ParseException in toDate function: " + ex);
        }
        log.debug("End function of toDate(String dateStr, String formatStr) in DateUtils class");
        return null;
    }

    public static Date toDate(String dateStr) {
        log.debug("Start function of toDate(String dateStr) in DateUtils class");
        if (dateStr.length() < 12) {
            dateStr += " 00:00:00";
        }
        String format = "dd-M-yyyy HH:mm:ss";
        log.debug("End function of toDate(String dateStr) in DateUtils class");
        return toDate(dateStr, format);
    }

    public static Date jsToJavaDate(String dateStr) {
        log.debug("Start function of jsToJavaDate in DateUtils class");
        if (dateStr.length() < 12) {
            dateStr += " 00:00:00";
        }
        String format = "dd/M/yyyy HH:mm:ss";
        log.debug("End function of jsToJavaDate in DateUtils class");
        return toDate(dateStr, format);
    }

    public static Long dateDiff(Date date1, Date date2) {
        log.debug("Start function of dateDiff in DateUtils class");
        if (date1 == null || date2 == null) {
            return 0L;
        }
        log.debug("End function of dateDiff in DateUtils class");
        return Math.abs(date1.getTime() - date2.getTime()) / (60 * 60 * 1000);
    }

    public static Long dateDiffInSec(Date date1, Date date2) {
        log.debug("Start function of dateDiffInSec in DateUtils class");
        if (date1 == null || date2 == null) {
            return 0L;
        }
        log.debug("End function of dateDiffInSec in DateUtils class");
        return (date1.getTime() - date2.getTime()) / 1000;
    }

    public static Long timeDiff(Date date1, Date date2) {
        log.debug("Start function of timeDiff in DateUtils class");
        if (date1 == null || date2 == null) {
            return 0L;
        }
        log.debug("End function of timeDiff in DateUtils class");
        return Math.abs(date1.getTime() - date2.getTime());
    }

    public static Integer getDifferenceInMonths(Date startDate, Date endDate) {
        log.debug("Start function of getDifferenceInMonths in DateUtils class");
        Calendar c1 = Calendar.getInstance();
        c1.setTime(startDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endDate);
        int diff = 0;
        if (c2.after(c1)) {
            while (c2.after(c1)) {
                c1.add(Calendar.MONTH, 1);
                if (c2.after(c1)) {
                    diff++;
                }
            }
        } else if (c2.before(c1)) {
            while (c2.before(c1)) {
                c1.add(Calendar.MONTH, -1);
                if (c1.before(c2)) {
                    diff--;
                }
            }
        }
        log.debug("End function of getDifferenceInMonths in DateUtils class");
        return diff;
    }

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        log.debug("Start function of getDaysBetweenDates in DateUtils class");
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        log.debug("End function of getDaysBetweenDates in DateUtils class");
        return dates;
    }
}
