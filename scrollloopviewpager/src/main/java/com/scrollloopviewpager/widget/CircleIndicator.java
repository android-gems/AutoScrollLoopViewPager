package com.scrollloopviewpager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.autoscrollloopviewpager.scrollloopviewpager.R;

/***
 * indicator for scroll
 *
 * @author BoBoMEe
 */

public class CircleIndicator extends View implements ViewPager.OnPageChangeListener, LoopViewPager.OnPageChangeListener {

    private int mCount;
    private int viewWidth;
    private int actualWidth;
    private int selectedPos;

    private Paint selectedPaint;
    private Paint unselectedPaint;

    private int radius = 3;
    private int dotMargin = 5;
    private int paddingTop = 15;
    private int paddingBottom = 15;
    private int unselected_color = Color.CYAN;
    private int selected_color = Color.RED;

    public CircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        handlerAttrs(context, attrs);
    }

    private void handlerAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
        radius = ta.getDimensionPixelOffset(R.styleable.CircleIndicator_radius, dip2px(context,radius));
        dotMargin = ta.getDimensionPixelOffset(R.styleable.CircleIndicator_dotMargin, dip2px(context,dotMargin));
        paddingTop = ta.getDimensionPixelOffset(R.styleable.CircleIndicator_paddingTop, dip2px(context,paddingTop));
        paddingBottom = ta.getDimensionPixelOffset(R.styleable.CircleIndicator_paddingBottom, dip2px(context,paddingBottom));
        selected_color = ta.getColor(R.styleable.CircleIndicator_selected_color, selected_color);
        unselected_color = ta.getColor(R.styleable.CircleIndicator_unselected_color, unselected_color);
    }

    public int getDotMargin() {
        return dotMargin;
    }

    public void setDotMargin(int dotMargin) {
        this.dotMargin = dip2px(getContext(), dotMargin);
        invalidate();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = dip2px(getContext(), radius);
        invalidate();
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = dip2px(getContext(), paddingTop);
        invalidate();
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = dip2px(getContext(), paddingBottom);
        invalidate();
    }

    public int getUnselected_color() {
        return unselected_color;
    }

    public void setUnselected_color(int unselected_color) {
        this.unselected_color = unselected_color;
        invalidate();
    }

    public int getSelected_color() {
        return selected_color;
    }

    public void setSelected_color(int selected_color) {
        this.selected_color = selected_color;
        invalidate();
    }

    public PageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = (viewWidth - actualWidth) / 2 + radius;
        int y = radius + paddingTop;
        for (int i = 0; i < mCount; i++) {
            boolean selected = selectedPos == i;
            canvas.drawCircle(x, y, radius, selected ? selectedPaint
                    : unselectedPaint);
            x += (radius * 2 + dotMargin);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = getMeasurement(widthMeasureSpec, actualWidth);
        int mViewHeight = getMeasurement(heightMeasureSpec, radius * 2
                + paddingBottom + paddingTop);
        setMeasuredDimension(viewWidth, mViewHeight);
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement = 0;

        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
        i = (mCount + i % mCount) % mCount;
        if (null != onPageChangeListener) {
            onPageChangeListener.onPageScrolled(i, v, i2);
            postInvalidate();
        }

    }

    @Override
    public void onPageSelected(int i) {
        i = (mCount + i % mCount) % mCount;
        selectedPos = i;
        if (null != onPageChangeListener) {
            onPageChangeListener.onPageSelected(i);
        }
        postInvalidate();
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        i = (mCount + i % mCount) % mCount;
        if (null != onPageChangeListener) {
            onPageChangeListener.onPageScrollStateChanged(i);
            postInvalidate();
        }

    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setViewPager(ViewGroup viewpager) {
        selectedPaint = new Paint();
        selectedPaint.setAntiAlias(true);
        selectedPaint.setColor(selected_color);
        selectedPaint.setStyle(Paint.Style.FILL);
        unselectedPaint = new Paint(selectedPaint);
        unselectedPaint.setColor(unselected_color);
        if (viewpager instanceof ViewPager) {
            ((ViewPager) viewpager).setOnPageChangeListener(this);
            mCount = ((ViewPager) viewpager).getAdapter().getCount();
        } else if (viewpager instanceof LoopViewPager) {
            ((LoopViewPager) viewpager).setOnPageChangeListener(this);
            mCount = ((LoopViewPager) viewpager).getAdapter().getCount();
        }

        actualWidth = dotMargin * (mCount - 1) + radius * 2 * mCount;
        invalidate();
    }

    /**
     * 对外的ViewPager的回调接口
     */
    public interface PageChangeListener {
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    // 对外的ViewPager的回调接口
    private PageChangeListener onPageChangeListener;

    // 对外的ViewPager的回调接口的设置
    public void setOnPageChangeListener(PageChangeListener pageChangeListener) {
        this.onPageChangeListener = pageChangeListener;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
        invalidate();
    }

}
