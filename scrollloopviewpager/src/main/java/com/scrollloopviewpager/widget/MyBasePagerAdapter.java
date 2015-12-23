package com.scrollloopviewpager.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * MyBasePagerAdapter
 */
public class  MyBasePagerAdapter<T> extends PagerAdapter {
    public T[] objects;
    public Context context;

    public MyBasePagerAdapter(Context context,T[] objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}