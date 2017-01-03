package views;

import android.content.Context;
import android.view.View;

public abstract class Pager {
	
	protected View mContentView;
	protected Context mContext;
	
	public Pager(Context context){
		mContext = context;
	}
	
	protected Context getContext(){
		return mContext;
	}
	
	public View getView(){
		if(mContentView == null)
			mContentView = createView(mContext);
		return mContentView;
	}
	
	public void setView(View view){
		mContentView = view;
	}
	
	protected abstract View createView(Context context);
	
}
