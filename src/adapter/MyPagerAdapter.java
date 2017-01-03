package adapter;

import java.util.ArrayList;
import java.util.List;

import views.GridPager;
import views.Pager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter {

	private List<Pager> mPagers = new ArrayList<Pager>();
	
	public MyPagerAdapter(){}
//	public MyPagerAdapter(ArrayList<Pager> pagers){
//		mPagers = pagers;
//	}
	public MyPagerAdapter(ArrayList<GridPager> pagers){
		mPagers.clear();
		mPagers.addAll(pagers);
	}
	
	public void setPagers(ArrayList<Pager> pagers){
		mPagers = pagers;
	}
	@Override
	public int getCount() {
		return mPagers.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mPagers.get(position).getView());
		return mPagers.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mPagers.get(position).getView());
	}

}
