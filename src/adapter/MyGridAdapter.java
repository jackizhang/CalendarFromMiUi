package adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jackyzhang.calendar.R;

import calendar.Day;
import calendar.Month;
import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter {

	private static final String TAG = MyGridAdapter.class.getSimpleName();
	
	private List<Day> mDays = new ArrayList<Day>();
	private Context mContext;
	private Day mChoosedDay;
	private Month mCurMonth;
	private Calendar cal;
	
	public MyGridAdapter(Context context,Month month,int choseDay) {
		mContext =context;
		//today
		cal = Calendar.getInstance();
		cal.setTime(new Date());
		mDays.addAll(month.getDaysOfMonth());
		mCurMonth = month;
//		mChoosedDay = mCurMonth.getDaysOfMonth().size()>choseDay
		fillUpDayList(month);
	}

	
	//补齐天数
	private void fillUpDayList(Month month) {
		//if not started with sunday,add preMonth's days
		Day firstDay = month.getDaysOfMonth().get(0);
		int firstDayOfWeek = firstDay.getIntDayOfWeek();
		Month prevMonth = month.preMonth();
		int sizeOfMonth = prevMonth.getDaysOfMonth().size();
		for(int i =1;i<firstDayOfWeek;i++){
			mDays.add(0,prevMonth.getDaysOfMonth().get(sizeOfMonth-i));
		}
		//if not ended with saturday,add nextMonth's days
		Day endDay = month.getDaysOfMonth().get(month.getDaysOfMonth().size()-1);
		int endDayOfWeek = endDay.getIntDayOfWeek();
		Month nextMonth = month.nextMonth();
		for(int i=0;i<(7-endDayOfWeek);i++){
			mDays.add(nextMonth.getDaysOfMonth().get(i));
		}
	}
	
	
	public void setMonth(Month month){
		mCurMonth = month;
		mDays.clear();
		mDays.addAll(month.getDaysOfMonth());
		fillUpDayList(month);
	}
	
	@Override
	public int getCount() {
		Log.i(TAG,"getCount:"+mDays.size());
		return mDays.size();
	}

	@Override
	public Object getItem(int position) {
		return mDays.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG,"getView at position:"+position);
		ViewHolder mHolder = null;
		if(convertView == null){
			mHolder = new ViewHolder();
			convertView =  LayoutInflater.from(mContext).inflate(R.layout.grid_day_item, parent, false);
			mHolder.ll_background = (LinearLayout)convertView.findViewById(R.id.ll_background);
			mHolder.iv_istodo = (ImageView)convertView.findViewById(R.id.iv_istodo);
			mHolder.tv_day_num = (TextView)convertView.findViewById(R.id.tv_day_num);
			mHolder.tv_day_lunar = (TextView)convertView.findViewById(R.id.tv_day_luner);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder)convertView.getTag();
		}
	
		
		mHolder.iv_istodo.setVisibility(View.INVISIBLE);
		mHolder.tv_day_num.setVisibility(View.VISIBLE);
		mHolder.tv_day_num.setText(mDays.get(position).getDay()+"");
//		mHolder.tv_day_num.setText("11");
		mHolder.tv_day_lunar.setText(mDays.get(position).getLunarDay().getStringLunarDay());
		Log.i(TAG,"day:"+mDays.get(position).getDay());
		Log.i(TAG,"lunarDay:"+mDays.get(position).getLunarDay().getStringLunarDay());
		mHolder.tv_day_lunar.setVisibility(View.VISIBLE);
		
		if(mDays.get(position).getMonth() != mCurMonth.getMonth())
			mHolder.tv_day_num.setTextColor(mContext.getResources().getColor(R.color.text_light_grey));
		else
			mHolder.tv_day_num.setTextColor(mContext.getResources().getColor(android.R.color.black));
		
		if((mDays.get(position).isToday())){
			mHolder.tv_day_num.setTextColor(mContext.getResources().getColor(R.color.text_blue_color));
		}
		if(mChoosedDay.equals(mDays.get(position))){
			mHolder.tv_day_lunar.setVisibility(View.INVISIBLE);
			mHolder.ll_background.setBackgroundResource(R.drawable.grid_item_white_background_circle);
		}
		if(mChoosedDay.isToday() && mDays.get(position).isToday()){
			mHolder.tv_day_num.setTextColor(mContext.getResources().getColor(android.R.color.white));
			mHolder.tv_day_lunar.setVisibility(View.INVISIBLE);
			mHolder.ll_background.setBackgroundResource(R.drawable.grid_item_blue_background_circle);
		}
		
		return convertView;
	}
	
	class ViewHolder{
		LinearLayout ll_background;
		ImageView iv_istodo;
		TextView tv_day_num;
		TextView tv_day_lunar;
	}

}
