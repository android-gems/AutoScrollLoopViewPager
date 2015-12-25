package com.scrollloopviewpager.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.autoscrollloopviewpager.scrollloopviewpager.R;

import java.util.ArrayList;

/***
 * banner
 *
 * @author BoBoMEe
 */

public class MyBanner extends RelativeLayout{

    // 自动轮播的时间间隔
    private final static int TIME_INTERVAL = 3;
    // 自动轮播启用开关
    private final static boolean _isAutoPlay = true;
    private ArrayList<Integer> bannerList;
    private AutoScrollViewPager viewPager;
    private Context context;
    private CircleIndicator pageIndex;
    public MyBanner(Context context) {
        this(context, null);
    }

    public MyBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public void setData(ArrayList<Integer> list){
        this.bannerList = list;
        initUI(this.context);
        if (_isAutoPlay) {
            viewPager.setInterval(TIME_INTERVAL * 1000);
            viewPager.startAutoScroll();
        }
    }

    public void setDurtion(double d) {
        viewPager.setAutoScrollDurationFactor(d);
    }

    private void initUI(Context context) {
        viewPager.setAdapter(new MyPagerAdapter(context, bannerList.toArray()));

        pageIndex.setViewPager(viewPager);
        pageIndex.setSelectedPos(0);
    }

    class MyPagerAdapter extends MyBasePagerAdapter{
        public MyPagerAdapter(Context context, Object[] objects) {
            super(context, objects);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = (getCount() + position % getCount()) % getCount();
            ImageView iv = new ImageView(context);
            iv.setImageResource((Integer) objects[position]);
            container.addView(iv);
            return iv;
        }
    }

    private int downX;
    private int downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 父控件不要拦截
                getParent().requestDisallowInterceptTouchEvent(true);

                downX = (int) ev.getX();
                downY = (int) ev.getY();
                viewPager.stopAutoScroll();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                // 下滑
                if (Math.abs(moveY - downY) > Math.abs(moveX - downX)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);

                }
                break;
            case MotionEvent.ACTION_CANCEL:
                viewPager.startAutoScroll();
                break;
            case MotionEvent.ACTION_UP:
                viewPager.startAutoScroll();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        if (!this.isInEditMode()) {
            // pic scale
//            DisplayMetrics dm = new DisplayMetrics();
//            ((Activity) (this.context)).getWindowManager().getDefaultDisplay()
//                    .getMetrics(dm);
//            int thumbnailWidth = dm.widthPixels;
//            double xScale = 240d / 640.0d;
//            int thumbnailHeight = (int) (thumbnailWidth * xScale);
//            LinearLayout.LayoutParams bannerLP = new LinearLayout.LayoutParams(
//                    thumbnailWidth, thumbnailHeight);
//            this.setLayoutParams(bannerLP);

            // 3.Viewpager
            viewPager = (AutoScrollViewPager)
                    findViewById(R.id.picslooper);
            viewPager.setFocusable(true);

            // 2. 圆点
            pageIndex = (CircleIndicator) findViewById(R.id.pageIndexor);
            pageIndex.setDotMargin(10);
            pageIndex.setPaddingBottom(6);
        }
        super.onFinishInflate();
    }
}
