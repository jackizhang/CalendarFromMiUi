package utils;


import java.util.Calendar;

import calendar.LunarDay;

import com.jackyzhang.calendar.R;

/**
 * 农历转换工具类
 */
public class CalendarUtil {

    private final static long[] LUNAR_INFO =
            new long[] { 0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
                    0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970,
                    0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0,
                    0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550,
                    0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355, 0x04da0,
                    0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570,
                    0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250,
                    0x0d558, 0x0b540, 0x0b5a0, 0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
                    0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60,
                    0x096d5, 0x092e0, 0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0,
                    0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
                    0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530, 0x05aa0,
                    0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0,
                    0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

    /**
     * 公历节日
     */
    private static String[] sSolarHoliday;
    /**
     * 农历节日
     */
    private static String[] sLunarHoliday;

    /**
     * 国家法定节假日
     */
    private static String[] sHoliday2015;
    /**
     * 国家法定工作日
     */
    private static String[] sWorkday2015;

    /**
     * 将公历的日期转换为农历
     * 
     * @param calendar
     * @return
     */
    public static LunarDay convert(Calendar calendar) {
        Calendar baseDate = Calendar.getInstance();
        baseDate.set(1900, 1 - 1, 31, 0, 0, 0);
        baseDate.set(Calendar.MILLISECOND, 0);
        // 求出和1900年1月31日相差的天数
        int offset = (int) ((calendar.getTimeInMillis() - baseDate.getTimeInMillis()) / 86400000L);

        // 用offset减去每农历年的天数
        // 计算当天是农历第几天
        // 最终结果是农历的年份
        // offset是当年的第几天
        int iYear;
        int daysOfYear = 0;
        for (iYear = 1900; iYear < 10000 && offset > 0; iYear++) {
            daysOfYear = getYearDays(iYear);
            offset -= daysOfYear;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
        }
        // 农历年份
        final int leapMonth = getLeapMonth(iYear); // 闰哪个月,1-12
        boolean leap = false;

        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth;
        int daysOfMonth = 0;
        for (iMonth = 1; iMonth <= 12 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = getLeapMonthDays(iYear);
            } else {
                daysOfMonth = getMonthDays(iYear, iMonth);
            }

            offset -= daysOfMonth;
            // 解除闰月
            if (leap && iMonth == (leapMonth + 1)) {
                leap = false;
            }
        }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
            }
        }
        // offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
        }
        int iDay = offset + 1;
        return new LunarDay(iYear, iMonth, iDay);
    }

    // 农历 y年的总天数
    private static int getYearDays(int year) {
        int sum = 348;
        for (int i = 0x8000; i > 0x8; i >>= 1) {
            if ((LUNAR_INFO[year - 1900] & i) != 0) {
                sum += 1;
            }
        }
        return (sum + getLeapMonthDays(year));
    }

    // 农历 y年m月的总天数
    private static int getMonthDays(int year, int month) {
        return (LUNAR_INFO[year - 1900] & (0x10000 >> month)) == 0 ? 29 : 30;
    }

    // 农历 y年闰月的天数
    private static int getLeapMonthDays(int year) {
        int month = getLeapMonth(year);
        if (month != 0) {
            return (LUNAR_INFO[year - 1900] & 0x10000) != 0 ? 30 : 29;
        }
        return 0;
    }

    // 农历 y年闰哪个月 1-12 , 没闰传回 0
    private static final int getLeapMonth(int year) {
        return (int) (LUNAR_INFO[year - 1900] & 0xf);
    }

    /**
     * 判断是否为国家放假日
     * 
     * @param context
     * @param month
     * @param day
     * @return
     */
    public static boolean isHoliDay(int month, int day) {
        if (sHoliday2015 == null) {
            sHoliday2015 = ResourceManager.getStringArray(R.array.holiday_2015);
        }
        return isInDays(month, day, sHoliday2015);
    }

    /**
     * 判断是否为国家工作日
     * 
     * @param context
     * @param month
     * @param day
     * @return
     */
    public static boolean isWorkDay(int month, int day) {
        if (sWorkday2015 == null) {
            sWorkday2015 = ResourceManager.getStringArray(R.array.workday_2015);
        }
        return isInDays(month, day, sWorkday2015);
    }

    /**
     * 公历部分节假日
     * 
     * @param month
     * @param day
     * @return
     */
    public static String getSolarHoliday(int month, int day) {
        if (sSolarHoliday == null) {
            sSolarHoliday = ResourceManager.getStringArray(R.array.solar_holiday);
        }
        return getHolidayName(sSolarHoliday, month, day);
    }

    /**
     * 农历部分假日
     * 
     * @param month
     * @param day
     * @return
     */
    public static String getLunarHoliday(int month, int day) {
        if (sLunarHoliday == null) {
            sLunarHoliday = ResourceManager.getStringArray(R.array.lunar_holiday);
        }
        return getHolidayName(sLunarHoliday, month, day);
    }

    /**
     * 判断是否为闰年
     * 
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 得到某月有多少天数
     * 
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        int daysOfMonth = 0; // 某月的天数
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                daysOfMonth = isLeapYear(year) ? 29 : 28;
                break;
            default:
                break;
        }
        return daysOfMonth;
    }

    /**
     * 判断是否在所给定的日期内
     * 
     * @param month
     * @param day
     * @param days
     * @return
     */
    private static boolean isInDays(int month, int day, String[] days) {
        for (String oneDay : days) {
            String date = String.format("%02d%02d", month, day);
            if (oneDay.equals(date)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取节假日的名称
     * 
     * @param holidayDayArray
     * @param month
     * @param day
     * @return
     */
    private static String getHolidayName(String[] holidayDayArray, int month, int day) {
        for (String holidayDay : holidayDayArray) {
            final String[] array = holidayDay.split(" ");
            final String holidayDate = array[0]; // 节假日的日期
            final String holidayName = array[1]; // 节假日的名称
            String date = String.format("%02d%02d", month, day);
            if (holidayDate.equals(date)) {
                return holidayName;
            }
        }
        return null;
    }
}
