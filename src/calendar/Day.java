package calendar;

import java.util.Calendar;
import java.util.Date;

import utils.CalendarUtil;


public class Day {

	private int mYear;
	private int mMonth;
	private int mDay;
	private LunarDay mLunarDay;
	
	public Day(int year, int month, int day){
		mYear = year;
		mMonth = month;
		mDay = day;
		Calendar cal = Calendar.getInstance();
		cal.set(mYear, mMonth, mDay);
		mLunarDay = CalendarUtil.convert(cal);
	}
	
	public int getYear() {
		return mYear;
	}

	public int getMonth() {
		return mMonth;
	}

	public int getDay() {
		return mDay;
	}
	
	public LunarDay getLunarDay(){
		return mLunarDay;
	}
	
	public boolean equals(Day otherDay) {
		if(mYear == otherDay.getYear() && mMonth == otherDay.getMonth() && mDay == otherDay.getDay())
			return true;
		else
			return false;
	}
	
	public boolean isToday(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Day curDay = new Day(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		return this.equals(curDay);
	}
	
}
