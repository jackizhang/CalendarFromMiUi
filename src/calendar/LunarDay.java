package calendar;

import utils.ResourceManager;
import android.content.res.Resources;

import com.jackyzhang.calendar.R;

public class LunarDay {

	private int mLunarYear;
	private int mLunarMonth;
	private int mLunarDay;
	
	public LunarDay(int year,int month,int day){
		mLunarYear = year;
		mLunarMonth = month;
		mLunarDay = day;
		
	}

	
	public String getStringLunarYear() {
		int num = mLunarYear - 1900 + 36;
		// 传入 月日的offset 传回干支, 0=甲子
		Resources res = ResourceManager.getResources();
		String[] heavenlyStems = res.getStringArray(R.array.gan);
		String[] earthlyBranches = res.getStringArray(R.array.zhi);
		return (heavenlyStems[num % heavenlyStems.length] + earthlyBranches[num
				% earthlyBranches.length]);
	}

	public String getStringLunarMonth() {
		Resources res = ResourceManager.getResources();
		String[] lunarMonths = res.getStringArray(R.array.lunar_month);
		return lunarMonths[mLunarMonth];
	}

	public String getStringLunarDay() {
		Resources res = ResourceManager.getResources();
		String[] lunarDays = res.getStringArray(R.array.lunar_day);
		return lunarDays[mLunarDay];
	}
	
	public int getIntLunarMonth(){
		return mLunarMonth;
	}
	
	public int getIntLunarDay(){
		return mLunarDay;
	}
	
}
