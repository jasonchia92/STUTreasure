package com.projectstu.stutreasure;

import java.util.ArrayList;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

//控制首頁照片頁面的手勢gesture
public class MyPagerAdapter extends PagerAdapter{

	
	private ArrayList<View> views;
	private ArrayList<String> titles;
	
	
	public MyPagerAdapter(ArrayList<View> views,ArrayList<String> titles){
		
		this.views = views;
		this.titles = titles;
	}
	
	@Override
	public int getCount() {
		return this.views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}
	
	//����view
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
	}
}
