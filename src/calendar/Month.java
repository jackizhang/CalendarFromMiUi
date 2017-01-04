package calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.util.Log;

public class Month {
	private static final String TAG = "Month";
	
	private int mYear;
	private int mMonth; //from 0 -11
	private static final int MAX_YEAR = 2048;
	private static final int MIN_YEAR = 1900;
	private static final int MAX_MONTH = 11;
	private static final int MIN_MONTH = 0;
	private List<Day> mDaysOfMonth = new ArrayList<Day>();
	
	public Month(int year,int month){
		mYear = year;
		mMonth = month;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH,mMonth);
		for(int i=0;i<cal.getActualMaximum(Calendar.DAY_OF_MONTH);i++){
			mDaysOfMonth.add(new Day(mYear, mMonth, i+1));
		}
	}
	
	public int getYear(){
		return mYear;
	}
	
	public int getMonth(){
		return mMonth;
	}
	
	public List<Day> getDaysOfMonth(){
		return mDaysOfMonth;
	}
	
	public Month preMonth(){
		if(mMonth >= 1)
			return new Month(mYear, mMonth-1);
		else{
			if(mYear>MIN_YEAR)
				return new Month(mYear-1,MAX_MONTH);
			else
				return new Month(MIN_YEAR,MAX_MONTH);
		}
	}
	
	public Month nextMonth(){
		if(mMonth< MAX_MONTH)
			return new Month(mYear,mMonth+1);
		else{
			if(mYear<MAX_YEAR)
				return new Month(mYear+1,MIN_MONTH);
			else
				return new Month(MAX_YEAR,MIN_MONTH);
		}
	}
	
	public Day getLastDay(){
		return mDaysOfMonth.get(mDaysOfMonth.size()-1);
	}
}
