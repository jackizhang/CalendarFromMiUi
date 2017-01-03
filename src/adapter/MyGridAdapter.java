package adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jackyzhang.calendar.R;

import calendar.Day;
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
	private int curYear,curMonth,curDay;
	private Calendar cal;
	
	public MyGridAdapter(Context context,List<Day> days) {
		mContext =context;
		//today
		cal = Calendar.getInstance();
		cal.setTime(new Date());
		int curYear = cal.get(Calendar.YEAR);
		int curMonth = cal.get(Calendar.MONTH)+1;
		int curDay = cal.get(Calendar.DAY_OF_MONTH);
		Log.i(TAG,"curDay"+curDay);
		mChoosedDay = new Day(curYear, curMonth, curDay);
		mDays.addAll(days);
	}
	
	public void setDays(List<Day> days){
		mDays = days;
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
//		mHolder.tv_day_num.setText(mDays.get(position).getDay());
		mHolder.tv_day_lunar.setText(mDays.get(position).getLunarDay().getStringLunarDay());
		Log.i(TAG,"day:"+mDays.get(position).getDay());
		Log.i(TAG,"lunarDay:"+mDays.get(position).getLunarDay().getStringLunarDay());
		mHolder.tv_day_lunar.setVisibility(View.VISIBLE);
		
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
