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
import calendar.Day;
import calendar.Month;

import com.jackyzhang.calendar.R;


public class MainActivity extends Activity {

	private ViewPager mViewPager;
	private MyPagerAdapter mAdapter;
	private Calendar calendar;
	private ArrayList<Month> mMonthList = new ArrayList<Month>(3);
	private ArrayList<GridPager> mPagers = new ArrayList<GridPager>(3);
	private Month mCurMonth;
	private Day mChosedDay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ResourceManager.init(this);
		Log.i("onCreate","pager's size()=="+mPagers.size());
		Log.i("onCreate","monthList's size"+mMonthList.size());
		initData();
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initData() {
		Date date = new Date();
		calendar = Calendar.getInstance();
		calendar.setTime(date);//today
		mChosedDay = new Day(calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH),
							calendar.get(Calendar.DAY_OF_MONTH));
		mCurMonth = new Month(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
//		notifyAdapter();
		Log.i("after initData","pager's size()=="+mPagers.size());
		Log.i("after initData","monthList's size"+mMonthList.size());
	}

	//init 3 pagers by curmonth
	private void notifyAdapter() {
		//init 3 list of days from preMonth,curMonth,nextMonth
		mMonthList.clear();
		mPagers.clear();
		mMonthList.add(0,mCurMonth.preMonth());
		mMonthList.add(1,mCurMonth);
		mMonthList.add(2,mCurMonth.nextMonth());
		//init pagerList
		GridPager prevPager = new GridPager(this);
		MyGridAdapter prevAdapter = new MyGridAdapter(getApplicationContext(),mMonthList.get(0).getDaysOfMonth());
		prevPager.setAdapter(prevAdapter);
		mPagers.add(0,prevPager);
		
		GridPager curPager = new GridPager(this);
		MyGridAdapter curAdapter = new MyGridAdapter(getApplicationContext(),mMonthList.get(1).getDaysOfMonth());
		curPager.setAdapter(curAdapter);
		mPagers.add(1,curPager);
		
		GridPager nextPager = new GridPager(this);
		MyGridAdapter nextAdapter = new MyGridAdapter(getApplicationContext(),mMonthList.get(2).getDaysOfMonth());
		nextPager.setAdapter(nextAdapter);
		mPagers.add(2,nextPager);
		
		mAdapter.notifyDataSetChanged();
	}

	
	private void initViews() {
		mViewPager = (ViewPager)findViewById(R.id.vp_clendar);
//		mAdapter = new MyPagerAdapter(mPagers);??子类为什么不行？
		mAdapter = new MyPagerAdapter(mPagers);
		
		mViewPager.setAdapter(mAdapter);
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
						mViewPager.setCurrentItem(1);
					}else if(mViewPager.getCurrentItem()<1){
						//prev month
						mCurMonth = mCurMonth.preMonth();
						notifyAdapter();
						mViewPager.setCurrentItem(1);
					}
				}
			}
		});
		mViewPager.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				notifyAdapter();
			}
		}, getResources().getInteger(android.R.integer.config_longAnimTime));
	}
}
