package com.elancier.meenatchi_CRM.retrofit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class Customviewpager extends ViewPager {
    private boolean swipeable = false;

    public Customviewpager(Context context) {
        super(context);
    }

    public Customviewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Call this method in your motion events when you want to disable or enable
// It should work as desired.
    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return (this.swipeable) ? super.onInterceptTouchEvent(arg0) : false;
    }
}
