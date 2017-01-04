package views;

import java.util.ArrayList;

import adapter.MyGridAdapter;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import calendar.Day;

import com.jackyzhang.calendar.R;

public class GridPager extends Pager {

	private MyGridAdapter mAdapter ;
	private OnItemClickListener mOnItemClickListener;
	private GridView mGridView;
	
	public GridPager(Context context) {
		super(context);
	}

	public void setOnItemClickListener(OnItemClickListener l){
		mOnItemClickListener = l;
	}
	
	public void setAdapter(MyGridAdapter adapter){
		mAdapter = adapter;
		mAdapter.notifyDataSetChanged();
	}
	
	public MyGridAdapter getAdapter(){
		return mAdapter;
	}
	
	@Override
	protected View createView(Context context) {
		mGridView = (GridView)View.inflate(context, R.layout.gridview_pager,null);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(mOnItemClickListener);
		return mGridView;
	}

}
