package activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import utils.ResourceManager;
import views.GridPager;
import views.Pager;
import adapter.MyGridAdapter;
import adapter.MyPagerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.TextView;
import calendar.Day;
import calendar.Month;

import com.jackyzhang.calendar.R;


public class MainActivity extends Activity {

	private ViewPager mViewPager;
	private MyPagerAdapter mAdapter;
	private TextView tv_curYear,tv_curMonth,tv_day_of_week;
	private Calendar calendar;
	private ArrayList<Month> mMonthList = new ArrayList<Month>(3);
	private ArrayList<GridPager> mPagers = new ArrayList<GridPager>(3);
	private Month mCurMonth;
	private int mChosedDay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ResourceManager.init(this);
		initData();
		initViews();
	}

	private void initData() {
		Date date = new Date();
		calendar = Calendar.getInstance();
		calendar.setTime(date);//today
		mChosedDay = calendar.get(Calendar.DAY_OF_MONTH);
		mCurMonth = new Month(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
		
		//init monthList and PagerList
		mMonthList.add(0,mCurMonth.preMonth());
		mMonthList.add(1,mCurMonth);
		mMonthList.add(2,mCurMonth.nextMonth());
		
		//pagerList
		GridPager prevPager = new GridPager(this);
		MyGridAdapter prevAdapter = new MyGridAdapter(getApplicationContext(),mMonthList.get(0),mChosedDay);
		prevPager.setAdapter(prevAdapter);
		mPagers.add(0,prevPager);
		
		GridPager curPager = new GridPager(this);
		MyGridAdapter curAdapter = new MyGridAdapter(getApplicationContext(),mMonthList.get(1),mChosedDay);
		curPager.setAdapter(curAdapter);
		mPagers.add(1,curPager);
		
		GridPager nextPager = new GridPager(this);
		MyGridAdapter nextAdapter = new MyGridAdapter(getApplicationContext(),mMonthList.get(2),mChosedDay);
		nextPager.setAdapter(nextAdapter);
		mPagers.add(2,nextPager);
		
	}

	//init 3 pagers by curmonth
	private void notifyAdapter() {
		//refresh monthList and pagerList
		mMonthList.set(0,mCurMonth.preMonth());
		mMonthList.set(1,mCurMonth);
		mMonthList.set(2,mCurMonth.nextMonth());
		
		mPagers.get(0).getAdapter().setMonth(mMonthList.get(0));
		mPagers.get(0).getAdapter().notifyDataSetChanged();
		
		mPagers.get(1).getAdapter().setMonth(mMonthList.get(1));
		mPagers.get(1).getAdapter().notifyDataSetChanged();
		
		mPagers.get(2).getAdapter().setMonth(mMonthList.get(2));
		mPagers.get(2).getAdapter().notifyDataSetChanged();
//		MyGridAdapter prevAdapter = new MyGridAdapter(getApplicationContext(),mMonthList.get(0).getDaysOfMonth());
//		mPagers.get(0).setAdapter(prevAdapter);
//		
//		MyGridAdapter curAdapter = new MyGridAdapter(getApplicationContext(),mMonthList.get(1).getDaysOfMonth());
//		mPagers.get(1).setAdapter(curAdapter);
//		
//		MyGridAdapter nextAdapter = new MyGridAdapter(getApplicationContext(),mMonthList.get(2).getDaysOfMonth());
//		mPagers.get(2).setAdapter(nextAdapter);
		
	}

	
	private void initViews() {
		
		tv_curMonth = (TextView)findViewById(R.id.tv_curMonth);
		tv_curYear = (TextView)findViewById(R.id.tv_curYear);
		tv_day_of_week = (TextView)findViewById(R.id.tv_chosed_day_of_week);
		
		mViewPager = (ViewPager)findViewById(R.id.vp_clendar);
//		mAdapter = new MyPagerAdapter(mPagers);??子类为什么不行？
		mAdapter = new MyPagerAdapter(mPagers);
		
		mViewPager.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(1);
//		mAdapter = new MyPagerAdapter(pagers)
		
		//recycle viewPager
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				Log.i("MainActivity","onPageScrollStateChanged");
				if(state == ViewPager.SCROLL_STATE_IDLE){
					if(mViewPager.getCurrentItem()>1){
						//next month
						mCurMonth = mCurMonth.nextMonth();
						notifyAdapter();
						mViewPager.setCurrentItem(1, false);
					}else if(mViewPager.getCurrentItem()<1){
						//prev month
						mCurMonth = mCurMonth.preMonth();
						notifyAdapter();
						mViewPager.setCurrentItem(1, false);
					}
					tv_curMonth.setText(mCurMonth.getMonth()+1+"月");
					tv_curYear.setText(mCurMonth.getYear()+"年");
					tv_day_of_week.setText(mChosedDay.getStringDayOfWeek());
				}
			}
		});
		
		tv_curMonth.setText(mCurMonth.getMonth()+1+"月");
		tv_curYear.setText(mCurMonth.getYear()+"年");
		tv_day_of_week.setText(mChosedDay.getStringDayOfWeek());
		
	}
}
