package com.humminbird.machinepartverifierandroid.Utilities;

import android.icu.util.Calendar;
import android.os.Build;

import java.text.SimpleDateFormat;

public class DateUtils {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;


    public static String getCurrentWeekday(){
        String weekDay = "Unknown";
        Calendar c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            if (Calendar.MONDAY == dayOfWeek) {
                weekDay = "Monday";
            } else if (Calendar.TUESDAY == dayOfWeek) {
                weekDay = "Tuesday";
            } else if (Calendar.WEDNESDAY == dayOfWeek) {
                weekDay = "Wednesday";
            } else if (Calendar.THURSDAY == dayOfWeek) {
                weekDay = "Thursday";
            } else if (Calendar.FRIDAY == dayOfWeek) {
                weekDay = "Friday";
            } else if (Calendar.SATURDAY == dayOfWeek) {
                weekDay = "Saturday";
            } else if (Calendar.SUNDAY == dayOfWeek) {
                weekDay = "Sunday";
            } else {
                weekDay = "Unknown";
            }
        }
        return weekDay;
    }

    public static String getTimestamp() {
        String timestamp = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            timestamp = getCurrentWeekday()+":"+getCurrentTime()+":"+getCurrentSecond()+getCurrentYear();
        } else {
            timestamp = "Alashazam";
        }
        return timestamp;
    }

    public static int getCurrentYear() {
        int currentYear = 0;
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            currentYear = c.get(Calendar.YEAR);
        }
        return currentYear;
    }

    public static String getCurrentTime() {
        String currentTime = null;
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            int hour = 0;
            int minute = 0;
            hour = c.get(Calendar.HOUR);
            minute = c.get(Calendar.MINUTE);
            int am = c.get(Calendar.AM_PM);

            String time = "";
            if(hour == 0){
                hour = 12;
            }

            if(minute < 10){
                time =  hour+":0"+minute;
            } else {
                time =  hour+":"+minute;
            }
            if(am == Calendar.AM){
                time = time+" A.M.";
            } else {
                time = time+" P.M.";
            }

            return time;
        } else {
            currentTime = "Unknown time.";
        }
        return currentTime;
    }

    public static int getCurrentSecond() {
        int currentSecond = 0;
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            int hour = 0;
            int minute = 0;
            currentSecond = c.get(Calendar.SECOND);
            return currentSecond;
        }
        return currentSecond;
    }

    public static String getCurrentReadableTimestamp(){
        String ts = "";
        Calendar c = null;
        ts = getCurrentTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            Calendar cal= Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            String month_name = month_date.format(cal.getTime());
            ts = ts+" "+month_name;
            ts = ts+" "+c.get(Calendar.DAY_OF_MONTH);
            ts = ts+" "+getCurrentYear();
            return ts;
        }

        return "0:0 Unknown 0";//h:m month day
    }

    public static String convertLongToTimestamp(long millis) {
        Calendar c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            //Set time in milliseconds
            c.setTimeInMillis(millis);

            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH)+1;
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            int hr = c.get(Calendar.HOUR);
            int ampm = c.get(Calendar.AM_PM);
            String apm = "AM";
            if(ampm == Calendar.PM){
                apm = "PM";
            }
            int min = c.get(Calendar.MINUTE);
            int sec = c.get(Calendar.SECOND);

            //Format mm/dd/yr' 'h:mm' '
            return mMonth+"/"+mDay+"/"+mYear+" "+hr+":"+min+" "+apm;

        }

        return "Device not supported.";
    }

    public static int getCurrentHour() {
        Calendar c = null;
        int currentHour = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            currentHour = c.get(Calendar.HOUR);
            if(currentHour == 0){
                //make it 12!!
                currentHour = 12;
            }
        }

        return currentHour;
    }

    public static int getCurrentMilitaryHour(){
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            if(c.get(Calendar.AM_PM) == 0){
                //Its morning
                return getCurrentHour();
            } else {
                //afternoon, so add the first twelve;
                return getCurrentHour()+12;
            }
        } else {
            return 0;
        }
    }

    public static long getTimeLong() {
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            return c.getTimeInMillis();
        } else {
            return 0;
        }
    }

    public static int getMonthFrom(long date) {

        Calendar c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            c.setTimeInMillis(date);
            int mYear = c.get(Calendar.YEAR)-1970;
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH)-1;
            int mWeek = (c.get(Calendar.DAY_OF_MONTH)-1)/7;

            return mMonth;
        } else {
            return -1;
        }

    }

    public static int getMinuteFrom(long date) {
        Calendar c = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            c.setTimeInMillis(date);
            int mYear = c.get(Calendar.YEAR)-1970;
            int mMonth = c.get(Calendar.MONTH);
            int minute = c.get(Calendar.MINUTE);
            int hour = c.get(Calendar.HOUR);
            int mDay = c.get(Calendar.DAY_OF_MONTH)-1;
            int mWeek = (c.get(Calendar.DAY_OF_MONTH)-1)/7;
            hour = hour *60;
            minute = minute+hour;
            return minute;
        } else {
            return -1;
        }
    }

    public static String getDateString() {
        String currentTime = null;
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            String day = getCurrentWeekday();
            currentTime = day.substring(0,3);

            int month = c.get(Calendar.MONTH);
            switch (month){
                case Calendar.JANUARY:
                    currentTime = currentTime+", January "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.FEBRUARY:
                    currentTime = currentTime+", February "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.MARCH:
                    currentTime = currentTime+", March "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.APRIL:
                    currentTime = currentTime+", April "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.MAY:
                    currentTime = currentTime+", May "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.JUNE:
                    currentTime = currentTime+", June "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.JULY:
                    currentTime = currentTime+", July "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.AUGUST:
                    currentTime = currentTime+", August "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.SEPTEMBER:
                    currentTime = currentTime+", September "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.OCTOBER:
                    currentTime = currentTime+", October "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.NOVEMBER:
                    currentTime = currentTime+", November "+c.get(Calendar.DAY_OF_MONTH);
                    break;
                case Calendar.DECEMBER:
                    currentTime = currentTime+", December "+c.get(Calendar.DAY_OF_MONTH);
                    break;

            }

            return currentTime;
        } else {
            currentTime = "Unknown date.";
        }
        return currentTime;
    }

    public static long getStreamExpireTime(long startTime) {
        String currentTime = null;
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            c.add(Calendar.HOUR, 1);

            return c.getTime().getTime();
        } else {
            return 0;
        }
    }

    public static String getCurrentMonth(int charCount) {
        String currentTime = null;
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            c.add(Calendar.HOUR, 1);

            int month =  c.getTime().getMonth();
            month++;
            switch(month){
                case 1:
                    return "Jan";

                case 2:
                    return "Feb";

                case 3:
                    return "Mar";

                case 4:
                    return "Apr";

                case 5:
                    return "May";

                case 6:
                    return "Jun";

                case 7:
                    return "Jul";

                case 8:
                    return "Aug";

                case 9:
                    return "Sep";

                case 10:
                    return "Oct";

                case 11:
                    return "Nov";

                case 12:
                    return "Dec";

                default:
                    return "Unknown";
            }
        } else {
            return "Unknown";
        }
    }

    public static int getCurrentDay() {
        String currentTime = null;
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            c.add(Calendar.HOUR, 1);

            return c.getTime().getDay();
        } else {
            return 0;
        }
    }

    public static int getCurrentDayOMonth() {
        String currentTime = null;
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            c.add(Calendar.HOUR, 1);

            return c.get(Calendar.DAY_OF_MONTH);
        } else {
            return 0;
        }
    }
}


