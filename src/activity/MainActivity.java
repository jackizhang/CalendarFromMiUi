package activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import utils.ResourceManager;
import views.GridPager;
import views.Pager;
import adapter.MyGridAdapter;
import adapter.MyPagerAdapter;
import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import calendar.Day;
import calendar.Month;

import com.jackyzhang.calendar.R;


public class MainActivity extends Activity {

	private ViewPager mViewPager;
	private MyPagerAdapter mAdapter;
	private TextView tv_curYear,tv_curMonth,tv_day_of_week;
	private LinearLayout ll_head;
	private Calendar calendar;
	private ArrayList<Month> mMonthList = new ArrayList<Month>(3);
	private ArrayList<GridPager> mPagers = new ArrayList<GridPager>(3);
	private Month mCurMonth;
	private int mChosedDay;
	//12 color for 12 months
	private int[] mColorList = {0xff7D97E1,0xff27BBD0,0xff8CC061,0xff3FBC7D,0xff11B599,0XffF38F3A,0XffEB6E4A,0XffDD5A62,0XffE8774D,0XffE29255,0XFF579EE0,0Xff9B88D0};
	private int mHeadBackColor;
	private ArgbEvaluator mArgbEvaluator;
	private boolean mRecycle = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ResourceManager.init(this);
		mArgbEvaluator = new ArgbEvaluator();
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
		
		mPagers.get(0).getAdapter().setMonth(mMonthList.get(0),mChosedDay);
		mPagers.get(0).getAdapter().notifyDataSetChanged();
		
		mPagers.get(1).getAdapter().setMonth(mMonthList.get(1),mChosedDay);
		mPagers.get(1).getAdapter().notifyDataSetChanged();
		
		mPagers.get(2).getAdapter().setMonth(mMonthList.get(2),mChosedDay);
		mPagers.get(2).getAdapter().notifyDataSetChanged();
		
	}

	
	private void initViews() {
		
		tv_curMonth = (TextView)findViewById(R.id.tv_curMonth);
		tv_curYear = (TextView)findViewById(R.id.tv_curYear);
		tv_day_of_week = (TextView)findViewById(R.id.tv_chosed_day_of_week);
		ll_head = (LinearLayout)findViewById(R.id.ll_head);
		
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
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				//翻到下一页的时候，positionOffset=0，但是还没有执行onPageScrollStateChanged()，所以curMonth没改变
				//就会把颜色变的和刚开始的颜色一样
				if(mRecycle || positionOffset == 0f)
					return; 
				//左滑到上个月
				int startColorInedx = mCurMonth.getMonth();
				int endColorIndex;
				if(position<1){
					endColorIndex = mCurMonth.preMonth().getMonth();
					mHeadBackColor = (Integer)mArgbEvaluator.evaluate(1-positionOffset, mColorList[startColorInedx], mColorList[endColorIndex]);
				}else{
					//右滑到下个月
					endColorIndex = mCurMonth.nextMonth().getMonth();
					mHeadBackColor = (Integer)mArgbEvaluator.evaluate(positionOffset, mColorList[startColorInedx], mColorList[endColorIndex]);
				}
				
					ll_head.setBackgroundColor(mHeadBackColor);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				if(state == ViewPager.SCROLL_STATE_IDLE){
					
					if(mViewPager.getCurrentItem()>1){
						//next month
						mCurMonth = mCurMonth.nextMonth();
						notifyAdapter();
						mRecycle = true;
						Log.i("MainActivity","setCurrentItem()");
						mViewPager.setCurrentItem(1, false);
						mRecycle = false;
					}else if(mViewPager.getCurrentItem()<1){
						//prev month
						mCurMonth = mCurMonth.preMonth();
						notifyAdapter();
						mRecycle = true;
						mViewPager.setCurrentItem(1, false);
						mRecycle = false;
					}
					
					tv_curMonth.setText(mCurMonth.getMonth()+1+"月");
					tv_curYear.setText(mCurMonth.getYear()+"年");
//					tv_day_of_week.setText(mChosedDay.getStringDayOfWeek());
				}
			}
		});
		
		tv_curMonth.setText(mCurMonth.getMonth()+1+"月");
		tv_curYear.setText(mCurMonth.getYear()+"年"); 
//		tv_day_of_week.setText(mChosedDay.getStringDayOfWeek());
		mHeadBackColor = mColorList[mCurMonth.getMonth()];
		ll_head.setBackgroundColor(mHeadBackColor);
	}
}
